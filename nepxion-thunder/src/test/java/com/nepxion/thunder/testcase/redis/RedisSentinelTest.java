package com.nepxion.thunder.testcase.redis;

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

import redis.clients.jedis.Jedis;

import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.nepxion.thunder.serialization.json.JacksonSerializer;

public class RedisSentinelTest {
    private static final Logger LOG = LoggerFactory.getLogger(RedisSentinelTest.class);

    @Test
    public void test() throws Exception {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        RedisSentinelPoolFactory.initialize(properties);

        Jedis jedis = RedisSentinelPoolFactory.getResource();
        del(jedis);
        hget(jedis);
    }

    protected void del(Jedis jedis) {
        for (int i = 0; i < 200; i++) {
            jedis.del("A1(" + i + ")");
            jedis.del("A2(" + i + ")");
        }
        LOG.info("Clear all data");
    }

    protected void hget(Jedis jedis) {
        Map<String, String> records = jedis.hgetAll("A1(1)");
        for (Map.Entry<String, String> entry : records.entrySet()) {
            MonitorStat monitorStat = JacksonSerializer.fromJson(entry.getValue(), MonitorStat.class);
            System.out.println(monitorStat.getException());
        }
    }

    protected void hset(Jedis jedis) {
        jedis.hset("abc", "1", "aaa");
        jedis.hset("abc", "2", "bbb");
        jedis.hset("abc", "3", "ccc");
        Map<String, String> value = jedis.hgetAll("abc");

        LOG.info("Value={}", value.toString());
    }
}