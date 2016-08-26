package com.nepxion.thunder.common.delegate;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.util.ClassUtil;

public class ThunderDelegateImpl implements ThunderDelegate {
    protected ThunderProperties properties;
    protected CacheContainer cacheContainer;
    protected ExecutorContainer executorContainer;

    public ThunderDelegateImpl() {

    }
    
    @Override
    public ThunderProperties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(ThunderProperties properties) {
        this.properties = properties;
    }

    @Override
    public CacheContainer getCacheContainer() {
        return cacheContainer;
    }

    @Override
    public void setCacheContainer(CacheContainer cacheContainer) {
        this.cacheContainer = cacheContainer;
    }

    @Override
    public ExecutorContainer getExecutorContainer() {
        return executorContainer;
    }

    @Override
    public void setExecutorContainer(ExecutorContainer executorContainer) {
        this.executorContainer = executorContainer;
    }
    
    @Override
    public <T> T createDelegate(String delegateClassId) throws Exception {
        String delegateClassName = properties.get(delegateClassId);

        T delegateInstance = ClassUtil.createInstance(delegateClassName);
        
        ThunderDelegate delegate = (ThunderDelegate) delegateInstance;
        delegate.setProperties(properties);
        delegate.setCacheContainer(cacheContainer);
        delegate.setExecutorContainer(executorContainer);
        
        return delegateInstance;
    }
}