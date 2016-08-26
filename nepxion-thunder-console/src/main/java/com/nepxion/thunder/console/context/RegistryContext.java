package com.nepxion.thunder.console.context;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.registry.RegistryInitializer;
import com.nepxion.thunder.registry.zookeeper.ZookeeperRegistryExecutor;
import com.nepxion.thunder.registry.zookeeper.ZookeeperRegistryInitializer;

public class RegistryContext {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryContext.class);
    private static RegistryInitializer registryInitializer = new ZookeeperRegistryInitializer();
    private static Map<ProtocolType, RegistryExecutor> registryExecutorMap = Maps.newConcurrentMap();

    private static boolean start = false;

    public static void start() {
        if (start) {
            return;
        }

        String registryAddress = PropertiesContext.getRegistryAddress();
        try {
            RegistryEntity registryEntity = new RegistryEntity();
            registryEntity.setAddress(registryAddress);

            ThunderProperties properties = PropertiesContext.getProperties();
            registryInitializer.start(registryEntity, properties);

            for (ProtocolType protocolType : ProtocolType.values()) {
                ProtocolEntity protocolEntity = new ProtocolEntity();
                protocolEntity.setType(protocolType);

                RegistryExecutor registryExecutor = new ZookeeperRegistryExecutor();
                registryExecutor.setRegistryInitializer(registryInitializer);
                registryExecutor.setProperties(properties);
                registryExecutor.setProtocolEntity(protocolEntity);

                registryExecutorMap.put(protocolType, registryExecutor);
            }

            start = true;
        } catch (Exception e) {
            LOG.error("Start connection with Registry Center failed, address={}", registryAddress, e);

            start = false;
        }
    }

    public static void stop() {
        if (!start) {
            return;
        }

        try {
            registryInitializer.stop();
            registryExecutorMap.clear();
        } catch (Exception e) {
            LOG.error("Stop connection with Registry Center failed", e);
        }
    }

    public static Map<ProtocolType, RegistryExecutor> getRegistryExecutorMap() {
        return registryExecutorMap;
    }

    public static RegistryExecutor getRegistryExecutor(ProtocolType protocolType) {
        return registryExecutorMap.get(protocolType);
    }

    public static RegistryExecutor getDefaultRegistryExecutor() {
        return getRegistryExecutor(ProtocolType.NETTY);
    }
}