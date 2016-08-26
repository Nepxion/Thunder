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

import com.nepxion.thunder.common.entity.ProtocolEntity;

public class ProtocolFactoryBean extends AbstractFactoryBean {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolFactoryBean.class);

    private ProtocolEntity protocolEntity;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("ProtocolFactoryBean has been initialized...");
    }

    @Override
    public ProtocolEntity getObject() throws Exception {
        return protocolEntity;
    }

    @Override
    public Class<ProtocolEntity> getObjectType() {
        return ProtocolEntity.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setProtocolEntity(ProtocolEntity protocolEntity) {
        this.protocolEntity = protocolEntity;
    }

    public ProtocolEntity getProtocolEntity() {
        return protocolEntity;
    }
}