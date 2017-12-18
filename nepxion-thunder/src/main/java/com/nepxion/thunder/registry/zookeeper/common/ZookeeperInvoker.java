package com.nepxion.thunder.registry.zookeeper.common;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.PathUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.nepxion.thunder.serialization.SerializerException;
import com.nepxion.thunder.serialization.SerializerExecutor;

public class ZookeeperInvoker {

    // 创建ZooKeeper客户端实例
    public CuratorFramework create(String address, int sessionTimeout, int connectTimeout, int connectWaitTime) {
        // RetryPolicy retryPolicy = new ExponentialBackoffRetry(connectWaitTime, 29); // 连一次连不上，下次连接时间递增
        RetryPolicy retryPolicy = new RetryNTimes(Integer.MAX_VALUE, connectWaitTime); // 不断的频繁重试
        CuratorFramework client = CuratorFrameworkFactory.newClient(address, sessionTimeout, connectTimeout, retryPolicy);

        return client;
    }

    // 启动ZooKeeper客户端
    public void start(CuratorFramework client) {
        client.start();
    }

    // 启动ZooKeeper客户端，直到第一次连接成功
    public void startAndBlock(CuratorFramework client) throws InterruptedException {
        client.start();
        client.blockUntilConnected();
    }

    // 启动ZooKeeper客户端，直到第一次连接成功，为每一次连接配置超时
    public void startAndBlock(CuratorFramework client, int maxWaitTime, TimeUnit units) throws InterruptedException {
        client.start();
        client.blockUntilConnected(maxWaitTime, units);
    }

    // 关闭ZooKeeper客户端连接
    public void close(CuratorFramework client) {
        client.close();
    }

    // 判断路径是否存在
    public boolean pathExist(CuratorFramework client, String path) throws Exception {
        return getPathStat(client, path) != null;
    }

    // 判断stat是否存在
    public Stat getPathStat(CuratorFramework client, String path) throws Exception {
        PathUtils.validatePath(path);

        ExistsBuilder builder = client.checkExists();
        if (builder == null) {
            return null;
        }

        Stat stat = builder.forPath(path);

        return stat;
    }
    
    // 创建路径
    public void createPath(CuratorFramework client, String path) throws Exception {
        PathUtils.validatePath(path);
        
        client.create().creatingParentsIfNeeded().forPath(path, null);
    }
    
    // 创建路径，并写入数据
    public void createPath(CuratorFramework client, String path, byte[] data) throws Exception {
        PathUtils.validatePath(path);
        
        client.create().creatingParentsIfNeeded().forPath(path, data);
    }
    
    // 创建路径，并写入对象
    public void createPath(CuratorFramework client, String path, Serializable object) throws Exception {
        PathUtils.validatePath(path);
        
        byte[] data = getData(object);
        
        client.create().creatingParentsIfNeeded().forPath(path, data);
    }
    
    // 创建路径
    public void createPath(CuratorFramework client, String path, CreateMode mode) throws Exception {
        PathUtils.validatePath(path);
        
        client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, null);
    }
    
    // 创建路径，并写入数据
    public void createPath(CuratorFramework client, String path, byte[] data, CreateMode mode) throws Exception {
        PathUtils.validatePath(path);
        
        client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data);
    }
    
    // 创建路径，并写入对象
    public void createPath(CuratorFramework client, String path, Serializable object, CreateMode mode) throws Exception {
        PathUtils.validatePath(path);
        
        byte[] data = getData(object);
        
        client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data);
    }
    
    // 删除路径
    public void deletePath(CuratorFramework client, String path) throws Exception {
        PathUtils.validatePath(path);
        
        client.delete().deletingChildrenIfNeeded().forPath(path);
    }
    
    // 获取数据
    public byte[] getData(CuratorFramework client, String path) throws Exception {
        PathUtils.validatePath(path);
        
        return client.getData().forPath(path);
    }
    
    // 获取对象
    public <T> T getObject(CuratorFramework client, String path, Class<T> clazz) throws Exception {
        PathUtils.validatePath(path);
        
        byte[] data = client.getData().forPath(path);
        
        return getObject(data, clazz);
    }
    
    // 写入数据
    public void setData(CuratorFramework client, String path, byte[] data) throws Exception {
        PathUtils.validatePath(path);
        
        client.setData().forPath(path, data);
    }
    
    // 写入对象
    public void setData(CuratorFramework client, String path, Serializable object) throws Exception {
        PathUtils.validatePath(path);
        
        byte[] data = getData(object);
        
        client.setData().forPath(path, data);
    }
    
    // 转换字节组数为对象
    public <T> T getObject(byte[] data, Class<T> clazz) throws SerializerException {
        try {
            return SerializerExecutor.deserialize(data, false);
        } catch (Exception e) {
            throw new SerializerException("Class can't be compatible", e);
        }
    }
    
    // 转换对象为字节组数
    public byte[] getData(Serializable object) throws SerializerException {
        try {
            return SerializerExecutor.serialize(object, false);
        } catch (Exception e) {
            throw new SerializerException("Class can't be compatible", e);
        }
    }

    // 获取子节点名称列表
    public List<String> getChildNameList(CuratorFramework client, String path) throws Exception {
        PathUtils.validatePath(path);
        
        return client.getChildren().forPath(path);
    }
    
    // 获取子节点路径列表
    public List<String> getChildPathList(CuratorFramework client, String path) throws Exception {
        List<String> childNameList = getChildNameList(client, path);
        
        List<String> childPathList = new ArrayList<String>();
        for (String childName : childNameList) {
            String childPath = path + "/" + childName;
            childPathList.add(childPath);
        }
        
        return childPathList;
    }
    
    // ZooKeeper节点的路径必须存在，如果不存在则立即创建
    /*public boolean createPath(final CuratorFramework client, final String path) throws Exception {
        PathUtils.validatePath(path);

        final CuratorZookeeperClient client = client.getZookeeperClient();*/

        /*
         * 操作重试，如果在执行一个操作时，遇到了ZooKeeper链接异常：
         * 1. RetryLoop可以不断重试，直到网络正常且操作执行成功为止
         * 2. RetryLoop只负责KeeperException，不负责其他RuntimeException，
         *    一旦KeeperException被捕获，就开始Retry
         *    一旦其他异常被捕获，就停止Retry
         *    无任何异常，不Retry 
         */
        /*return RetryLoop.callWithRetry(client, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EnsurePath ensure = new EnsurePath(path);
                ensure.ensure(client);

                return true;
            }
        });
    }*/
}