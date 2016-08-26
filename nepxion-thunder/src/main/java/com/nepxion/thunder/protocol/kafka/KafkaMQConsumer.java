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

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.MQPropertyEntity;

public class KafkaMQConsumer extends ThunderDelegateImpl {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMQConsumer.class);
    
    protected MQPropertyEntity mqPropertyEntity;
    protected Consumer<String, byte[]> consumer;
    
    public KafkaMQConsumer(MQPropertyEntity mqPropertyEntity, String groupId) {
        Map<String, Object> map = null;
        try {
            map = mqPropertyEntity.summarizeProperties(ThunderConstants.KAFKA_CONSUMER_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.error("Extract properties failed", e);
        }
        map.put("group.id", groupId);

        this.mqPropertyEntity = mqPropertyEntity;
        this.consumer = new KafkaConsumer<String, byte[]>(map, new StringDeserializer(), new ByteArrayDeserializer());
    }

    public MQPropertyEntity getMQPropertyEntity() {
        return mqPropertyEntity;
    }
    
    public Consumer<String, byte[]> getConsumer() {
        return consumer;
    }
}