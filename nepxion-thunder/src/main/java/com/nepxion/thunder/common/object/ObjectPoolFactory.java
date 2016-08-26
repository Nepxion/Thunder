package com.nepxion.thunder.common.object;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.property.ThunderProperties;

public class ObjectPoolFactory {
    private static ThunderProperties properties;
    
    public static void initialize(ThunderProperties properties) {
        ObjectPoolFactory.properties = properties;
    }
    
    public static GenericObjectPoolConfig createFSTObjectPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        try {
            config.setMaxTotal(ThunderConstants.CPUS * properties.getInteger(ThunderConstants.FST_OBJECT_POOL_MAX_TOTAL_ATTRIBUTE_NAME));
            config.setMaxIdle(ThunderConstants.CPUS * properties.getInteger(ThunderConstants.FST_OBJECT_POOL_MAX_IDLE_ATTRIBUTE_NAME));
            config.setMinIdle(ThunderConstants.CPUS * properties.getInteger(ThunderConstants.FST_OBJECT_POOL_MIN_IDLE_ATTRIBUTE_NAME));
            config.setMaxWaitMillis(properties.getLong(ThunderConstants.FST_OBJECT_POOL_MAX_WAIT_MILLIS_ATTRIBUTE_NAME));
            config.setTimeBetweenEvictionRunsMillis(properties.getLong(ThunderConstants.FST_OBJECT_POOL_TIME_BETWEEN_EVICTION_RUN_MILLIS_ATTRIBUTE_NAME));
            config.setMinEvictableIdleTimeMillis(properties.getLong(ThunderConstants.FST_OBJECT_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME));
            config.setSoftMinEvictableIdleTimeMillis(properties.getLong(ThunderConstants.FST_OBJECT_POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME));
            config.setBlockWhenExhausted(properties.getBoolean(ThunderConstants.FST_OBJECT_POOL_BLOCK_WHEN_EXHAUSTED_ATTRIBUTE_NAME));
            config.setLifo(properties.getBoolean(ThunderConstants.FST_OBJECT_POOL_LIFO_ATTRIBUTE_NAME));
            config.setFairness(properties.getBoolean(ThunderConstants.FST_OBJECT_POOL_FAIRNESS_ATTRIBUTE_NAME));
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);
            config.setTestOnCreate(false);
            config.setTestWhileIdle(false);
            config.setNumTestsPerEvictionRun(-1);
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized");
        }

        return config;
    }
    
    public static GenericObjectPoolConfig createRedisObjectPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        try {
            config.setMaxTotal(ThunderConstants.CPUS * properties.getInteger(ThunderConstants.REDIS_OBJECT_POOL_MAX_TOTAL_ATTRIBUTE_NAME));
            config.setMaxIdle(ThunderConstants.CPUS * properties.getInteger(ThunderConstants.REDIS_OBJECT_POOL_MAX_IDLE_ATTRIBUTE_NAME));
            config.setMinIdle(ThunderConstants.CPUS * properties.getInteger(ThunderConstants.REDIS_OBJECT_POOL_MIN_IDLE_ATTRIBUTE_NAME));
            config.setMaxWaitMillis(properties.getLong(ThunderConstants.REDIS_OBJECT_POOL_MAX_WAIT_MILLIS_ATTRIBUTE_NAME));
            config.setTimeBetweenEvictionRunsMillis(properties.getLong(ThunderConstants.REDIS_OBJECT_POOL_TIME_BETWEEN_EVICTION_RUN_MILLIS_ATTRIBUTE_NAME));
            config.setMinEvictableIdleTimeMillis(properties.getLong(ThunderConstants.REDIS_OBJECT_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME));
            config.setSoftMinEvictableIdleTimeMillis(properties.getLong(ThunderConstants.REDIS_OBJECT_POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME));
            config.setBlockWhenExhausted(properties.getBoolean(ThunderConstants.REDIS_OBJECT_POOL_BLOCK_WHEN_EXHAUSTED_ATTRIBUTE_NAME));
            config.setLifo(properties.getBoolean(ThunderConstants.REDIS_OBJECT_POOL_LIFO_ATTRIBUTE_NAME));
            config.setFairness(properties.getBoolean(ThunderConstants.REDIS_OBJECT_POOL_FAIRNESS_ATTRIBUTE_NAME));
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);
            config.setTestOnCreate(false);
            config.setTestWhileIdle(false);
            config.setNumTestsPerEvictionRun(-1);
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized");
        }

        return config;
    }
}