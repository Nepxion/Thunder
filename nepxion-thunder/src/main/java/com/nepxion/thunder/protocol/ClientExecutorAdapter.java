package com.nepxion.thunder.protocol;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.MethodEntity;
import com.nepxion.thunder.common.entity.MethodKey;
import com.nepxion.thunder.serialization.SerializerExecutor;

public class ClientExecutorAdapter extends AbstractDominationExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ClientExecutorAdapter.class);

    public void handle(ProtocolResponse response) throws Exception {
        response.setDeliverEndTime(System.currentTimeMillis());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Response={}", SerializerExecutor.toJson(response));
        }

        try {
            if (response.isAsync()) {
                handleAsync(response);
            } else {
                handleSync(response);
            }
        } catch (Exception e) {
            handleException(e, response);
            throw e;
        } finally {
            boolean heartbeat = response.isHeartbeat();
            if (heartbeat) {
                return;
            }

            handleMonitor(response);

            handleEvent(response, ApplicationType.REFERENCE);
        }
    }

    @SuppressWarnings("all")
    protected void handleAsync(ProtocolResponse response) throws Exception {
        boolean heartbeat = response.isHeartbeat();
        boolean broadcast = response.isBroadcast();
        if (heartbeat || broadcast) {
            return;
        }

        String interfaze = response.getInterface();
        MethodKey methodKey = response.createMethodKey();
        MethodEntity methodEntity = cacheContainer.getMethodEntity(interfaze, methodKey);

        ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();
        clientInterceptorAdapter.handleAsync(response, methodEntity);
    }

    private void handleSync(ProtocolResponse response) throws Exception {
        ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();
        clientInterceptorAdapter.handleSync(response); 
    }

    private void handleException(Exception e, ProtocolResponse response) {
        e.printStackTrace();
    }
}