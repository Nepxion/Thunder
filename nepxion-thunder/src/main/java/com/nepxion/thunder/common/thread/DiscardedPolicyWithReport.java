package com.nepxion.thunder.common.thread;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 任务饱和时以FIFO的方式抛弃队列中一部分现有任务，再添加新任务
public class DiscardedPolicyWithReport implements RejectedExecutionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DiscardedPolicyWithReport.class);
    
    private String threadName;

    public DiscardedPolicyWithReport() {
        this(null);
    }
    
    public DiscardedPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (threadName != null) {
            LOG.error("Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }
        
        if (!executor.isShutdown()) {
            BlockingQueue<Runnable> queue = executor.getQueue();
            // 舍弃1/2队列元素，例如7个单位的元素，舍弃3个
            int discardSize = queue.size() >> 1;
            for (int i = 0; i < discardSize; i++) {
                // 从头部移除并返问队列头部的元素
                queue.poll();
            }
            
            // 添加元素，如果队列满，不阻塞，返回false
            queue.offer(runnable);
        }
    }
}