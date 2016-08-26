package com.nepxion.thunder.common.entity;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public enum DestinationType {
    // JMS类型的MQ
    REQUEST_QUEUE_ASYNC("request-queue-async"),
    REQUEST_QUEUE_SYNC("request-queue-sync"),
    RESPONSE_QUEUE_ASYNC("response-queue-async"),
    RESPONSE_QUEUE_SYNC("response-queue-sync"),
    RESPONSE_TOPIC_ASYNC("response-topic-async"),
    
    // 非JMS类型的MQ，例如Kafka
    REQUEST_QUEUE("request-queue"),
    RESPONSE_QUEUE("response-queue"),
    RESPONSE_TOPIC("response-topic");

    private String value;

    private DestinationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static DestinationType fromString(String value) {
        for (DestinationType type : DestinationType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}