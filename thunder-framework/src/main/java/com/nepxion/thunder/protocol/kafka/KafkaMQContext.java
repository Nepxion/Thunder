package com.nepxion.thunder.protocol.kafka;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;

import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.DestinationEntity;
import com.nepxion.thunder.common.entity.DestinationType;
import com.nepxion.thunder.common.entity.MQEntity;
import com.nepxion.thunder.common.entity.MQPropertyEntity;
import com.nepxion.thunder.common.entity.ReferenceEntity;
import com.nepxion.thunder.common.util.RandomUtil;

public class KafkaMQContext {
    private CacheContainer cacheContainer;
    private ExecutorContainer executorContainer;

    private MQEntity mqEntity;
    private MQPropertyEntity mqPropertyEntity;

    private KafkaMQProducer producer;

    public KafkaMQContext(KafkaMQExecutorDelegate executorDelegate) {
        this.cacheContainer = executorDelegate.getCacheContainer();
        this.executorContainer = executorDelegate.getExecutorContainer();
        this.mqEntity = cacheContainer.getMQEntity();
    }

    public void initializeContext(String interfaze, String server) throws Exception {
        mqPropertyEntity = new MQPropertyEntity(interfaze, server, mqEntity);
    }

    public void initializeRequestContext(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        producer = new KafkaMQProducer(mqPropertyEntity);

        Map<String, ReferenceEntity> referenceEntityMap = cacheContainer.getReferenceEntityMap();
        ReferenceEntity referenceEntity = referenceEntityMap.get(interfaze);
        boolean hasFeedback = referenceEntity.hasFeedback();
        if (hasFeedback) {
            DestinationEntity requestQueueDestinationEntity = KafkaMQDestinationUtil.createDestinationEntity(DestinationType.REQUEST_QUEUE, interfaze, applicationEntity);

            initializeClientHandler(requestQueueDestinationEntity, interfaze, applicationEntity);
        }
    }

    private void initializeClientHandler(DestinationEntity destinationEntity, String interfaze, ApplicationEntity applicationEntity) throws Exception {
        String topic = destinationEntity.toString();

        KafkaMQClientHandler clientHandler = new KafkaMQClientHandler(mqPropertyEntity, topic + "-" + RandomUtil.uuidRandom());
        clientHandler.setCacheContainer(cacheContainer);
        clientHandler.setExecutorContainer(executorContainer);
        clientHandler.consume(topic, interfaze, applicationEntity);
    }

    public void initializeResponseContext(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        producer = new KafkaMQProducer(mqPropertyEntity);

        DestinationEntity responseQueueDestinationEntity = KafkaMQDestinationUtil.createDestinationEntity(DestinationType.RESPONSE_QUEUE, interfaze, applicationEntity);
        DestinationEntity responseTopicDestinationEntity = KafkaMQDestinationUtil.createDestinationEntity(DestinationType.RESPONSE_TOPIC, interfaze, applicationEntity);
        DestinationEntity requestQueueDestinationEntity = KafkaMQDestinationUtil.createDestinationEntity(DestinationType.REQUEST_QUEUE, interfaze, applicationEntity);

        initializeServerHandler(responseQueueDestinationEntity, requestQueueDestinationEntity, interfaze, applicationEntity, false);
        initializeServerHandler(responseTopicDestinationEntity, requestQueueDestinationEntity, interfaze, applicationEntity, true);
    }

    private void initializeServerHandler(DestinationEntity responseDestinationEntity, DestinationEntity requestDestinationEntity, String interfaze, ApplicationEntity applicationEntity, boolean topic) throws Exception {
        String responseTopic = responseDestinationEntity.toString();
        String requestTopic = requestDestinationEntity.toString();

        KafkaMQServerHandler serveHandler = new KafkaMQServerHandler(mqPropertyEntity, responseTopic + (topic ? "-" + RandomUtil.uuidRandom() : ""));
        serveHandler.setCacheContainer(cacheContainer);
        serveHandler.setExecutorContainer(executorContainer);
        serveHandler.consume(responseTopic, requestTopic, interfaze, applicationEntity);
    }

    public KafkaMQProducer getProducer() {
        return producer;
    }
}