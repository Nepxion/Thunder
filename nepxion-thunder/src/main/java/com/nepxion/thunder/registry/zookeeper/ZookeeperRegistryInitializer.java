package com.nepxion.thunder.registry.zookeeper;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.curator.framework.CuratorFramework;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl; 
import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.registry.RegistryInitializer;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperException;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperRegistryInitializer extends ThunderDelegateImpl implements RegistryInitializer {
    private ZookeeperInvoker invoker = new ZookeeperInvoker();
    private CuratorFramework client;

    @Override
    public void start(RegistryEntity registryEntity) throws Exception {
        if (client != null) {
            throw new ZookeeperException("Zookeeper has started");
        }

        if (properties == null) {
            throw new ZookeeperException("properties is null");
        }

        String address = registryEntity.getAddress();
        int sessionTimeout = properties.getInteger(ThunderConstants.ZOOKEEPER_SESSION_TIMOUT_ATTRIBUTE_NAME);
        int connectTimeout = properties.getInteger(ThunderConstants.ZOOKEEPER_CONNECT_TIMEOUT_ATTRIBUTE_NAME);
        int connectWaitTime = properties.getInteger(ThunderConstants.ZOOKEEPER_CONNECT_WAIT_TIME_ATTRIBUTE_NAME);
        client = invoker.create(address, sessionTimeout, connectTimeout, connectWaitTime);
        invoker.startAndBlock(client);
    }

    @Override
    public void start(RegistryEntity registryEntity, ThunderProperties properties) throws Exception {
        if (client != null) {
            throw new ZookeeperException("Zookeeper has started");
        }

        setProperties(properties);

        start(registryEntity);
    }

    @Override
    public void stop() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        invoker.close(client);
    }

    public ZookeeperInvoker getInvoker() {
        return invoker;
    }

    public CuratorFramework getClient() {
        return client;
    }
}