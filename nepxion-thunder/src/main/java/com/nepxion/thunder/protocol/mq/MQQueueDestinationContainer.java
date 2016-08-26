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

import com.google.common.collect.Maps;

public class MQQueueDestinationContainer extends MQTopicDestinationContainer {
    // 缓存所有的同步请求Destination，对应为非持久化容器(Queue)，key为interface
    private Map<String, Destination> syncRequestDestinationMap = Maps.newConcurrentMap();

    // 缓存所有的同步响应Destination，对应为非持久化容器(Queue)，key为interface
    private Map<String, Destination> syncResponseDestinationMap = Maps.newConcurrentMap();
    
    // 缓存所有的异步请求Destination，对应为持久化容器(Queue)，key为interface
    private Map<String, Destination> asyncRequestDestinationMap = Maps.newConcurrentMap();
    
    // 缓存所有的异步响应Destination，对应为持久化容器(Queue)，key为interface
    private Map<String, Destination> asyncResponseDestinationMap = Maps.newConcurrentMap();

    public Map<String, Destination> getSyncRequestDestinationMap() {
        return syncRequestDestinationMap;
    }

    public Map<String, Destination> getSyncResponseDestinationMap() {
        return syncResponseDestinationMap;
    }

    public Map<String, Destination> getAsyncRequestDestinationMap() {
        return asyncRequestDestinationMap;
    }

    public Map<String, Destination> getAsyncResponseDestinationMap() {
        return asyncResponseDestinationMap;
    }
}