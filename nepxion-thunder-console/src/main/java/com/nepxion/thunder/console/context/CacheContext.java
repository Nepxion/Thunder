package com.nepxion.thunder.console.context;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;

import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.protocol.redis.cluster.RedisClusterFactory;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSentinelPoolFactory;

public class CacheContext {

    public static void startSentinel() {
        ThunderProperties properties = PropertiesContext.getProperties();
        RedisSentinelPoolFactory.initialize(properties);
    }

    public static void stopSentinel() {
        RedisSentinelPoolFactory.close();
    }
    
    public static boolean sentinelEnabled() {
        return RedisSentinelPoolFactory.enabled();
    }
    
    public static void startCluster() {
        ThunderProperties properties = PropertiesContext.getProperties();
        RedisClusterFactory.initialize(properties);
    }

    public static void stopCluster() {
        try {
            RedisClusterFactory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean clusterEnabled() {
        return RedisClusterFactory.enabled();
    }
    
    public static void stop() {
        stopSentinel();
        stopCluster();
    }
}