package com.nepxion.thunder.protocol.netty;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.compression.JdkZlibDecoder;
import io.netty.handler.codec.compression.JdkZlibEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.protocol.AbstractClientExecutor;

public class NettyClientExecutor extends AbstractClientExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClientExecutor.class);

    @Override
    public void start(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        // Netty是多个Interface共享一个通道，注册Interface后，如果对应的通道已经开启，不需要重复开启了，但需要复制
        boolean started = started(null, applicationEntity);
        if (started) {
            cacheContainer.getConnectionCacheEntity().duplicateConnectionEntity(interfaze, applicationEntity);

            return;
        }

        CyclicBarrier barrier = new CyclicBarrier(2);

        connect(interfaze, applicationEntity, barrier);

        // 主线程等待连接成功后，第一个await执行，唤醒当前连接线程和主线程
        // 两倍于Netty的连接超时事件，这么做防止服务端Netty连接还不完成，客户端就开始调用服务
        barrier.await(properties.getLong(ThunderConstant.NETTY_CONNECT_TIMEOUT_ATTRIBUTE_NAME) * 2, TimeUnit.MILLISECONDS);
    }

    private void connect(final String interfaze, final ApplicationEntity applicationEntity, final CyclicBarrier barrier) throws Exception {
        final String host = applicationEntity.getHost();
        final int port = applicationEntity.getPort();

        LOG.info("Attempt to connect to {}:{}", host, port);

        final ThreadFactory threadFactory = new AffinityThreadFactory("ClientAffinityThreadFactory", AffinityStrategies.DIFFERENT_CORE);
        final EventLoopGroup group = new NioEventLoopGroup(ThunderConstant.CPUS, threadFactory);
        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    Bootstrap client = new Bootstrap();
                    client.group(group).channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .option(ChannelOption.SO_SNDBUF, properties.getInteger(ThunderConstant.NETTY_SO_SNDBUF_ATTRIBUTE_NAME))
                            .option(ChannelOption.SO_RCVBUF, properties.getInteger(ThunderConstant.NETTY_SO_RCVBUF_ATTRIBUTE_NAME))
                            .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getInteger(ThunderConstant.NETTY_CONNECT_TIMEOUT_ATTRIBUTE_NAME))
                            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                            .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, properties.getInteger(ThunderConstant.NETTY_WRITE_BUFFER_LOW_WATER_MARK_ATTRIBUTE_NAME))
                            .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, properties.getInteger(ThunderConstant.NETTY_WRITE_BUFFER_HIGH_WATER_MARK_ATTRIBUTE_NAME))
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel channel) throws Exception {
                                    channel.pipeline()
                                            .addLast(new IdleStateHandler(properties.getLong(ThunderConstant.NETTY_WRITE_IDLE_TIME_ATTRIBUTE_NAME), properties.getLong(ThunderConstant.NETTY_READ_IDLE_TIME_ATTRIBUTE_NAME), properties.getLong(ThunderConstant.NETTY_ALL_IDLE_TIME_ATTRIBUTE_NAME), TimeUnit.MILLISECONDS))
                                            .addLast(new NettyObjectDecoder(properties.getInteger(ThunderConstant.NETTY_MAX_MESSAGE_SIZE_ATTRIBUTE_NAME)))
                                            .addLast(new NettyObjectEncoder())
                                            .addLast(new JdkZlibDecoder())
                                            .addLast(new JdkZlibEncoder())
                                            .addLast(new WriteTimeoutHandler(properties.getInteger(ThunderConstant.NETTY_WRITE_TIMEOUT_ATTRIBUTE_NAME)))
                                            .addLast(new ReadTimeoutHandler(properties.getInteger(ThunderConstant.NETTY_READ_TIMEOUT_ATTRIBUTE_NAME)))
                                            .addLast(new NettyClientHandler(cacheContainer, executorContainer, properties.getBoolean(ThunderConstant.TRANSPORT_LOG_PRINT_ATTRIBUTE_NAME), properties.getBoolean(ThunderConstant.HEART_BEAT_LOG_PRINT_ATTRIBUTE_NAME)));
                                }
                            });

                    ChannelFuture channelFuture = null;
                    try {
                        channelFuture = client.connect(host, port).sync();
                    } catch (Exception e) {
                        LOG.info("Connect failed", e);

                        // 如果连不上，那么让本地缓存中对应的连接信息失效
                        offline(interfaze, applicationEntity);

                        // 失效后，马上唤醒线程
                        if (barrier != null) {
                            barrier.await();
                        }

                        return null;
                    }

                    online(interfaze, applicationEntity, channelFuture);

                    LOG.info("Connect to {}:{} successfully", host, port);

                    // 连接成功后唤醒当前连接线程和主线程，但要等待另外一个await
                    if (barrier != null) {
                        barrier.await();
                    }

                    // 作用：做阻塞线程用，一直等待服务端Channel关闭，然后结束线程
                    channelFuture.channel().closeFuture().sync();

                    offline(null, applicationEntity);
                } catch (Exception e) {
                    LOG.error("Client executor exception", e);
                } finally {
                    group.shutdownGracefully().sync();

                    TimeUnit.MILLISECONDS.sleep(properties.getInteger(ThunderConstant.NETTY_RECONNECT_DELAY_ATTRIBUTE_NAME));

                    // 启动重连
                    // 判断注册中心是否有该服务Online(即ApplicationEntity注册)。如果ApplicationEntity不存在，则表示服务端下线，不需要重连；如果存在，表示是因为网络的原因，则需要重连
                    boolean online = executorContainer.getRegistryExecutor().isServiceInstanceOnline(interfaze, applicationEntity);
                    // 避免开启多个相同的通道
                    boolean started = started(null, applicationEntity);
                    if (online && !started) {
                        LOG.info("Channel is closed, remote address={}:{}, try to reconnect...", host, port);
                        connect(interfaze, applicationEntity, null);
                    }
                }

                return null;
            }
        });
    }
}