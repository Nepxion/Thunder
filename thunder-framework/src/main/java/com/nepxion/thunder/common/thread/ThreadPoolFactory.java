package com.nepxion.thunder.common.thread;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.ThreadQueueType;
import com.nepxion.thunder.common.entity.ThreadRejectedPolicyType;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.util.ClassUtil;
import com.nepxion.thunder.common.util.StringUtil;

public class ThreadPoolFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadPoolFactory.class);

    private static ThunderProperties properties;

    private static ConcurrentMap<String, ThreadPoolExecutor> threadPoolServerExecutorMap = Maps.newConcurrentMap();
    private static ConcurrentMap<String, ThreadPoolExecutor> threadPoolClientExecutorMap = Maps.newConcurrentMap();

    public static void initialize(ThunderProperties properties) {
        ThreadPoolFactory.properties = properties;
    }

    public static ThreadPoolExecutor createThreadPoolDefaultExecutor(String url, String interfaze) {
        return createThreadPoolExecutor(url, interfaze,
                ThunderConstant.CPUS * 1,
                ThunderConstant.CPUS * 2,
                15 * 60 * 1000,
                false);
    }

    public static ThreadPoolExecutor createThreadPoolDefaultExecutor() {
        return createThreadPoolExecutor(ThunderConstant.CPUS * 1,
                ThunderConstant.CPUS * 2,
                15 * 60 * 1000,
                false);
    }

    public static ThreadPoolExecutor createThreadPoolServerExecutor(String url, String interfaze) {
        try {
            return createThreadPoolExecutor(threadPoolServerExecutorMap, url,
                    properties.getBoolean(ThunderConstant.THREAD_POOL_MULTI_MODE_ATTRIBUTE_NAME) ? interfaze : properties.getString(ThunderConstant.NAMESPACE_ELEMENT_NAME) + "-" + ApplicationType.SERVICE,
                    ThunderConstant.CPUS * properties.getInteger(ThunderConstant.THREAD_POOL_SERVER_CORE_POOL_SIZE_ATTRIBUTE_NAME),
                    ThunderConstant.CPUS * properties.getInteger(ThunderConstant.THREAD_POOL_SERVER_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME),
                    properties.getLong(ThunderConstant.THREAD_POOL_SERVER_KEEP_ALIVE_TIME_ATTRIBUTE_NAME),
                    properties.getBoolean(ThunderConstant.THREAD_POOL_SERVER_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME));
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized", e);
        }
    }

    public static ThreadPoolExecutor createThreadPoolClientExecutor(String url, String interfaze) {
        try {
            return createThreadPoolExecutor(threadPoolClientExecutorMap, url,
                    properties.getBoolean(ThunderConstant.THREAD_POOL_MULTI_MODE_ATTRIBUTE_NAME) ? interfaze : properties.getString(ThunderConstant.NAMESPACE_ELEMENT_NAME) + "-" + ApplicationType.REFERENCE,
                    ThunderConstant.CPUS * properties.getInteger(ThunderConstant.THREAD_POOL_CLIENT_CORE_POOL_SIZE_ATTRIBUTE_NAME),
                    ThunderConstant.CPUS * properties.getInteger(ThunderConstant.THREAD_POOL_CLIENT_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME),
                    properties.getLong(ThunderConstant.THREAD_POOL_CLIENT_KEEP_ALIVE_TIME_ATTRIBUTE_NAME),
                    properties.getBoolean(ThunderConstant.THREAD_POOL_CLIENT_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME));
        } catch (Exception e) {
            throw new IllegalArgumentException("Properties maybe isn't initialized");
        }
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(final ConcurrentMap<String, ThreadPoolExecutor> threadPoolExecutorMap, final String url, final String interfaze, final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final boolean allowCoreThreadTimeOut) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(interfaze);
        if (threadPoolExecutor == null) {
            ThreadPoolExecutor newThreadPoolExecutor = createThreadPoolExecutor(url, interfaze, corePoolSize, maximumPoolSize, keepAliveTime, allowCoreThreadTimeOut);
            threadPoolExecutor = threadPoolExecutorMap.putIfAbsent(interfaze, newThreadPoolExecutor);
            if (threadPoolExecutor == null) {
                threadPoolExecutor = newThreadPoolExecutor;
            }
        }

        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(final String url, final String interfaze, final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final boolean allowCoreThreadTimeOut) {
        final String threadName = StringUtil.firstLetterToUpper(ClassUtil.convertBeanName(interfaze)) + "-" + (StringUtils.isNotEmpty(url) ? url + "-" : "") + "thread";

        LOG.info("Thread pool executor is created, threadName={}, corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, allowCoreThreadTimeOut={}", threadName, corePoolSize, maximumPoolSize, keepAliveTime, allowCoreThreadTimeOut);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                createBlockingQueue(),
                new ThreadFactory() {
                    private AtomicInteger number = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, threadName + "-" + number.getAndIncrement());
                    }
                },
                createRejectedPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);

        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, boolean allowCoreThreadTimeOut) {
        LOG.info("Thread pool executor is created, corePoolSize={}, maximumPoolSize={}, keepAliveTime={}, allowCoreThreadTimeOut={}", corePoolSize, maximumPoolSize, keepAliveTime, allowCoreThreadTimeOut);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                createBlockingQueue(),
                createRejectedPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);

        return threadPoolExecutor;
    }

    private static BlockingQueue<Runnable> createBlockingQueue() {
        String queue = properties.getString(ThunderConstant.THREAD_POOL_QUEUE_ATTRIBUTE_NAME);
        ThreadQueueType queueType = ThreadQueueType.fromString(queue);

        int queueCapacity = ThunderConstant.CPUS * properties.getInteger(ThunderConstant.THREAD_POOL_QUEUE_CAPACITY_ATTRIBUTE_NAME);

        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<Runnable>(queueCapacity);
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(queueCapacity);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<Runnable>();
        }

        return null;
    }

    private static RejectedExecutionHandler createRejectedPolicy() {
        String rejectedPolicy = properties.getString(ThunderConstant.THREAD_POOL_REJECTED_POLICY_ATTRIBUTE_NAME);
        ThreadRejectedPolicyType rejectedPolicyType = ThreadRejectedPolicyType.fromString(rejectedPolicy);

        switch (rejectedPolicyType) {
            case BLOCKING_POLICY_WITH_REPORT:
                return new BlockingPolicyWithReport();
            case CALLER_RUNS_POLICY_WITH_REPORT:
                return new CallerRunsPolicyWithReport();
            case ABORT_POLICY_WITH_REPORT:
                return new AbortPolicyWithReport();
            case REJECTED_POLICY_WITH_REPORT:
                return new RejectedPolicyWithReport();
            case DISCARDED_POLICY_WITH_REPORT:
                return new DiscardedPolicyWithReport();
        }

        return null;
    }
}