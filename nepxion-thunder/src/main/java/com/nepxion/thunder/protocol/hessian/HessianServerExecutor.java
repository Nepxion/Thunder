package com.nepxion.thunder.protocol.hessian;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.atomic.AtomicBoolean;

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.protocol.AbstractServerExecutor;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSubscriber;

public class HessianServerExecutor extends AbstractServerExecutor {
    private AtomicBoolean start = new AtomicBoolean(false);
    
    @Override
    public void start(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        boolean redisEnabled = RedisSentinelPoolFactory.enabled();
        if (redisEnabled) {
            RedisSubscriber subscriber = new RedisSubscriber(executorContainer);
            subscriber.subscribe(interfaze, applicationEntity);
        }
        
        boolean started = started(interfaze, applicationEntity);
        if (started) {
            return;
        }
        
        start.set(true);
    }

    @Override
    public boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        return start.get();
    }
}