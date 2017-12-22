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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.curator.utils.PathUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.nepxion.thunder.serialization.SerializerException;
import com.nepxion.thunder.serialization.SerializerExecutor;

public class ZookeeperInvoker {
    private CuratorFramework client;

    private final Lock lock = new ReentrantLock();

    // 重试指定的次数, 且每一次重试之间停顿的时间逐渐增加
    public RetryPolicy createExponentialBackoffRetry(int baseSleepTimeMs, int maxRetries) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);

        return retryPolicy;
    }

    // 重试指定的次数, 且每一次重试之间停顿的时间逐渐增加，增加了最大重试次数的控制
    public RetryPolicy createBoundedExponentialBackoffRetry(int baseSleepTimeMs, int maxSleepTimeMs, int maxRetries) {
        RetryPolicy retryPolicy = new BoundedExponentialBackoffRetry(baseSleepTimeMs, maxSleepTimeMs, maxRetries);

        return retryPolicy;
    }

    // 指定最大重试次数的重试
    public RetryPolicy createRetryNTimes(int count, int sleepMsBetweenRetries) {
        RetryPolicy retryPolicy = new RetryNTimes(count, sleepMsBetweenRetries);

        return retryPolicy;
    }

    // 永远重试
    public RetryPolicy createRetryForever(int retryIntervalMs) {
        RetryPolicy retryPolicy = new RetryForever(retryIntervalMs);

        return retryPolicy;
    }

    // 一直重试，直到达到规定的时间 
    public RetryPolicy createRetryUntilElapsed(int maxElapsedTimeMs, int sleepMsBetweenRetries) {
        RetryPolicy retryPolicy = new RetryUntilElapsed(maxElapsedTimeMs, sleepMsBetweenRetries);

        return retryPolicy;
    }

    // 创建ZooKeeper客户端实例
    public void create(String address, int sessionTimeout, int connectTimeout, int connectWaitTime) throws Exception {
        try {
            lock.lock();

            if (client != null) {
                throw new ZookeeperException("Zookeeper client isn't null, it has been initialized already");
            }

            // RetryPolicy retryPolicy = createExponentialBackoffRetry(connectWaitTime, 29);
            RetryPolicy retryPolicy = createRetryNTimes(Integer.MAX_VALUE, connectWaitTime);
            client = CuratorFrameworkFactory.newClient(address, sessionTimeout, connectTimeout, retryPolicy);
        } finally {
            lock.unlock();
        }
    }

    // 启动ZooKeeper客户端
    public void start() throws Exception {
        try {
            lock.lock();

            validateClosedStatus();

            client.start();
        } finally {
            lock.unlock();
        }
    }

    // 启动ZooKeeper客户端，直到第一次连接成功
    public void startAndBlock() throws Exception {
        try {
            lock.lock();

            validateClosedStatus();

            client.start();
            client.blockUntilConnected();
        } finally {
            lock.unlock();
        }
    }

    // 启动ZooKeeper客户端，直到第一次连接成功，为每一次连接配置超时
    public void startAndBlock(int maxWaitTime, TimeUnit units) throws Exception {
        try {
            lock.lock();

            validateClosedStatus();

            client.start();
            client.blockUntilConnected(maxWaitTime, units);
        } finally {
            lock.unlock();
        }
    }

    // 关闭ZooKeeper客户端连接
    public void close() throws Exception {
        try {
            lock.lock();

            validateStartedStatus();

            client.close();
        } finally {
            lock.unlock();
        }
    }

    // 获取ZooKeeper客户端是否初始化
    public boolean isInitialized() {
        return client != null;
    }

    // 获取ZooKeeper客户端连接是否正常
    public boolean isStarted() {
        return client.getState() == CuratorFrameworkState.STARTED;
    }

    // 检查ZooKeeper是否是启动状态
    public void validateStartedStatus() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        if (!isStarted()) {
            throw new ZookeeperException("Zookeeper client is closed");
        }
    }

    // 检查ZooKeeper是否是关闭状态
    public void validateClosedStatus() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        if (isStarted()) {
            throw new ZookeeperException("Zookeeper client is started");
        }
    }

    // 获取ZooKeeper客户端
    public CuratorFramework getClient() {
        return client;
    }

    // 判断路径是否存在
    public boolean pathExist(String path) throws Exception {
        return getPathStat(path) != null;
    }

    // 判断stat是否存在
    public Stat getPathStat(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        ExistsBuilder builder = client.checkExists();
        if (builder == null) {
            return null;
        }

        Stat stat = builder.forPath(path);

        return stat;
    }

    // 创建路径
    public void createPath(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        client.create().creatingParentsIfNeeded().forPath(path, null);
    }

    // 创建路径，并写入数据
    public void createPath(String path, byte[] data) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        client.create().creatingParentsIfNeeded().forPath(path, data);
    }

    // 创建路径，并写入对象
    public void createPath(String path, Serializable object) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        byte[] data = getData(object);

        client.create().creatingParentsIfNeeded().forPath(path, data);
    }

    // 创建路径
    public void createPath(String path, CreateMode mode) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, null);
    }

    // 创建路径，并写入数据
    public void createPath(String path, byte[] data, CreateMode mode) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data);
    }

    // 创建路径，并写入对象
    public void createPath(String path, Serializable object, CreateMode mode) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        byte[] data = getData(object);

        client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, data);
    }

    // 删除路径
    public void deletePath(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        client.delete().deletingChildrenIfNeeded().forPath(path);
    }

    // 获取数据
    public byte[] getData(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        return client.getData().forPath(path);
    }

    // 获取对象
    public <T> T getObject(String path, Class<T> clazz) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        byte[] data = client.getData().forPath(path);

        return getObject(data, clazz);
    }

    // 写入数据
    public void setData(String path, byte[] data) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        client.setData().forPath(path, data);
    }

    // 写入对象
    public void setData(String path, Serializable object) throws Exception {
        validateStartedStatus();
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
    public List<String> getChildNameList(String path) throws Exception {
        validateStartedStatus();
        PathUtils.validatePath(path);

        return client.getChildren().forPath(path);
    }

    // 获取子节点路径列表
    public List<String> getChildPathList(String path) throws Exception {
        List<String> childNameList = getChildNameList(path);

        List<String> childPathList = new ArrayList<String>();
        for (String childName : childNameList) {
            String childPath = path + "/" + childName;
            childPathList.add(childPath);
        }

        return childPathList;
    }
}