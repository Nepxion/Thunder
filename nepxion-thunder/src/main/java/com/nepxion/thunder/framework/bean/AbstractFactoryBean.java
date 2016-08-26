package com.nepxion.thunder.framework.bean;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.util.ClassUtil;

@SuppressWarnings("all")
public abstract class AbstractFactoryBean implements ApplicationContextAware, FactoryBean, InitializingBean {
    protected ApplicationContext applicationContext;
    
    protected ThunderDelegate delegate;
    protected ThunderProperties properties;
    protected CacheContainer cacheContainer;
    protected ExecutorContainer executorContainer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public ThunderDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(ThunderDelegate delegate) {
        this.delegate = delegate;
        this.properties = delegate.getProperties();
        this.cacheContainer = delegate.getCacheContainer();
        this.executorContainer = delegate.getExecutorContainer();
    }
    
    public ThunderProperties getProperties() {
        return properties;
    }

    public CacheContainer getCacheContainer() {
        return cacheContainer;
    }

    public ExecutorContainer getExecutorContainer() {
        return executorContainer;
    }
    
    protected <T> T createDelegate(String delegateClassId) throws Exception {        
        return delegate.createDelegate(delegateClassId);
    }
}