package com.nepxion.thunder.registry.zookeeper;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.entity.MonitorEntity;
import com.nepxion.thunder.event.registry.InstanceEventType;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;
import com.nepxion.thunder.registry.zookeeper.common.listener.ZookeeperPathChildrenCacheListener;

public class ZookeeperMonitorInstanceWatcher extends ZookeeperPathChildrenCacheListener {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperMonitorInstanceWatcher.class);

    private ZookeeperInvoker invoker;
    private CacheContainer cacheContainer;

    public ZookeeperMonitorInstanceWatcher(ZookeeperInvoker invoker, CacheContainer cacheContainer, String path) throws Exception {
        super(invoker.getClient(), path);

        this.invoker = invoker;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public void initialized(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void childAdded(PathChildrenCacheEvent event) throws Exception {
        onEvent(event, InstanceEventType.ONLINE);
    }

    @Override
    public void childUpdated(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void childRemoved(PathChildrenCacheEvent event) throws Exception {
        onEvent(event, InstanceEventType.OFFLINE);
    }

    @Override
    public void connectionSuspended(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void connectionReconnected(PathChildrenCacheEvent event) throws Exception {

    }

    @Override
    public void connectionLost(PathChildrenCacheEvent event) throws Exception {

    }

    private void onEvent(PathChildrenCacheEvent event, InstanceEventType instanceEventType) throws Exception {
        MonitorEntity monitorEntity = cacheContainer.getMonitorEntity();

        String childPath = event.getData().getPath();
        String address = childPath.substring(childPath.lastIndexOf("/") + 1);
        /*switch (instanceEventType) {
            case ONLINE:
                monitorEntity.addAddress(address);
                break;
            case OFFLINE:
                monitorEntity.removeAddress(address);
                break;
        }*/

        List<String> addresses = invoker.getChildNameList(path);
        monitorEntity.setAddresses(addresses);

        LOG.info("Watched {} - address={}", instanceEventType.toString(), address);
    }
}