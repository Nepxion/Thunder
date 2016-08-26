package com.nepxion.thunder.protocol.redis.sentinel;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.common.property.ThunderProperties;

/**
 * 非线程安全类，initialize和close需要在单线程里面执行
 */
public class RedisSentinelPoolFactory {
    private static final Logger LOG = LoggerFactory.getLogger(RedisSentinelPoolFactory.class);

    private static ThunderProperties properties;
    private static JedisSentinelPool sentinelPool;

    public static void initialize(ThunderProperties properties) {
        if (sentinelPool != null) {
            return;
        }

        RedisSentinelPoolFactory.properties = properties;

        String sentinelValue = null;
        try {
            sentinelValue = properties.getString(ThunderConstants.REDIS_SENTINEL_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.warn("Redis sentinel address is null, sentinel pool won't start");

            return;
        }

        if (StringUtils.isEmpty(sentinelValue)) {
            LOG.warn("Redis sentinel address is null, sentinel pool won't start");

            return;
        }

        try {
            Set<String> sentinelSet = new HashSet<String>();
            String[] sentinelArray = StringUtils.split(sentinelValue, ";");
            for (String sentinel : sentinelArray) {
                sentinelSet.add(sentinel.trim());
            }

            sentinelPool = new JedisSentinelPool(properties.getString(ThunderConstants.REDIS_MASTER_NAME_ATTRIBUTE_NAME),
                    sentinelSet,
                    ObjectPoolFactory.createRedisObjectPoolConfig(),
                    properties.getInteger(ThunderConstants.REDIS_CONNECTION_TIMEOUT_ATTRIBUTE_NAME),
                    properties.getInteger(ThunderConstants.REDIS_SO_TIMEOUT_ATTRIBUTE_NAME),
                    StringUtils.isNotEmpty(properties.getString(ThunderConstants.REDIS_PASSWORD_ATTRIBUTE_NAME)) ? properties.getString(ThunderConstants.REDIS_PASSWORD_ATTRIBUTE_NAME) : null,
                    properties.getInteger(ThunderConstants.REDIS_DATABASE_ATTRIBUTE_NAME),
                    StringUtils.isNotEmpty(properties.getString(ThunderConstants.REDIS_CLIENT_NAME_ATTRIBUTE_NAME)) ? properties.getString(ThunderConstants.REDIS_CLIENT_NAME_ATTRIBUTE_NAME) : null);
            LOG.info("Redis sentinel is initialized...");
        } catch (Exception e) {
            LOG.error("Redis sentinel pool is initialized failed", e);
        }
    }

    public static ThunderProperties getProperties() {
        return properties;
    }

    public static Jedis getResource() {
        if (sentinelPool == null) {
            return null;
        }

        return sentinelPool.getResource();
    }

    public static void close() {
        if (sentinelPool != null) {
            sentinelPool.close();
            sentinelPool = null;
        }
    }

    public static boolean enabled() {
        return sentinelPool != null && !sentinelPool.isClosed();
    }
}