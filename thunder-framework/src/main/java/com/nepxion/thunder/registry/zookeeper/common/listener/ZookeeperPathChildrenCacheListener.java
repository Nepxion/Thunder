package com.nepxion.thunder.registry.zookeeper.common.listener;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

public abstract class ZookeeperPathChildrenCacheListener extends ZookeeperCacheListener implements PathChildrenCacheListener {
    protected PathChildrenCache pathChildrenCache;

    public ZookeeperPathChildrenCacheListener(CuratorFramework client, String path) throws Exception {
        super(client, path);

        pathChildrenCache = new PathChildrenCache(client, path, false);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        addListener();
    }

    public void addListener() {
        pathChildrenCache.getListenable().addListener(this);
    }

    public void removeListener() {
        pathChildrenCache.getListenable().removeListener(this);
    }

    public PathChildrenCache getPathChildrenCache() {
        return pathChildrenCache;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        PathChildrenCacheEvent.Type type = event.getType();
        switch (type) {
            case INITIALIZED:
                initialized(event);
                break;
            case CHILD_ADDED:
                childAdded(event);
                break;
            case CHILD_UPDATED:
                childUpdated(event);
                break;
            case CHILD_REMOVED:
                childRemoved(event);
                break;
            case CONNECTION_SUSPENDED:
                connectionSuspended(event);
                break;
            case CONNECTION_RECONNECTED:
                connectionReconnected(event);
                break;
            case CONNECTION_LOST:
                connectionLost(event);
                break;
        }
    }

    public abstract void initialized(PathChildrenCacheEvent event) throws Exception;

    public abstract void childAdded(PathChildrenCacheEvent event) throws Exception;

    public abstract void childUpdated(PathChildrenCacheEvent event) throws Exception;

    public abstract void childRemoved(PathChildrenCacheEvent event) throws Exception;

    public abstract void connectionSuspended(PathChildrenCacheEvent event) throws Exception;

    public abstract void connectionReconnected(PathChildrenCacheEvent event) throws Exception;

    public abstract void connectionLost(PathChildrenCacheEvent event) throws Exception;
}