package com.nepxion.thunder.monitor;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;
import com.nepxion.thunder.common.util.RandomUtil;
import com.nepxion.thunder.protocol.redis.cluster.RedisClusterFactory;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.nepxion.thunder.serialization.SerializerExecutor;

public class RedisServiceMonitorExecutor extends AbstractMonitorExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(RedisServiceMonitorExecutor.class);

    @Override
    public void execute(final MonitorStat monitorStat) throws Exception {
        final String traceId = monitorStat.getTraceId();
        if (StringUtils.isEmpty(traceId)) {
            LOG.error("Trace ID is null, monitor stat can't be put into redis");

            return;
        }

        if (RedisSentinelPoolFactory.enabled()) {
            executeToSentinel(traceId, monitorStat);
        } else if (RedisClusterFactory.enabled()) {
            executeToCluster(traceId, monitorStat);
        }
    }

    public void executeToSentinel(final String traceId, final MonitorStat monitorStat) throws Exception {
        final Jedis jedis = RedisSentinelPoolFactory.getResource();
        if (jedis == null) {
            LOG.error("No redis sentinel resource found, execute failed");

            return;
        }

        ThreadPoolFactory.createThreadPoolDefaultExecutor().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    jedis.hset(traceId, RandomUtil.uuidRandom(), SerializerExecutor.toJson(monitorStat));
                    jedis.pexpire(traceId, properties.getLong(ThunderConstant.REDIS_DATA_EXPIRATION_ATTRIBUTE_NAME));
                } catch (Exception e) {
                    LOG.error("Execute failed", e);
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }

                return null;
            }
        });
    }

    public void executeToCluster(final String traceId, final MonitorStat monitorStat) throws Exception {
        final JedisCluster cluster = RedisClusterFactory.getCluster();
        if (cluster == null) {
            LOG.error("No redis cluster found, execute failed");

            return;
        }

        ThreadPoolFactory.createThreadPoolDefaultExecutor().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    cluster.hset(traceId, RandomUtil.uuidRandom(), SerializerExecutor.toJson(monitorStat));
                    cluster.pexpire(traceId, properties.getLong(ThunderConstant.REDIS_DATA_EXPIRATION_ATTRIBUTE_NAME));
                } catch (Exception e) {
                    LOG.error("Execute failed", e);
                } finally {
                    if (cluster != null) {
                        cluster.close();
                    }
                }

                return null;
            }
        });
    }
}