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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
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
import com.nepxion.thunder.protocol.ProtocolMessage;
import com.nepxion.thunder.protocol.ProtocolRequest;
import com.nepxion.thunder.protocol.ProtocolResponse;
import com.nepxion.thunder.protocol.ServerExecutorAdapter;

public class NettyServerHandler extends SimpleChannelInboundHandler<ProtocolRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(NettyServerHandler.class);

    private CacheContainer cacheContainer;
    private ExecutorContainer executorContainer;
    private boolean transportLogPrint;

    public NettyServerHandler(CacheContainer cacheContainer, ExecutorContainer executorContainer, boolean transportLogPrint) {
        this.cacheContainer = cacheContainer;
        this.executorContainer = executorContainer;
        this.transportLogPrint = transportLogPrint;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext context, final ProtocolRequest request) throws Exception {
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        final String url = applicationEntity.toUrl();
        final String interfaze = request.getInterface();
        ThreadPoolFactory.createThreadPoolServerExecutor(url, interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if (transportLogPrint) {
                    boolean heartbeat = request.isHeartbeat();
                    if (!heartbeat) {
                        LOG.info("Request from client={}, service={}", getRemoteAddress(context), interfaze);
                    }
                }
                
                ProtocolResponse response = new ProtocolResponse();
                try {
                    ServerExecutorAdapter serverExecutorAdapter = executorContainer.getServerExecutorAdapter();
                    serverExecutorAdapter.handle(request, response);
                } catch (Exception e) {
                    LOG.error("Server handle failed", e);
                } finally {
                    try {
                        boolean feedback = request.isFeedback();
                        if (feedback) {
                            context.writeAndFlush(response);
                        }
                    } catch(Exception e) {
                        LOG.error("Channel write and flush failed", e);
                    } finally {
                        ReferenceCountUtil.release(request);
                    }
                }

                return null;
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        // LOG.error("Unexpected exception from downstream, cause={}", cause.getMessage(), cause);
        // LOG.warn("Unexpected exception from downstream, remote address={}, cause={}", getRemoteAddress(context), cause.getClass().getName(), cause);
        
        if (cause instanceof IOException) {
            LOG.warn("Channel is closed, remote address={}...", getRemoteAddress(context));
        }
        
        ProtocolMessage message = new ProtocolMessage();
        message.setFromUrl(getRemoteAddress(context).toString());
        message.setToUrl(getLocalAddress(context).toString());
        message.setException((Exception) cause);
        ProtocolEventFactory.postServerSystemEvent(ProtocolType.NETTY, message);
    }
    
    public SocketAddress getLocalAddress(ChannelHandlerContext context) {
        return context.channel().localAddress();
    }
    
    public SocketAddress getRemoteAddress(ChannelHandlerContext context) {
        return context.channel().remoteAddress();
    }
}