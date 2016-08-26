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

import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;
import com.nepxion.thunder.registry.zookeeper.common.listener.ZookeeperNodeCacheListener;

public class ZookeeperReferenceConfigWatcher extends ZookeeperNodeCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperReferenceConfigWatcher.class);
    
    private String interfaze;
    
    private ZookeeperInvoker invoker;
    private CacheContainer cacheContainer;
    
    public ZookeeperReferenceConfigWatcher(CuratorFramework client, String interfaze, ZookeeperInvoker invoker, CacheContainer cacheContainer, String path) throws Exception {
        super(client, path);
        
        this.interfaze = interfaze;
        this.invoker = invoker;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public void nodeChanged() throws Exception {
        ReferenceConfig referenceConfig = invoker.getObject(client, path, ReferenceConfig.class);

        Map<String, ReferenceConfig> referenceConfigMap = cacheContainer.getReferenceConfigMap();
        referenceConfigMap.put(interfaze, referenceConfig);
        
        LOG.info("Watched - reference config is changed, interface={}", interfaze);
    }
}