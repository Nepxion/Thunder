package com.nepxion.thunder.testcase.redis;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;

import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;
import com.nepxion.thunder.protocol.redis.cluster.RedisClusterFactory;
import com.nepxion.thunder.serialization.json.JacksonSerializer;

public class RedisClusterTest {
    private static final Logger LOG = LoggerFactory.getLogger(RedisClusterTest.class);

    @Test
    public void test() throws Exception {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        RedisClusterFactory.initialize(properties);

        JedisCluster cluster = RedisClusterFactory.getCluster();
        del(cluster);
        hget(cluster);
    }

    protected void del(JedisCluster cluster) {
        for (int i = 0; i < 200; i++) {
            cluster.del("A1(" + i + ")");
            cluster.del("A2(" + i + ")");
        }
        LOG.info("Clear all data");
    }

    protected void hget(JedisCluster cluster) {
        Map<String, String> records = cluster.hgetAll("A1(1)");
        for (Map.Entry<String, String> entry : records.entrySet()) {
            MonitorStat monitorStat = JacksonSerializer.fromJson(entry.getValue(), MonitorStat.class);
            System.out.println(monitorStat.getException());
        }
    }

    protected void hset(JedisCluster cluster) {
        cluster.hset("abc", "1", "aaa");
        cluster.hset("abc", "2", "bbb");
        cluster.hset("abc", "3", "ccc");
        Map<String, String> value = cluster.hgetAll("abc");

        LOG.info("Value={}", value.toString());
    }
}