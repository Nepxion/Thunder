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

import com.nepxion.thunder.common.entity.ApplicationEntity;

public class ApplicationFactoryBean extends AbstractFactoryBean {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationFactoryBean.class);

    private ApplicationEntity applicationEntity;

    @Override
    public void afterPropertiesSet() throws Exception {        
        LOG.info("ApplicationFactoryBean has been initialized...");
    }

    @Override
    public ApplicationEntity getObject() throws Exception {
        return applicationEntity;
    }

    @Override
    public Class<ApplicationEntity> getObjectType() {
        return ApplicationEntity.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setApplicationEntity(ApplicationEntity applicationEntity) {
        this.applicationEntity = applicationEntity;
    }

    public ApplicationEntity getApplicationEntity() {
        return applicationEntity;
    }
}