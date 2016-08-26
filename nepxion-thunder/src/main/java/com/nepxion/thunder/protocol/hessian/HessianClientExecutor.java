package com.nepxion.thunder.protocol.hessian;

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

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ConnectionEntity;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.protocol.AbstractClientExecutor;

public class HessianClientExecutor extends AbstractClientExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(HessianClientExecutor.class);

    private HessianAuthProxyFactory proxyFactory = new HessianAuthProxyFactory();

    @Override
    public void start(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        boolean started = started(interfaze, applicationEntity);
        if (started) {
            return;
        }
        
        proxyFactory.setReferenceConfigMap(cacheContainer.getReferenceConfigMap());

        Class<?> interfaceClass = Class.forName(interfaze);

        String url = HessianUrlUtil.toUrl(interfaze, applicationEntity, cacheContainer);

        Object proxy = proxyFactory.create(interfaceClass, url);

        LOG.info("Create proxy for {} successfully", url);

        online(interfaze, applicationEntity, proxy);
    }
    
    @Override
    public void setProperties(ThunderProperties properties) {
        super.setProperties(properties);

        long readTimeout = properties.getLong(ThunderConstants.HESSIAN_READ_TIMEOUT_ATTRIBUTE_NAME);
        long connectTimeout = properties.getLong(ThunderConstants.HESSIAN_CONNECT_TIMEOUT_ATTRIBUTE_NAME);

        proxyFactory.setOverloadEnabled(true);
        proxyFactory.setReadTimeout(readTimeout);
        proxyFactory.setConnectTimeout(connectTimeout);
    }
    
    @Override
    public ConnectionEntity online(String interfaze, ApplicationEntity applicationEntity, Object proxy) throws Exception {
        ConnectionEntity connectionEntity = super.online(interfaze, applicationEntity, proxy);

        String url = HessianUrlUtil.toUrl(interfaze, applicationEntity, cacheContainer);
        connectionEntity.setUrl(url);

        return connectionEntity;
    }
}