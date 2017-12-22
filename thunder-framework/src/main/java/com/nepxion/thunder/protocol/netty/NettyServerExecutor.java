package com.nepxion.thunder.protocol.netty;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.compression.JdkZlibDecoder;
import io.netty.handler.codec.compression.JdkZlibEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.protocol.AbstractServerExecutor;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSubscriber;

public class NettyServerExecutor extends AbstractServerExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(NettyServerExecutor.class);

    private AtomicBoolean start = new AtomicBoolean(false);

    @Override
    public void start(String interfaze, final ApplicationEntity applicationEntity) throws Exception {
        boolean redisEnabled = RedisSentinelPoolFactory.enabled();
        if (redisEnabled) {
            RedisSubscriber subscriber = new RedisSubscriber(executorContainer);
            subscriber.subscribe(interfaze, applicationEntity);
        }

        boolean started = started(interfaze, applicationEntity);
        if (started) {
            return;
        }

        final String host = applicationEntity.getHost();
        final int port = applicationEntity.getPort();

        LOG.info("Attempt to start server with host={}, port={}", host, port);

        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // From https://github.com/netty/netty/wiki/Thread-Affinity
        final ThreadFactory threadFactory = new AffinityThreadFactory("ServerAffinityThreadFactory", AffinityStrategies.DIFFERENT_CORE);
        final EventLoopGroup workerGroup = new NioEventLoopGroup(2 * ThunderConstant.CPUS, threadFactory);
        Executors.newSingleThreadExecutor().submit(new Callable<ChannelFuture>() {
            @Override
            public ChannelFuture call() throws Exception {
                try {
                    ServerBootstrap server = new ServerBootstrap();
                    server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .option(ChannelOption.SO_SNDBUF, properties.getInteger(ThunderConstant.NETTY_SO_SNDBUF_ATTRIBUTE_NAME))
                            .option(ChannelOption.SO_RCVBUF, properties.getInteger(ThunderConstant.NETTY_SO_RCVBUF_ATTRIBUTE_NAME))
                            .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                            .childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, properties.getInteger(ThunderConstant.NETTY_WRITE_BUFFER_LOW_WATER_MARK_ATTRIBUTE_NAME))
                            .childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, properties.getInteger(ThunderConstant.NETTY_WRITE_BUFFER_HIGH_WATER_MARK_ATTRIBUTE_NAME))
                            .option(ChannelOption.SO_BACKLOG, properties.getInteger(ThunderConstant.NETTY_SO_BACKLOG_ATTRIBUTE_NAME))
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel channel) throws Exception {
                                    channel.pipeline()
                                            .addLast(new NettyObjectDecoder(properties.getInteger(ThunderConstant.NETTY_MAX_MESSAGE_SIZE_ATTRIBUTE_NAME)))
                                            .addLast(new NettyObjectEncoder())
                                            .addLast(new JdkZlibDecoder())
                                            .addLast(new JdkZlibEncoder())
                                            .addLast(new NettyServerHandler(cacheContainer, executorContainer, properties.getBoolean(ThunderConstant.TRANSPORT_LOG_PRINT_ATTRIBUTE_NAME)));
                                }
                            });

                    ChannelFuture channelFuture = server.bind(port).sync();

                    LOG.info("Server has started with port={} successfully", port);

                    start.set(true);

                    // 问题： 如果执行下面的代码，会把主线程给阻塞，导致容器后面代码无法执行下去，容器会支撑主线程始终运行
                    // 作用：监听端口关闭，做阻塞线程用，一直等待Channel关闭，然后结束线程
                    // channelFuture.channel().closeFuture().sync();

                    return channelFuture;
                } finally {
                    // 作为服务器，资源始终不能被释放，否则会触发Unregister事件
                    // bossGroup.shutdownGracefully().sync();
                    // workerGroup.shutdownGracefully().sync();
                }
            }
            // 同步等待，因为newSingleThreadExecutor(线程数只有一个)，所以不会等待
        }).get();
    }

    @Override
    public boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        return start.get();
    }
}