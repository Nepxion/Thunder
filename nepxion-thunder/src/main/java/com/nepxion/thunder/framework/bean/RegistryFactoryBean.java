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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.registry.RegistryInitializer;

public class RegistryFactoryBean extends AbstractFactoryBean implements DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryFactoryBean.class);

    private RegistryEntity registryEntity;
    private RegistryInitializer registryInitializer;
    private RegistryExecutor registryExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("RegistryFactoryBean has been initialized...");
    }

    @Override
    public RegistryEntity getObject() throws Exception {
        return registryEntity;
    }

    @Override
    public Class<RegistryEntity> getObjectType() {
        return RegistryEntity.class;
    }

    @Override
    public void destroy() throws Exception {
        registryInitializer.stop();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setRegistryEntity(RegistryEntity registryEntity) {
        this.registryEntity = registryEntity;
    }

    public RegistryEntity getRegistryEntity() {
        return registryEntity;
    }

    public RegistryInitializer getRegistryInitializer() {
        return registryInitializer;
    }

    public void setRegistryInitializer(RegistryInitializer registryInitializer) {
        this.registryInitializer = registryInitializer;
    }

    public void setRegistryExecutor(RegistryExecutor registryExecutor) {
        this.registryExecutor = registryExecutor;
    }
    
    public RegistryExecutor getRegistryExecutor() {
        return registryExecutor;
    }
}