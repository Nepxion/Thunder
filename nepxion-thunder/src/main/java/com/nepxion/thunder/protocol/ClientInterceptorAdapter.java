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

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.callback.ThunderCallback;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.CallbackType;
import com.nepxion.thunder.common.entity.MethodEntity;
import com.nepxion.thunder.common.entity.ResponseAsyncEntity;
import com.nepxion.thunder.common.entity.ResponseSyncEntity;
import com.nepxion.thunder.common.promise.PromiseContext;
import com.nepxion.thunder.common.promise.PromiseEntity;

public class ClientInterceptorAdapter extends ThunderDelegateImpl {
    private static final Logger LOG = LoggerFactory.getLogger(ClientInterceptorAdapter.class);
    
    private DominationExecutor dominationExecutor;
    
    private AtomicBoolean start = new AtomicBoolean(false);
    
    public void persistAsync(ProtocolRequest request, MethodEntity methodEntity) {
        CallbackType callbackType = methodEntity.getCallbackType();
        if (callbackType == null) {
            return;
        }
        
        String messageId = request.getMessageId();
        ResponseAsyncEntity responseEntity = new ResponseAsyncEntity();
        responseEntity.setRequest(request);
        
        if (callbackType == CallbackType.PROMISE) {
            PromiseEntity<Object> promise = new PromiseEntity<Object>();

            PromiseContext.setPromise(promise);
            responseEntity.setPromise(promise);
        }
        
        Map<String, ResponseAsyncEntity> responseEntityMap = cacheContainer.getResponseAsyncEntityMap();
        responseEntityMap.put(messageId, responseEntity);
        
        if (start.getAndSet(true)) {
            return;
        }

        long scan = properties.getLong(ThunderConstants.ASYNC_SCAN_ATTRIBUTE_NAME);
        
        Thread scanAsyncThread = new Thread(new ScanAsyncRunnable(responseEntityMap, scan), "Scan Async");
        scanAsyncThread.setDaemon(true);
        scanAsyncThread.start();
    }
    
    // 内部类被虚拟机加载才执行clinit(并且jvm会加锁)的机制来启动这个守护扫描线程
    private class ScanAsyncRunnable implements Runnable {
        private Map<String, ResponseAsyncEntity> responseEntityMap;
        private long scan;
        
        public ScanAsyncRunnable(Map<String, ResponseAsyncEntity> responseEntityMap, long scan) {
            this.responseEntityMap = responseEntityMap;
            this.scan = scan;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    if (MapUtils.isNotEmpty(responseEntityMap)) {
                        Iterator<Map.Entry<String, ResponseAsyncEntity>> iterator = responseEntityMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, ResponseAsyncEntity> entry = iterator.next();
                            ResponseAsyncEntity responseEntity = entry.getValue();
                            ProtocolRequest request = responseEntity.getRequest();
                            long timeout = request.getTimeout();
                            if (System.currentTimeMillis() - request.getProcessStartTime() > timeout) {
                                iterator.remove();

                                TimeoutException timeoutException = new TimeoutException();
                                LOG.error("Async method timeout", timeoutException);
                                
                                invokeTimeout(request, timeoutException);
                            }
                        }
                    }
                    
                    TimeUnit.MILLISECONDS.sleep(scan);
                } catch (Exception e) {
                    LOG.error("Scan async cache failed", e);
                }
            }
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void handleAsync(ProtocolResponse response, MethodEntity methodEntity) {
        String messageId = response.getMessageId();
        Object result = response.getResult();
        Exception exception = response.getException();
        
        CallbackType callbackType = methodEntity.getCallbackType();
        if (callbackType == null) {
            return;
        }
        
        Map<String, ResponseAsyncEntity> responseEntityMap = cacheContainer.getResponseAsyncEntityMap();
        ResponseAsyncEntity responseEntity = responseEntityMap.remove(messageId);
        if (responseEntity != null) {
            if (callbackType == CallbackType.PROMISE) {
                PromiseEntity promise = responseEntity.getPromise();
                if (promise != null) {
                    if (exception == null) {
                        promise.resolve(result);
                    } else {
                        promise.reject(exception);
                    }
                }

            } else {
                ThunderCallback callback = methodEntity.getCallback();
                if (callback != null) {
                    callback.call(result, exception);
                }
            }
        } else {
            LOG.warn("Expired async response for messageId={}, ignore", messageId);
        }
    }
    
    public Object invokeSync(ClientInterceptor clientInterceptor, ProtocolRequest request) throws Exception {        
        ResponseSyncEntity responseEntity = new ResponseSyncEntity();
        CyclicBarrier barrier = new CyclicBarrier(2);
        responseEntity.setBarrier(barrier);

        String messageId = request.getMessageId();
        long timeout = request.getTimeout();
        Map<String, ResponseSyncEntity> responseEntityMap = cacheContainer.getResponseSyncEntityMap();
        responseEntityMap.put(messageId, responseEntity);

        clientInterceptor.invokeAsync(request);

        Exception timeoutException = null;
        try {
            try {
                responseEntity.getBarrier().await(timeout, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                LOG.error("Sync method timeout", e);
                
                responseEntity.setResult(null);
                responseEntity.setException(e);
                
                timeoutException = e;
            }
            
            if (responseEntity.getException() != null) {
                throw responseEntity.getException();
            }

            return responseEntity.getResult();
        } finally {
            if (responseEntityMap.get(messageId) != null) {
                responseEntityMap.remove(messageId);
            }
            
            invokeTimeout(request, timeoutException);
        }
    }
    
    public void handleSync(ProtocolResponse response) throws Exception {
        String messageId = response.getMessageId();
        Map<String, ResponseSyncEntity> responseEntityMap = cacheContainer.getResponseSyncEntityMap();
        try {
            ResponseSyncEntity responseEntity = responseEntityMap.get(messageId);
            if (responseEntity != null) {
                responseEntity.setResult(response.getResult());
                responseEntity.setException(response.getException());

                CyclicBarrier barrier = responseEntity.getBarrier();
                barrier.await();
            } else {
                LOG.warn("Expired sync response for messageId={}, ignore", messageId);
            }
        } finally {
            if (responseEntityMap.get(messageId) != null) {
                responseEntityMap.remove(messageId);
            }
        }
    }
    
    private void invokeTimeout(ProtocolRequest request, Exception timeoutException) {
        if (timeoutException == null) {
            return;
        }
        
        long timestamp = System.currentTimeMillis();
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        String toCluster = applicationEntity.getCluster();
        String toUrl = applicationEntity.toUrl();
        
        request.setToCluster(toCluster);
        request.setToUrl(toUrl);
        request.setProcessEndTime(timestamp);
        request.setDeliverEndTime(timestamp);
        request.setException(timeoutException);

        if (dominationExecutor == null) {
            dominationExecutor = new AbstractDominationExecutor() {
                @Override
                public CacheContainer getCacheContainer() {
                    return ClientInterceptorAdapter.this.getCacheContainer();
                }

                @Override
                public ExecutorContainer getExecutorContainer() {
                    return ClientInterceptorAdapter.this.getExecutorContainer();
                }
            };
        }

        dominationExecutor.handleMonitor(request);
        dominationExecutor.handleEvent(request, ApplicationType.REFERENCE);
    }
}