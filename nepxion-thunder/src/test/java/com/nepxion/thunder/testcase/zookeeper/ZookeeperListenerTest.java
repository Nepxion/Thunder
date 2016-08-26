package com.nepxion.thunder.testcase.zookeeper;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;
import com.nepxion.thunder.registry.zookeeper.common.listener.ZookeeperNodeCacheListener;
import com.nepxion.thunder.registry.zookeeper.common.listener.ZookeeperPathChildrenCacheListener;
import com.nepxion.thunder.registry.zookeeper.common.listener.ZookeeperTreeCacheListener;

public class ZookeeperListenerTest {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperListenerTest.class);
    
    private static final String NAMESPACE = "/Neptune";
    private static final String APPLICATION = NAMESPACE + "/application";
    private static final String SERVER = APPLICATION + "/server";
    private static final String SUB_SERVER = SERVER + "/sub-server";
    
    private static final String ADDRESS = "192.168.126.131:2181,192.168.126.132:2181,192.168.126.135:2181";
    private static final int SESSION_TIMEOUT = 60 * 1000;
    private static final int CONNECT_TIMEOUT = 15 * 1000;
    private static final int CONNECT_WAIT_TIME = 1000;
    
    @Test
    public void test() throws Exception {
        ZookeeperInvoker invoker = new ZookeeperInvoker();
        CuratorFramework client = invoker.create(ADDRESS, SESSION_TIMEOUT, CONNECT_TIMEOUT, CONNECT_WAIT_TIME);
        invoker.startAndBlock(client, 20000, TimeUnit.MILLISECONDS);
        
        if (!invoker.pathExist(client, APPLICATION)) {
            invoker.createPath(client, APPLICATION, CreateMode.PERSISTENT);
        }
        
        usingNodeCacheListener(client, SERVER);
        usingPathChildrenListener(client, SERVER);
        usingTreeCacheListener(client, SERVER);
        
        // 创建节点
        if (!invoker.pathExist(client, SERVER)) {
            LOG.info("创建节点：" + SERVER);
            invoker.createPath(client, SERVER);
        }
        
        // 放入节点数据
        if (invoker.pathExist(client, SERVER)) {
            LOG.info("放入节点数据：" + SERVER);
            invoker.setData(client, SERVER, new String("server"));
        }
        
        // 删除节点
        if (invoker.pathExist(client, SERVER)) {
            LOG.info("删除节点：" + SERVER);
            invoker.deletePath(client, SERVER);
        }
        
        // 创建子节点
        if (!invoker.pathExist(client, SUB_SERVER)) {
            LOG.info("创建子节点：" + SUB_SERVER);
            invoker.createPath(client, SUB_SERVER);
        }
        
        // 放入子节点数据
        if (invoker.pathExist(client, SUB_SERVER)) {
            LOG.info("放入子节点数据：" + SUB_SERVER);
            invoker.setData(client, SUB_SERVER, new String("sub-server"));
        }
        
        // 删除子节点
        if (invoker.pathExist(client, SUB_SERVER)) {
            LOG.info("删除子节点：" + SUB_SERVER);
            invoker.deletePath(client, SUB_SERVER);
        }
        
        //executor.close(client);
        
        System.in.read();
    }
    
    private void usingNodeCacheListener(CuratorFramework client, String path) throws Exception {   
        ZookeeperNodeCacheListener listener = new ZookeeperNodeCacheListener(client, path) {
            @Override
            public void nodeChanged() throws Exception {
                ChildData data = nodeCache.getCurrentData();
                if (data == null) {
                    LOG.info("Node changed for null");
                } else {
                    LOG.info("Node changed for Path={}", data.getPath());
                }
            }           
        };
        listener.getNodeCache().close();
    }
    
    private void usingPathChildrenListener(CuratorFramework client, String path) throws Exception {
        ZookeeperPathChildrenCacheListener listener = new ZookeeperPathChildrenCacheListener(client, path) {
            @Override
            public void initialized(PathChildrenCacheEvent event) throws Exception {                
                LOG.info("Path={}, Event Type={}, Initial Data={}", event.getData().getPath(), event.getType(), event.getInitialData());
            }

            @Override
            public void childAdded(PathChildrenCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}, Initial Data={}", event.getData().getPath(), event.getType(), event.getInitialData());
            }

            @Override
            public void childUpdated(PathChildrenCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}, Initial Data={}", event.getData().getPath(), event.getType(), event.getInitialData());
            }

            @Override
            public void childRemoved(PathChildrenCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}, Initial Data={}", event.getData().getPath(), event.getType(), event.getInitialData());
            }

            @Override
            public void connectionSuspended(PathChildrenCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}, Initial Data={}", event.getData().getPath(), event.getType(), event.getInitialData());
            }

            @Override
            public void connectionReconnected(PathChildrenCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}, Initial Data={}", event.getData().getPath(), event.getType(), event.getInitialData());
            }

            @Override
            public void connectionLost(PathChildrenCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}, Initial Data={}", event.getData().getPath(), event.getType(), event.getInitialData());
            }
        };
        listener.getPathChildrenCache().close();
    }
    
    private void usingTreeCacheListener(CuratorFramework client, String path) throws Exception {        
        ZookeeperTreeCacheListener listener = new ZookeeperTreeCacheListener(client, path) {
            @Override
            public void initialized(TreeCacheEvent event) throws Exception {                
                //LOG.info("Path={}, Event Type={}", event.getData().getPath(), event.getType());
            }

            @Override
            public void nodeAdded(TreeCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}", event.getData().getPath(), event.getType());
            }

            @Override
            public void nodeUpdated(TreeCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}", event.getData().getPath(), event.getType());
            }

            @Override
            public void nodeRemoved(TreeCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}", event.getData().getPath(), event.getType());
            }

            @Override
            public void connectionSuspended(TreeCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}", event.getData().getPath(), event.getType());
            }

            @Override
            public void connectionReconnected(TreeCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}", event.getData().getPath(), event.getType());
            }

            @Override
            public void connectionLost(TreeCacheEvent event) throws Exception {
                LOG.info("Path={}, Event Type={}", event.getData().getPath(), event.getType());
            }
        };
        listener.getTreeCache().close();
    }
}