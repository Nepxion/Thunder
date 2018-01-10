package com.nepxion.thunder.framework.bean;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.common.util.ClassUtil;
import com.nepxion.thunder.protocol.ServerExecutor;
import com.nepxion.thunder.protocol.ServerExecutorAdapter;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.security.SecurityBootstrap;
import com.nepxion.thunder.security.SecurityExecutor;
import com.nepxion.thunder.serialization.SerializerException;

@SuppressWarnings("all")
public class ServiceFactoryBean extends AbstractFactoryBean {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceFactoryBean.class);

    private Class<?> interfaze;
    private Object service;

    private ServiceEntity serviceEntity;
    private ServerExecutor serverExecutor;
    private ServerExecutorAdapter serverExecutorAdapter;
    private SecurityExecutor securityExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        cacheService();

        startServer();

        registerService();

        startSecurityBootstrap();

        LOG.info("ServiceFactoryBean has been initialized...");
    }

    // 缓存Service
    protected void cacheService() {
        String interfaceName = interfaze.getName();

        serviceEntity.setService(service);

        Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
        serviceEntityMap.put(interfaceName, serviceEntity);
    }

    // 注册Service，并把服务所在的应用信息写入
    protected void registerService() throws Exception {
        String interfaceName = interfaze.getName();
        List<String> methods = ClassUtil.convertMethodList(interfaze);

        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

        // 注册Service
        RegistryExecutor registryExecutor = executorContainer.getRegistryExecutor();
        registryExecutor.registerService(interfaceName, applicationEntity);

        // 持久化Service配置信息
        ServiceConfig serviceConfig = null;
        try {
            serviceConfig = registryExecutor.retrieveService(interfaceName, applicationEntity);
        } catch (SerializerException e) {
            LOG.warn("ServiceConfig class was upgraded, Registry Center will initialize it again");
        }
        if (serviceConfig == null) {
            serviceConfig = new ServiceConfig();
            serviceConfig.setInterface(interfaceName);
            serviceConfig.setMethods(methods);
            serviceConfig.setSecretKey(properties.getString(ThunderConstant.SECRET_KEY_ATTRIBUTE_NAME));
            serviceConfig.setVersion(properties.getInteger(ThunderConstant.VERSION_ATTRIBUTE_NAME));
            serviceConfig.setToken(properties.getLong(ThunderConstant.TOKEN_ATTRIBUTE_NAME));

            registryExecutor.persistService(serviceConfig, applicationEntity);
        } else {
            List<String> methodList = serviceConfig.getMethods();
            // 方法列表是null(可以是空)或者两个方法列表不相等(methods肯定不为null)
            if (methodList == null || !CollectionUtils.isEqualCollection(methodList, methods)) {
                LOG.info("ServiceConfig methods are changed, Registry Center will initialize it again");

                serviceConfig.setMethods(methods);

                registryExecutor.persistService(serviceConfig, applicationEntity);
            }
        }

        Map<String, ServiceConfig> serviceConfigMap = cacheContainer.getServiceConfigMap();
        serviceConfigMap.put(interfaceName, serviceConfig);

        // Service配置信息更改后通知服务端，包括限流和密钥
        registryExecutor.addServiceConfigWatcher(interfaceName, applicationEntity);

        // 把限流配置回写到本地
        serviceEntity.setDefaultToken(serviceConfig.getToken());
        serviceEntity.setToken(serviceConfig.getToken());
    }

    // 启动服务端
    protected void startServer() throws Exception {
        String interfaceName = interfaze.getName();

        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

        try {
            serverExecutor.start(interfaceName, applicationEntity);
        } catch (Exception e) {
            LOG.error("Start server failed", e);
            throw e;
        }
    }

    // 启动令牌刷新
    protected void startSecurityBootstrap() throws Exception {
        SecurityBootstrap securityBootstrap = cacheContainer.getSecurityBootstrap();
        if (securityBootstrap == null) {
            ApplicationConfig applicationConfig = cacheContainer.getApplicationConfig();
            int frequency = applicationConfig.getFrequency();

            Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();

            securityBootstrap = new SecurityBootstrap();
            securityBootstrap.setServiceEntityMap(serviceEntityMap);
            securityBootstrap.start(frequency);

            cacheContainer.setSecurityBootstrap(securityBootstrap);
        }
    }

    @Override
    public Object getObject() throws Exception {
        return service;
    }

    @Override
    public Class getObjectType() {
        return interfaze;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setInterface(Class<?> interfaze) {
        this.interfaze = interfaze;
    }

    public Class<?> getInterface() {
        return interfaze;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public Object getService() {
        return service;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServerExecutor(ServerExecutor serverExecutor) {
        this.serverExecutor = serverExecutor;
    }

    public ServerExecutor getServerExecutor() {
        return serverExecutor;
    }

    public void setServerExecutorAdapter(ServerExecutorAdapter serverExecutorAdapter) {
        this.serverExecutorAdapter = serverExecutorAdapter;
    }

    public ServerExecutorAdapter getServerExecutorAdapter() {
        return serverExecutorAdapter;
    }

    public void setSecurityExecutor(SecurityExecutor securityExecutor) {
        this.securityExecutor = securityExecutor;
    }

    public SecurityExecutor getSecurityExecutor() {
        return securityExecutor;
    }
}