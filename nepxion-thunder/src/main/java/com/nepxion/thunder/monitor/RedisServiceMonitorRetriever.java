package com.nepxion.thunder.monitor;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import com.nepxion.thunder.common.entity.RedisType;
import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.protocol.redis.cluster.RedisClusterFactory;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSentinelPoolFactory;

public class RedisServiceMonitorRetriever extends AbstractMonitorRetriever {
    
    public List<MonitorStat> retrieve(String traceId, RedisType redisType) throws Exception {
        if (StringUtils.isEmpty(traceId)) {
            throw new MonitorException("Trace ID is null");
        }
        
        if (redisType == null) {
            throw new MonitorException("Redis type is null");
        }
        
        Map<String, String> monitorStatMap = null;
        switch (redisType) {
            case REDIS_SENTINEL:
                monitorStatMap = retrieveFromSentinel(traceId);
                break;
            case REDIS_CLUSTER:
                monitorStatMap = retrieveFromCluster(traceId);
                break;
        }

        if (MapUtils.isEmpty(monitorStatMap)) {
            return null;
        }

        List<MonitorStat> monitorStatList = retrieve(monitorStatMap);

        sort(monitorStatList);

        return monitorStatList;
    }

    public Map<String, String> retrieveFromSentinel(String traceId) throws Exception {
        Jedis jedis = RedisSentinelPoolFactory.getResource();
        if (jedis == null) {
            throw new MonitorException("No redis sentinel resource found, retrieve failed");
        }

        Map<String, String> monitorStatMap = jedis.hgetAll(traceId);
        if (MapUtils.isEmpty(monitorStatMap)) {
            return null;
        }

        return monitorStatMap;
    }

    public Map<String, String> retrieveFromCluster(String traceId) throws Exception {
        JedisCluster cluster = RedisClusterFactory.getCluster();
        if (cluster == null) {
            throw new MonitorException("No redis cluster found, retrieve failed");
        }

        Map<String, String> monitorStatMap = cluster.hgetAll(traceId);
        if (MapUtils.isEmpty(monitorStatMap)) {
            return null;
        }

        return monitorStatMap;
    }

    public List<MonitorStat> retrieve(Map<String, String> monitorStatMap) {
        List<MonitorStat> monitorStatList = new ArrayList<MonitorStat>();
        for (Map.Entry<String, String> entry : monitorStatMap.entrySet()) {
            MonitorStat monitorStat = create(entry.getValue());
            monitorStatList.add(monitorStat);
        }

        return monitorStatList;
    }

    public List<MonitorStat> retrieve(String[] monitorStatArray) {
        List<MonitorStat> monitorStatList = new ArrayList<MonitorStat>();
        for (String monitorStatJson : monitorStatArray) {
            MonitorStat monitorStat = create(monitorStatJson);
            monitorStatList.add(monitorStat);
        }

        return monitorStatList;
    }
}