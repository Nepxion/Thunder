package com.nepxion.thunder.console.controller;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;
import java.util.Map;

import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.common.entity.RedisType;
import com.nepxion.thunder.monitor.LogServiceMonitorRetriever;
import com.nepxion.thunder.monitor.RedisServiceMonitorRetriever;
import com.nepxion.thunder.monitor.SplunkLogServiceMonitorRetriever;

public class MonitorController {
    private static SplunkLogServiceMonitorRetriever splunkLogServiceMonitorRetriever = new SplunkLogServiceMonitorRetriever();
    private static LogServiceMonitorRetriever logServiceMonitorRetriever = new LogServiceMonitorRetriever();
    private static RedisServiceMonitorRetriever redisServiceMonitorRetriever = new RedisServiceMonitorRetriever();

    public static SplunkLogServiceMonitorRetriever getSplunkLogServiceMonitorRetriever() {
        return splunkLogServiceMonitorRetriever;
    }

    public static LogServiceMonitorRetriever getLogServiceMonitorRetriever() {
        return logServiceMonitorRetriever;
    }

    public static RedisServiceMonitorRetriever getRedisServiceMonitorRetriever() {
        return redisServiceMonitorRetriever;
    }

    public static List<MonitorStat> retrieveFromSplunk(String traceId, Map<String, Object> conditions) throws Exception {
        return splunkLogServiceMonitorRetriever.retrieve(traceId, conditions);
    }

    public static List<MonitorStat> retrieveFromLog(String traceId, String filePath) throws Exception {
        return logServiceMonitorRetriever.retrieve(traceId, filePath);
    }

    public static List<MonitorStat> retrieveFromRedis(String traceId, RedisType redisType) throws Exception {
        return redisServiceMonitorRetriever.retrieve(traceId, redisType);
    }
}