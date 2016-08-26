package com.nepxion.thunder.common.property;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

public abstract class ThunderPropertiesEntity implements Serializable {
    private static final long serialVersionUID = 5403317977587754742L;
    
    protected ThunderProperties properties;

    public ThunderPropertiesEntity(ThunderProperties properties) {
        this.properties = properties;
    }

    public ThunderProperties getProperties() {
        return properties;
    }

    // 在局部变量找不到相关值的时候，就取全局变量值
    // 例如：
    // 当全局变量配置为 kafka.producer.send.buffer.bytes = 20
    // 局部变量配置应为 kafka-1.kafka.producer.send.buffer.bytes = 20
    public String getString(String key) throws Exception {
        ThunderProperties subProperties = getSubProperties();
        try {
            return subProperties.getString(key);
        } catch (Exception e) {
            return properties.getString(key);
        }
    }

    public int getInteger(String key) throws Exception {
        ThunderProperties subProperties = getSubProperties();
        try {
            return subProperties.getInteger(key);
        } catch (Exception e) {
            return properties.getInteger(key);
        }
    }

    public long getLong(String key) throws Exception {
        ThunderProperties subProperties = getSubProperties();
        try {
            return subProperties.getLong(key);
        } catch (Exception e) {
            return properties.getLong(key);
        }
    }

    public boolean getBoolean(String key) throws Exception {
        ThunderProperties subProperties = getSubProperties();
        try {
            return subProperties.getBoolean(key);
        } catch (Exception e) {
            return properties.getBoolean(key);
        }
    }

    public Map<String, Object> summarizeProperties(String prefix) throws Exception {
        Map<String, Object> map = Maps.newConcurrentMap();

        // 从全局变量中归类
        summarizeProperties(properties, map, prefix.endsWith(ThunderProperties.DOT) ? prefix : prefix + ThunderProperties.DOT);

        // 从局部变量中归类，如果全局变量和局部变量都存在某个属性，那么就使用局部变量，否则使用全部变量
        ThunderProperties subProperties = getSubProperties();
        summarizeProperties(subProperties, map, prefix.endsWith(ThunderProperties.DOT) ? prefix : prefix + ThunderProperties.DOT);

        return map;
    }

    private void summarizeProperties(ThunderProperties properties, Map<String, Object> map, String prefix) {
        for (Map.Entry<String, Object> entry : properties.getMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.startsWith(prefix)) {
                int index = key.indexOf(prefix);
                String name = key.substring(index + prefix.length());
                map.put(name, value.toString());
            }
        }
    }

    public abstract ThunderProperties getSubProperties() throws Exception;
}