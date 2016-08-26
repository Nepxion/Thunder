package com.nepxion.thunder.registry.zookeeper.common.watcher;

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
import org.apache.curator.framework.api.BackgroundPathable;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.utils.PathUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;

import com.nepxion.thunder.registry.zookeeper.common.ZookeeperException;

public abstract class ZookeeperWatcher implements CuratorWatcher {
    protected CuratorFramework client;
    protected ZookeeperWatcherType watcherType;
    protected String path;

    public ZookeeperWatcher(CuratorFramework client) {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        this.client = client;
    }
    
    public CuratorFramework getClient() {
        return client;
    }

    public String getPath() {
        return path;
    }

    public void usingWatcher(ZookeeperWatcherType watcherType, String path) throws Exception {
        if (watcherType == null) {
            throw new ZookeeperException("Watcher type is null");
        }

        PathUtils.validatePath(path);

        this.watcherType = watcherType;
        this.path = path;

        switch (watcherType) {
            case EXISTS:
                usingWatcher(client.checkExists().usingWatcher(this), path);
                break;
            case GET_CHILDREN:
                usingWatcher(client.getChildren().usingWatcher(this), path);
                break;
            case GET_DATA:
                usingWatcher(client.getData().usingWatcher(this), path);
                break;
        }
    }

    public void usingWatcher(BackgroundPathable<?> backgroundPathable, String path) throws Exception {
        backgroundPathable.forPath(path);
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
        String path = event.getPath();
        EventType type = event.getType();

        if (type != EventType.None) {
            usingWatcher(watcherType, path);
        }

        switch (type) {
            case None:
                none(event);
                break;
            case NodeCreated:
                nodeCreated(event);
                break;
            case NodeDeleted:
                nodeDeleted(event);
                break;
            case NodeDataChanged:
                nodeDataChanged(event);
                break;
            case NodeChildrenChanged:
                nodeChildrenChanged(event);
                break;
        }
    }

    public abstract void none(WatchedEvent event) throws Exception;

    public abstract void nodeCreated(WatchedEvent event) throws Exception;

    public abstract void nodeDeleted(WatchedEvent event) throws Exception;

    public abstract void nodeDataChanged(WatchedEvent event) throws Exception;

    public abstract void nodeChildrenChanged(WatchedEvent event) throws Exception;
}