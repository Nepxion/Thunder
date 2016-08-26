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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;
import com.nepxion.thunder.registry.zookeeper.common.listener.ZookeeperNodeCacheListener;
import com.nepxion.thunder.security.SecurityBootstrap;

public class ZookeeperApplicationConfigWatcher extends ZookeeperNodeCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperApplicationConfigWatcher.class);

    private ZookeeperInvoker invoker;
    private CacheContainer cacheContainer;

    public ZookeeperApplicationConfigWatcher(CuratorFramework client, ZookeeperInvoker invoker, CacheContainer cacheContainer, String path) throws Exception {
        super(client, path);

        this.invoker = invoker;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public void nodeChanged() throws Exception {
        ApplicationConfig applicationConfig = invoker.getObject(client, path, ApplicationConfig.class);

        ApplicationConfig config = cacheContainer.getApplicationConfig();

        if (applicationConfig.getFrequency() != config.getFrequency()) {
            SecurityBootstrap securityBootstrap = cacheContainer.getSecurityBootstrap();
            if (securityBootstrap != null) {
                int frequency = applicationConfig.getFrequency();

                securityBootstrap.restart(frequency);
            }
        }

        cacheContainer.setApplicationConfig(applicationConfig);

        LOG.info("Watched - application config is changed");
    }
}