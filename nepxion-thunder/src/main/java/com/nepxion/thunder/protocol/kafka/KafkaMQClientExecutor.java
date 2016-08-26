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
import com.nepxion.thunder.common.entity.ConnectionEntity;
import com.nepxion.thunder.common.entity.ReferenceEntity;
import com.nepxion.thunder.protocol.AbstractClientExecutor;
import com.nepxion.thunder.protocol.ProtocolException;

public class KafkaMQClientExecutor extends AbstractClientExecutor implements KafkaMQExecutorDelegate {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMQClientExecutor.class);

    @Override
    public void start(final String interfaze, final ApplicationEntity applicationEntity) throws Exception {
        final String server = getServer(interfaze);
        
        final Map<String, KafkaMQContext> contextMap = KafkaMQCacheContainer.getReferenceContextMap();
        
        final CyclicBarrier barrier = new CyclicBarrier(2);
        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    KafkaMQContext context = contextMap.get(server);
                    if (context == null) {
                        context = new KafkaMQContext(KafkaMQClientExecutor.this);
                        context.initializeContext(interfaze, server);
                        contextMap.put(server, context);
                    }
                    context.initializeRequestContext(interfaze, applicationEntity);
                } catch (Exception e) {
                    LOG.error("Start MQ failed", e);
                }

                barrier.await();

                return null;
            }
        });

        barrier.await();
    }
        
    private String getServer(String interfaze) {
        Map<String, ReferenceEntity> referenceEntityMap = cacheContainer.getReferenceEntityMap();
        ReferenceEntity referenceEntity = referenceEntityMap.get(interfaze);
        
        return referenceEntity.getServer();
    }
    
    @Override
    public boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        String server = getServer(interfaze);
        
        Map<String, KafkaMQContext> contextMap = KafkaMQCacheContainer.getReferenceContextMap();
        
        return contextMap.get(server) != null;
    }
    
    @Override
    public ConnectionEntity online(String interfaze, ApplicationEntity applicationEntity, Object connnectionHandler) throws Exception {
        throw new ProtocolException("Online feature isn't supported in KafkaMQClientExecutor");
    }

    @Override
    public void offline(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        throw new ProtocolException("Offline feature isn't supported in KafkaMQClientExecutor");
    }
}