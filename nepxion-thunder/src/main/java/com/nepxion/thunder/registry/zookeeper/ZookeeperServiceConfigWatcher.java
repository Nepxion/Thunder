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

import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;
import com.nepxion.thunder.registry.zookeeper.common.listener.ZookeeperNodeCacheListener;

public class ZookeeperServiceConfigWatcher extends ZookeeperNodeCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperServiceConfigWatcher.class);
    
    private String interfaze;
    
    private ZookeeperInvoker invoker;
    private CacheContainer cacheContainer;
    
    public ZookeeperServiceConfigWatcher(CuratorFramework client, String interfaze, ZookeeperInvoker invoker, CacheContainer cacheContainer, String path) throws Exception {
        super(client, path);
        
        this.interfaze = interfaze;
        this.invoker = invoker;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public void nodeChanged() throws Exception {
        ServiceConfig serviceConfig = invoker.getObject(client, path, ServiceConfig.class);

        Map<String, ServiceConfig> serviceConfigMap = cacheContainer.getServiceConfigMap();
        serviceConfigMap.put(interfaze, serviceConfig);
        
        Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
        ServiceEntity serviceEntity = serviceEntityMap.get(interfaze);
        long token = serviceConfig.getToken();
        serviceEntity.setDefaultToken(token);
        serviceEntity.setToken(token);
        
        LOG.info("Watched - service config is changed, interface={}", interfaze);
    }
}