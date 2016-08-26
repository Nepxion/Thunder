package com.nepxion.thunder.protocol;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.CallbackType;
import com.nepxion.thunder.common.entity.MethodEntity;

public abstract class AbstractClientInterceptor extends ThunderDelegateImpl implements ClientInterceptor {    
    protected String interfaze;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long timestamp = System.currentTimeMillis();
        
        ProtocolRequest request = new ProtocolRequest();
        request.setProcessStartTime(timestamp);
        request.setProcessEndTime(timestamp);
        request.setDeliverStartTime(timestamp);
        
        String methodName = invocation.getMethod().getName();
        Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
        Object[] arguments = invocation.getArguments();
        // Class<?> returnType = invocation.getMethod().getReturnType();

        MethodEntity methodEntity = cacheContainer.getMethodEntity(interfaze, methodName, parameterTypes);
                
        int traceIdIndex = methodEntity.getTraceIdIndex();
        boolean async = methodEntity.isAsync();
        String callback = null;
        CallbackType callbackType = methodEntity.getCallbackType();
        if (callbackType != null) {
            if (callbackType == CallbackType.PROMISE) {
                callback = CallbackType.PROMISE.toString();
            } else {
                callback = methodEntity.getCallback().getClass().getName();
            }
        }
        long timeout = methodEntity.getTimeout();
        boolean broadcast = methodEntity.isBroadcast();
        boolean heartbeat = false;
        boolean feedback = !(broadcast || heartbeat || (async && !methodEntity.isCallback()));
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        String fromCluster = applicationEntity.getCluster();
        String fromUrl = applicationEntity.toUrl();
 
        request.setFromCluster(fromCluster);
        request.setFromUrl(fromUrl);
        if (arguments.length > traceIdIndex) {
            request.setTraceId(arguments[traceIdIndex].toString());
        }
        request.setInterface(interfaze);
        request.setMethod(methodName);
        request.setParameterTypes(parameterTypes);
        request.setParameters(arguments);
        request.setAsync(async);
        request.setCallback(callback);
        request.setTimeout(timeout);
        request.setBroadcast(broadcast);
        request.setHeartbeat(heartbeat);
        request.setFeedback(feedback);
        
        Map<String, ReferenceConfig> referenceConfigMap = cacheContainer.getReferenceConfigMap();
        ReferenceConfig referenceConfig = referenceConfigMap.get(interfaze);
        request.setReferenceConfig(referenceConfig);

        if (request.isAsync()) {
            if (request.isBroadcast()) {
                invokeBroadcast(request);
            } else {
                ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();
                clientInterceptorAdapter.persistAsync(request, methodEntity);
                
                invokeAsync(request);
            }
            
            return null;
        } else {
            return invokeSync(request);
        }
    }

    @Override
    public void setInterface(String interfaze) {
        this.interfaze = interfaze;
    }
}