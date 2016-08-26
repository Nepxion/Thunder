package com.nepxion.thunder.protocol.netty;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import io.netty.channel.ChannelFuture;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.cluster.loadbalance.LoadBalanceExecutor;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ConnectionEntity;
import com.nepxion.thunder.event.protocol.ProtocolEventFactory;
import com.nepxion.thunder.protocol.AbstractClientInterceptor;
import com.nepxion.thunder.protocol.ClientInterceptorAdapter;
import com.nepxion.thunder.protocol.ProtocolRequest;
import com.nepxion.thunder.protocol.redis.sentinel.RedisPublisher;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSentinelPoolFactory;

public class NettyClientInterceptor extends AbstractClientInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(NettyClientInterceptor.class);

    @Override
    public void invokeAsync(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();

        LoadBalanceExecutor loadBalanceExecutor = executorContainer.getLoadBalanceExecutor();
        ConnectionEntity connectionEntity = null;
        try {
            connectionEntity = loadBalanceExecutor.loadBalance(interfaze);
        } catch (Exception e) {
            request.setException(e);
            ProtocolEventFactory.postClientProducerEvent(cacheContainer.getProtocolEntity().getType(), request);
            
            throw e;
        }
        
        if (connectionEntity == null) {
            return;
        }

        ChannelFuture channelFuture = (ChannelFuture) connectionEntity.getConnectionHandler();
        if (channelFuture != null) {
            channelFuture.channel().writeAndFlush(request);
        }
    }

    @Override
    public Object invokeSync(ProtocolRequest request) throws Exception {
        ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();

        return clientInterceptorAdapter.invokeSync(this, request);
    }

    @Override
    public void invokeBroadcast(ProtocolRequest request) throws Exception {
        boolean redisEnabled = RedisSentinelPoolFactory.enabled();
        if (redisEnabled) {
            invokeRedisBroadcast(request);
        } else {
            LOG.info("Redis broadcast is disabled, use round broadcast");

            invokeRoundBroadcast(request);
        }
    }

    private void invokeRedisBroadcast(ProtocolRequest request) throws Exception {
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

        RedisPublisher publisher = new RedisPublisher();
        publisher.publish(request, applicationEntity);
    }

    private void invokeRoundBroadcast(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();
        
        List<ConnectionEntity> connectionEntityList = cacheContainer.getConnectionCacheEntity().getConnectionEntityList(interfaze);
        for (ConnectionEntity connectionEntity : connectionEntityList) {
            ApplicationEntity applicationEntity = connectionEntity.getApplicationEntity();
            ChannelFuture channelFuture = (ChannelFuture) connectionEntity.getConnectionHandler();
            if (channelFuture != null) {
                try {
                    channelFuture.channel().writeAndFlush(request);
                } catch (Exception e) {
                    LOG.error("Async broadcast failed, host={}, port={}, service={}, method={}", applicationEntity.getHost(), applicationEntity.getPort(), request.getInterface(), request.getMethod());
                    throw e;
                }
            }
        }
    }
}