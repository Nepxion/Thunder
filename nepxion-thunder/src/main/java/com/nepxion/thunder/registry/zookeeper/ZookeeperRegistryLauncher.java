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

import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.registry.RegistryInitializer;
import com.nepxion.thunder.registry.RegistryLauncher;

public class ZookeeperRegistryLauncher implements RegistryLauncher {
    private RegistryInitializer registryInitializer;
    private RegistryExecutor registryExecutor;

    @Override
    public void start(String address, ProtocolType protocolType) throws Exception {
        // 读取配置文件
        ThunderProperties properties = ThunderPropertiesManager.getProperties();

        RegistryEntity registryEntity = new RegistryEntity();
        registryEntity.setAddress(address);

        ProtocolEntity protocolEntity = new ProtocolEntity();
        protocolEntity.setType(protocolType);

        // 启动Zookeeper连接
        registryInitializer = new ZookeeperRegistryInitializer();
        registryInitializer.start(registryEntity, properties);

        registryExecutor = new ZookeeperRegistryExecutor();
        registryExecutor.setRegistryInitializer(registryInitializer);
        registryExecutor.setProperties(properties);
        registryExecutor.setProtocolEntity(protocolEntity);
    }

    @Override    
    public void stop() throws Exception {
        // 停止Zookeeper连接
        registryInitializer.stop();
    }

    @Override    
    public RegistryExecutor getRegistryExecutor() {
        return registryExecutor;
    }
}