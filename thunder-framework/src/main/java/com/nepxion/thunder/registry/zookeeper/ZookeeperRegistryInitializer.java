package com.nepxion.thunder.registry.zookeeper;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.registry.RegistryInitializer;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperException;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperRegistryInitializer extends ThunderDelegateImpl implements RegistryInitializer {
    private ZookeeperInvoker invoker = new ZookeeperInvoker();

    @Override
    public void start(RegistryEntity registryEntity) throws Exception {
        if (properties == null) {
            throw new ZookeeperException("Properties is null");
        }

        String address = registryEntity.getAddress();
        int sessionTimeout = properties.getInteger(ThunderConstant.ZOOKEEPER_SESSION_TIMOUT_ATTRIBUTE_NAME);
        int connectTimeout = properties.getInteger(ThunderConstant.ZOOKEEPER_CONNECT_TIMEOUT_ATTRIBUTE_NAME);
        int connectWaitTime = properties.getInteger(ThunderConstant.ZOOKEEPER_CONNECT_WAIT_TIME_ATTRIBUTE_NAME);

        invoker.create(address, sessionTimeout, connectTimeout, connectWaitTime);
        invoker.startAndBlock();
    }

    @Override
    public void start(RegistryEntity registryEntity, ThunderProperties properties) throws Exception {
        setProperties(properties);

        start(registryEntity);
    }

    @Override
    public void stop() throws Exception {
        invoker.close();
    }

    public ZookeeperInvoker getInvoker() {
        return invoker;
    }
}