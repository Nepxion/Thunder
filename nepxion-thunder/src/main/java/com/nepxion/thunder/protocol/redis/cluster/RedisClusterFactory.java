package com.nepxion.thunder.protocol.redis.cluster;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.common.property.ThunderProperties;

/**
 * 非线程安全类，initialize和close需要在单线程里面执行
 */
public class RedisClusterFactory {
    private static final Logger LOG = LoggerFactory.getLogger(RedisClusterFactory.class);

    private static ThunderProperties properties;
    private static JedisCluster cluster;

    public static void initialize(ThunderProperties properties) {
        RedisClusterFactory.properties = properties;

        String clusterValue = null;
        try {
            clusterValue = properties.getString(ThunderConstants.REDIS_CLUSTER_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.warn("Redis cluster address is null, redis won't start");

            return;
        }

        if (StringUtils.isEmpty(clusterValue)) {
            LOG.warn("Redis cluster address is null, redis won't start");

            return;
        }

        try {
            HashSet<HostAndPort> clusterSet = new HashSet<HostAndPort>();
            String[] clusterArray = StringUtils.split(clusterValue, ";");
            for (String cluster : clusterArray) {
                String[] info = StringUtils.split(cluster, ":");
                clusterSet.add(new HostAndPort(info[0].trim(), Integer.valueOf(info[1].trim())));
            }

            cluster = new JedisCluster(clusterSet,
                    properties.getInteger(ThunderConstants.REDIS_CONNECTION_TIMEOUT_ATTRIBUTE_NAME),
                    properties.getInteger(ThunderConstants.REDIS_SO_TIMEOUT_ATTRIBUTE_NAME),
                    properties.getInteger(ThunderConstants.REDIS_MAX_REDIRECTIONS_ATTRIBUTE_NAME),
                    ObjectPoolFactory.createRedisObjectPoolConfig());
            LOG.info("Redis cluster is initialized...");
        } catch (Exception e) {
            LOG.error("Redis cluster is initialized failed", e);
        }
    }

    public static ThunderProperties getProperties() {
        return properties;
    }

    public static JedisCluster getCluster() {
        return cluster;
    }

    public static void close() throws IOException {
        if (cluster != null) {
            cluster.close();
            cluster = null;
        }
    }

    public static boolean enabled() {
        return cluster != null;
    }
}