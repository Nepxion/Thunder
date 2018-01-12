package com.nepxion.thunder.testcase.tool;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.util.RandomUtil;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperTest {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperTest.class);

    private static final String ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 60 * 1000;
    private static final int CONNECT_TIMEOUT = 15 * 1000;
    private static final int CONNECT_WAIT_TIME = 1000;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    @Test
    public void testCreate() throws Exception {
        ZookeeperInvoker invoker = new ZookeeperInvoker();
        invoker.create(ADDRESS, SESSION_TIMEOUT, CONNECT_TIMEOUT, CONNECT_WAIT_TIME);
        invoker.startAndBlock(20000, TimeUnit.MILLISECONDS);

        String path1 = "/thunder/application/netty/MY_GROUP/APP-IOS/service/com.nepxion.thunder.test.service.UserService/{\"application\":\"APP-IOS\",\"group\":\"MY_GROUP\",\"cluster\":\"NettyServerCluster\",\"host\":\"10.11.106.121\",\"port\":6010}";
        create(invoker, path1);

        String path2 = "/thunder/application/netty/MY_GROUP/APP-IOS/service/com.nepxion.thunder.test.service.AnimalService/{\"application\":\"APP-IOS\",\"group\":\"MY_GROUP\",\"cluster\":\"NettyServerCluster\",\"host\":\"10.11.106.121\",\"port\":6010}";
        create(invoker, path2);

        // invoker.close(client);

        System.in.read();
    }

    private void create(final ZookeeperInvoker invoker, final String path) throws Exception {
        for (int i = 0; i < 5000; i++) {
            LOG.info("发送事件={}", i);
            EXECUTOR.execute(new Runnable() {
                public void run() {
                    try {
                        String fullPath = path + RandomUtil.uuidRandom();
                        if (invoker.pathExist(fullPath)) {
                            invoker.deletePath(fullPath);
                        }
                        invoker.createPath(fullPath, CreateMode.EPHEMERAL);
                        // TimeUnit.MILLISECONDS.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Test
    public void testDelete() throws Exception {
        ZookeeperInvoker invoker = new ZookeeperInvoker();
        invoker.create(ADDRESS, SESSION_TIMEOUT, CONNECT_TIMEOUT, CONNECT_WAIT_TIME);
        invoker.startAndBlock(20000, TimeUnit.MILLISECONDS);

        invoker.deletePath("/thunder");

        // invoker.close();

        System.in.read();
    }
}