package com.nepxion.thunder.protocol.mq;

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

import javax.jms.Destination;

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ReferenceEntity;
import com.nepxion.thunder.protocol.AbstractClientInterceptor;
import com.nepxion.thunder.protocol.ClientInterceptorAdapter;
import com.nepxion.thunder.protocol.ProtocolRequest;

public class MQClientInterceptor extends AbstractClientInterceptor {
    
    @Override
    public void invokeAsync(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();
        boolean isAsync = request.isAsync();
        
        Destination queueResponseDestination = null;
        Destination queueRequestDestination = null;
        
        if (isAsync) {
            queueResponseDestination = MQCacheContainer.getMQQueueDestinationContainer().getAsyncResponseDestinationMap().get(interfaze);
            queueRequestDestination = MQCacheContainer.getMQQueueDestinationContainer().getAsyncRequestDestinationMap().get(interfaze);
        } else {
            queueResponseDestination = MQCacheContainer.getMQQueueDestinationContainer().getSyncResponseDestinationMap().get(interfaze);
            queueRequestDestination = MQCacheContainer.getMQQueueDestinationContainer().getSyncRequestDestinationMap().get(interfaze);
        }
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        MQProducer mqProducer = getMQProducer(interfaze);
        mqProducer.produceRequest(queueResponseDestination, queueRequestDestination, applicationEntity, request);
    }

    @Override
    public Object invokeSync(ProtocolRequest request) throws Exception {
        ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();
        
        return clientInterceptorAdapter.invokeSync(this, request);
    }

    @Override
    public void invokeBroadcast(ProtocolRequest request) throws Exception {
        String interfaze = request.getInterface();
        
        Destination topicResponseDestination = MQCacheContainer.getMQTopicDestinationContainer().getAsyncResponseDestinationMap().get(interfaze);
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        MQProducer mqProducer = getMQProducer(interfaze);
        mqProducer.produceRequest(topicResponseDestination, null, applicationEntity, request);
    }
    
    private MQProducer getMQProducer(String interfaze) {
        Map<String, ReferenceEntity> referenceEntityMap = cacheContainer.getReferenceEntityMap();
        ReferenceEntity referenceEntity = referenceEntityMap.get(interfaze);
        String server = referenceEntity.getServer();
        
        return MQCacheContainer.getReferenceContextMap().get(server).getMQProducer();
    }
}