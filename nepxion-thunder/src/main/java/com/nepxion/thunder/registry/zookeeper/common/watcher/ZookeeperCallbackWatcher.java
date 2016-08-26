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

import java.util.concurrent.Executor;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.BackgroundPathable;

public abstract class ZookeeperCallbackWatcher extends ZookeeperWatcher {

    private BackgroundCallback callback;
    private Object context;
    private Executor executor;

    public ZookeeperCallbackWatcher(CuratorFramework client, BackgroundCallback callback, Object context, Executor executor) {
        super(client);

        this.callback = callback;
        this.context = context;
        this.executor = executor;
    }

    @Override
    public void usingWatcher(BackgroundPathable<?> backgroundPathable, String path) throws Exception {
        backgroundPathable.inBackground(callback, context, executor).forPath(path);
    }
}