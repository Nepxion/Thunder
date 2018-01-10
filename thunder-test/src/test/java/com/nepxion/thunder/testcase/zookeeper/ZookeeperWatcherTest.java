package com.nepxion.thunder.testcase.zookeeper;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.BackgroundPathable;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;
import com.nepxion.thunder.registry.zookeeper.common.watcher.ZookeeperWatcher;
import com.nepxion.thunder.registry.zookeeper.common.watcher.ZookeeperWatcherType;

public class ZookeeperWatcherTest {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperWatcherTest.class);

    private static final String NAMESPACE = "/Neptune";
    private static final String APPLICATION = NAMESPACE + "/application";
    private static final String SERVER = APPLICATION + "/server";
    private static final String SUB_SERVER = SERVER + "/sub-server";

    private static final String ADDRESS = "192.168.126.131:2181,192.168.126.132:2181,192.168.126.135:2181";
    private static final int SESSION_TIMEOUT = 60 * 1000;
    private static final int CONNECTION_TIMEOUT = 15 * 1000;
    private static final int CONNECT_WAIT_TIME = 1000;

    @Test
    public void test() throws Exception {
        ZookeeperInvoker invoker = new ZookeeperInvoker();
        invoker.create(ADDRESS, SESSION_TIMEOUT, CONNECTION_TIMEOUT, CONNECT_WAIT_TIME);
        invoker.startAndBlock(20000, TimeUnit.MILLISECONDS);

        if (!invoker.pathExist(APPLICATION)) {
            invoker.createPath(APPLICATION, CreateMode.PERSISTENT);
        }

        usingWatcher(invoker.getClient(), ZookeeperWatcherType.EXISTS, APPLICATION);
        usingCallbackWatcher(invoker.getClient(), ZookeeperWatcherType.GET_CHILDREN, APPLICATION);
        usingWatcher(invoker.getClient(), ZookeeperWatcherType.GET_DATA, APPLICATION);

        // 创建节点
        if (!invoker.pathExist(SERVER)) {
            LOG.info("创建节点：" + SERVER);
            invoker.createPath(SERVER);
        }

        // 放入节点数据
        if (invoker.pathExist(SERVER)) {
            LOG.info("放入节点数据：" + SERVER);
            invoker.setData(SERVER, new String("server"));
        }

        // 删除节点
        if (invoker.pathExist(SERVER)) {
            LOG.info("删除节点：" + SERVER);
            invoker.deletePath(SERVER);
        }

        // 创建子节点
        if (!invoker.pathExist(SUB_SERVER)) {
            LOG.info("创建子节点：" + SUB_SERVER);
            invoker.createPath(SUB_SERVER);
        }

        // 放入子节点数据
        if (invoker.pathExist(SUB_SERVER)) {
            LOG.info("放入子节点数据：" + SUB_SERVER);
            invoker.setData(SUB_SERVER, new String("sub-server"));
        }

        // 删除子节点
        if (invoker.pathExist(SUB_SERVER)) {
            LOG.info("删除子节点：" + SUB_SERVER);
            invoker.deletePath(SUB_SERVER);
        }

        // invoker.close();

        System.in.read();
    }

    private void usingWatcher(CuratorFramework client, final ZookeeperWatcherType type, String path) throws Exception {
        ZookeeperWatcher watcher = new ZookeeperWatcher(client) {
            @Override
            public void none(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }

            @Override
            public void nodeCreated(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }

            @Override
            public void nodeDeleted(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }

            @Override
            public void nodeDataChanged(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }

            @Override
            public void nodeChildrenChanged(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }
        };
        watcher.usingWatcher(type, path);
    }

    private void usingCallbackWatcher(CuratorFramework client, final ZookeeperWatcherType type, String path) throws Exception {
        final BackgroundCallback callback = new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                LOG.info("Callback path={}", event.getPath());
            }
        };

        ZookeeperWatcher watcher = new ZookeeperWatcher(client) {
            @Override
            public void usingWatcher(BackgroundPathable<?> backgroundPathable, String path) throws Exception {
                backgroundPathable.inBackground(callback).forPath(path);
            }

            @Override
            public void none(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }

            @Override
            public void nodeCreated(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }

            @Override
            public void nodeDeleted(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }

            @Override
            public void nodeDataChanged(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }

            @Override
            public void nodeChildrenChanged(WatchedEvent event) throws Exception {
                LOG.info("Path={}, Watcher Type={}, Event Type={}", event.getPath(), type, event.getType());
            }
        };
        watcher.usingWatcher(type, path);
    }
}