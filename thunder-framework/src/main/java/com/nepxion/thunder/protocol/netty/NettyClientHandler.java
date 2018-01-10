package com.nepxion.thunder.protocol.netty;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.SocketAddress;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;
import com.nepxion.thunder.event.protocol.ProtocolEventFactory;
import com.nepxion.thunder.protocol.ClientExecutorAdapter;
import com.nepxion.thunder.protocol.ProtocolMessage;
import com.nepxion.thunder.protocol.ProtocolRequest;
import com.nepxion.thunder.protocol.ProtocolResponse;

public class NettyClientHandler extends SimpleChannelInboundHandler<ProtocolResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandler.class);

    private CacheContainer cacheContainer;
    private ExecutorContainer executorContainer;
    private boolean transportLogPrint;
    private boolean heartBeatLogPrint;

    public NettyClientHandler(CacheContainer cacheContainer, ExecutorContainer executorContainer, boolean transportLogPrint, boolean heartBeatLogPrint) {
        this.cacheContainer = cacheContainer;
        this.executorContainer = executorContainer;
        this.transportLogPrint = transportLogPrint;
        this.heartBeatLogPrint = heartBeatLogPrint;
    }

    @SuppressWarnings("all")
    @Override
    protected void channelRead0(final ChannelHandlerContext context, final ProtocolResponse response) throws Exception {
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        final String url = applicationEntity.toUrl();
        final String interfaze = response.getInterface();
        ThreadPoolFactory.createThreadPoolClientExecutor(url, interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (transportLogPrint) {
                    LOG.info("Response from server={}, service={}", getRemoteAddress(context), interfaze);
                }

                try {
                    ClientExecutorAdapter clientExecutorAdapter = executorContainer.getClientExecutorAdapter();
                    clientExecutorAdapter.handle(response);
                } catch (Exception e) {
                    LOG.error("Client handle failed", e);
                } finally {
                    ReferenceCountUtil.release(response);
                }

                return null;
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        // LOG.error("Unexpected exception from downstream, cause={}", cause.getMessage(), cause);
        // LOG.warn("Unexpected exception from downstream, remote address={}, cause={}", getRemoteAddress(context), cause.getClass().getName(), cause);

        if (cause instanceof ChannelException) {
            LOG.error("Channel will be closed for {}", cause.getClass().getName());

            context.close();
        }

        ProtocolMessage message = new ProtocolMessage();
        message.setFromUrl(getRemoteAddress(context).toString());
        message.setToUrl(getLocalAddress(context).toString());
        message.setException((Exception) cause);
        ProtocolEventFactory.postClientSystemEvent(ProtocolType.NETTY, message);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object e) throws Exception {
        // 读写空闲时,发送心跳信息
        if (e instanceof IdleStateEvent) {
            /*IdleStateEvent event = (IdleStateEvent) e;
            IdleState state = event.state();
            if (state == IdleState.WRITER_IDLE) {*/
                ProtocolRequest request = new ProtocolRequest();
                request.setHeartbeat(true);
                request.setInterface(NettyHeartbeat.class.getName());
                request.setMethod("beat");
                request.setAsync(true);

                if (heartBeatLogPrint) {
                    LOG.info("Send heart beat request...");
                }

                context.writeAndFlush(request);
            /*}*/
        } /*else {
            super.userEventTriggered(context, e);
        }*/
    }

    public SocketAddress getLocalAddress(ChannelHandlerContext context) {
        return context.channel().localAddress();
    }

    public SocketAddress getRemoteAddress(ChannelHandlerContext context) {
        return context.channel().remoteAddress();
    }
}