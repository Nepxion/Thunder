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

import com.google.common.collect.Maps;

public class MQCacheContainer {
    // 缓存Service所有JMS MQ的服务器配置，key为server(MQ配置名)，例如activeMQ-1
    private static Map<String, MQContext> serviceContextMap = Maps.newConcurrentMap();

    // 缓存Reference所有JMS MQ的服务器配置，key为server(MQ配置名)，例如activeMQ-1
    private static Map<String, MQContext> referenceContextMap = Maps.newConcurrentMap();

    // 缓存队列实体
    private static MQQueueDestinationContainer mqQueueContainer = new MQQueueDestinationContainer();

    // 缓存订阅实体
    private static MQTopicDestinationContainer mqTopicContainer = new MQTopicDestinationContainer();

    public static Map<String, MQContext> getServiceContextMap() {
        return serviceContextMap;
    }

    public static Map<String, MQContext> getReferenceContextMap() {
        return referenceContextMap;
    }

    public static MQQueueDestinationContainer getMQQueueDestinationContainer() {
        return mqQueueContainer;
    }

    public static MQTopicDestinationContainer getMQTopicDestinationContainer() {
        return mqTopicContainer;
    }
}