package com.nepxion.thunder.registry.zookeeper.common.listener;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

public abstract class ZookeeperNodeCacheListener extends ZookeeperCacheListener implements NodeCacheListener {
    protected NodeCache nodeCache;

    public ZookeeperNodeCacheListener(CuratorFramework client, String path) throws Exception {
        super(client, path);

        nodeCache = new NodeCache(client, path, false);
        nodeCache.start(true);

        addListener();
    }

    public void addListener() {
        nodeCache.getListenable().addListener(this);
    }

    public void removeListener() {
        nodeCache.getListenable().removeListener(this);
    }

    public NodeCache getNodeCache() {
        return nodeCache;
    }
}