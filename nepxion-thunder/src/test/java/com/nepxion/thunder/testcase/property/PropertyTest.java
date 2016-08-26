package com.nepxion.thunder.testcase.property;

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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.MQEntity;
import com.nepxion.thunder.common.entity.MQPropertyEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;

public class PropertyTest {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyTest.class);

    @Test
    public void testMQEntity() throws Exception {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();

        MQEntity entity = new MQEntity();
        entity.extractProperties(properties, ProtocolType.TIBCO);
        Map<String, ThunderProperties> propertiesMap = entity.getPropertiesMap();
        for (Map.Entry<String, ThunderProperties> entry : propertiesMap.entrySet()) {
            String key = entry.getKey();
            ThunderProperties value = entry.getValue();
            LOG.info("{} : {}", key, value.getMap());
        }
    }

    @Test
    public void testMQPropertyEntity() throws Exception {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        
        MQEntity entity = new MQEntity();
        entity.extractProperties(properties, ProtocolType.TIBCO);
        
        MQPropertyEntity propertyEntity = new MQPropertyEntity("com.nepxion.test", "tibco-1", entity);
        LOG.info("mqUrl : {}", propertyEntity.getString("mqUrl"));
        
        LOG.info("summarizeProperties : {}", propertyEntity.summarizeProperties(ThunderConstants.KAFKA_CONSUMER_ATTRIBUTE_NAME));
    }
}