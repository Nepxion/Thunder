package com.nepxion.thunder.protocol.kafka;

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
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.protocol.AbstractServerExecutor;

public class KafkaMQServerExecutor extends AbstractServerExecutor implements KafkaMQExecutorDelegate {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMQServerExecutor.class);
    
    @Override
    public void start(final String interfaze, final ApplicationEntity applicationEntity) throws Exception {
        final String server = getServer(interfaze);
        
        final Map<String, KafkaMQContext> contextMap = KafkaMQCacheContainer.getServiceContextMap();

        final CyclicBarrier barrier = new CyclicBarrier(2);
        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    KafkaMQContext context = contextMap.get(server);
                    if (context == null) {
                        context = new KafkaMQContext(KafkaMQServerExecutor.this);
                        context.initializeContext(interfaze, server);
                        contextMap.put(server, context);
                    }
                    context.initializeResponseContext(interfaze, applicationEntity);
                } catch (Exception e) {
                    LOG.error("Start MQ failed", e);
                }
                
                barrier.await();

                return null;
            }
        });

        barrier.await();
    }
    
    @Override
    public boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        String server = getServer(interfaze);
        
        Map<String, KafkaMQContext> contextMap = KafkaMQCacheContainer.getServiceContextMap();
                
        return contextMap.get(server) != null;
    }
    
    private String getServer(String interfaze) {
        Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
        ServiceEntity serviceEntity = serviceEntityMap.get(interfaze);
        
        return serviceEntity.getServer();
    }
}