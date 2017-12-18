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

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.security.SecurityExecutor;
import com.nepxion.thunder.serialization.SerializerExecutor;

public class ServerExecutorAdapter extends AbstractDominationExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ServerExecutorAdapter.class);

    public void handle(ProtocolRequest request, ProtocolResponse response) throws Exception {
        long startTimestamp = System.currentTimeMillis();
        request.setDeliverEndTime(startTimestamp);
        response.setProcessStartTime(startTimestamp);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Request={}", SerializerExecutor.toJson(request));
        }

        try {
            handleResult(request, response);
        } catch (Exception e) {
            handleException(e, response);
            throw e;
        } finally {
            boolean heartbeat = request.isHeartbeat();
            if (heartbeat) {
                return;
            }
            
            long endTimestamp = System.currentTimeMillis();
            response.setProcessEndTime(endTimestamp);
            response.setDeliverStartTime(endTimestamp);
            
            handleMonitor(request);
            handleNoFeedbackMonitor(response);
            
            handleNoFeedbackEvent(response, ApplicationType.SERVICE);
        }
    }

    private void handleResult(ProtocolRequest request, ProtocolResponse response) throws Exception {
        boolean heartbeat = request.isHeartbeat();
        if (heartbeat) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Receive heart beat request...");
            }
            
            response.setHeartbeat(heartbeat);
            
            return;
        }

        String messageId = request.getMessageId();
        String traceId = request.getTraceId();
        String interfaze = request.getInterface();
        String methodName = request.getMethod();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        boolean async = request.isAsync();
        String callback = request.getCallback();
        long timeout = request.getTimeout();
        boolean broadcast = request.isBroadcast();
        boolean feedback = request.isFeedback();
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        String responseCluster = applicationEntity.getCluster();
        String responseUrl = applicationEntity.toUrl();
        String requestCluster = request.getFromCluster();
        String requestUrl = request.getFromUrl();
        
        request.setToCluster(responseCluster);
        request.setToUrl(responseUrl);
        
        response.setMessageId(messageId);
        response.setTraceId(traceId);
        response.setInterface(interfaze);
        response.setMethod(methodName);
        response.setParameterTypes(parameterTypes);
        response.setParameters(parameters);
        response.setAsync(async);
        response.setCallback(callback);
        response.setTimeout(timeout);
        response.setBroadcast(broadcast);
        response.setFeedback(feedback);
        response.setFromCluster(responseCluster);
        response.setFromUrl(responseUrl);
        response.setToCluster(requestCluster);
        response.setToUrl(requestUrl);

        SecurityExecutor securityExecutor = executorContainer.getSecurityExecutor();
        if (securityExecutor != null) {
            boolean authorized = securityExecutor.execute(request, response);
            if (!authorized) {
                return;
            }
        }

        Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
        ServiceEntity serviceEntity = serviceEntityMap.get(interfaze);

        Object service = serviceEntity.getService();
        Class<?> clazz = service.getClass();
        Method method = clazz.getMethod(methodName, parameterTypes);
        Object result = method.invoke(service, parameters);
        response.setResult(result);
    }

    private void handleException(Exception e, ProtocolResponse response) {
        response.setException(e);
    }
        
    private void handleNoFeedbackMonitor(ProtocolResponse response) {
        if (response.isFeedback()) {
            return;
        }
        
        response.setDeliverEndTime(response.getDeliverStartTime());
        response.setToCluster(response.getFromCluster());
        response.setToUrl(response.getFromUrl());
        
        handleMonitor(response);
    }
    
    private void handleNoFeedbackEvent(ProtocolResponse response, ApplicationType applicationType) {
        if (response.isFeedback()) {
            return;
        }
        
        handleEvent(response, applicationType);
    }
}