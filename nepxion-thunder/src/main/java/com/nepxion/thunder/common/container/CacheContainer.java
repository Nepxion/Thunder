package com.nepxion.thunder.common.container;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ConnectionCacheEntity;
import com.nepxion.thunder.common.entity.MQEntity;
import com.nepxion.thunder.common.entity.MethodEntity;
import com.nepxion.thunder.common.entity.MethodKey;
import com.nepxion.thunder.common.entity.MonitorEntity;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ReferenceEntity;
import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.common.entity.ResponseAsyncEntity;
import com.nepxion.thunder.common.entity.ResponseSyncEntity;
import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.common.entity.StrategyEntity;
import com.nepxion.thunder.common.entity.WebServiceEntity;
import com.nepxion.thunder.security.SecurityBootstrap;

public class CacheContainer {
    private static final Logger LOG = LoggerFactory.getLogger(CacheContainer.class);
    
    // 缓存客户端所有可用连接
    private ConnectionCacheEntity connectionCacheEntity = new ConnectionCacheEntity();

    // 缓存服务端所有的Service，key为interface
    private Map<String, ServiceEntity> serviceEntityMap = Maps.newConcurrentMap();

    // 缓存客户端所有的Reference，key为interface
    private Map<String, ReferenceEntity> referenceEntityMap = Maps.newConcurrentMap();

    // 缓存客户端所有的同步处理响应结果，key为message id
    private Map<String, ResponseSyncEntity> responseSyncEntityMap = Maps.newConcurrentMap();
    
    // 缓存客户端所有的异步处理响应结果，key为message id，用于链式调用
    private Map<String, ResponseAsyncEntity> responseAsyncEntityMap = Maps.newConcurrentMap();
    
    public ConnectionCacheEntity getConnectionCacheEntity() {
        return connectionCacheEntity;
    }

    public Map<String, ServiceEntity> getServiceEntityMap() {
        return serviceEntityMap;
    }

    public Map<String, ReferenceEntity> getReferenceEntityMap() {
        return referenceEntityMap;
    }
    
    public MethodEntity getMethodEntity(String interfaze, MethodKey methodKey) {
        ReferenceEntity referenceEntity = referenceEntityMap.get(interfaze);
        
        return referenceEntity.getMethodEntity(methodKey);
    }
    
    public MethodEntity getMethodEntity(String interfaze, String method, Class<?>[] parameterTypes) {
        MethodKey methodKey = MethodKey.create(method, parameterTypes);
        
        return getMethodEntity(interfaze, methodKey);
    }

    public Map<String, ResponseSyncEntity> getResponseSyncEntityMap() {
        return responseSyncEntityMap;
    }
    
    public Map<String, ResponseAsyncEntity> getResponseAsyncEntityMap() {
        return responseAsyncEntityMap;
    }
        
    // 缓存应用实体
    private ApplicationEntity applicationEntity;

    // 缓存协议实体
    private ProtocolEntity protocolEntity;

    // 缓存注册实体
    private RegistryEntity registryEntity;

    // 缓存策略实体
    private StrategyEntity strategyEntity;
    
    // 缓存监控实体
    private MonitorEntity monitorEntity;
    
    // 缓存Web服务实体
    private WebServiceEntity webServiceEntity;
    
    // 缓存MQ实体
    private MQEntity mqEntity;

    public ApplicationEntity getApplicationEntity() {
        return applicationEntity;
    }

    public void setApplicationEntity(ApplicationEntity applicationEntity) {
        this.applicationEntity = applicationEntity;

        LOG.info("Application entity has been set...");
    }

    public ProtocolEntity getProtocolEntity() {
        return protocolEntity;
    }

    public void setProtocolEntity(ProtocolEntity protocolEntity) {
        this.protocolEntity = protocolEntity;

        LOG.info("Protocol entity has been set...");
    }

    public RegistryEntity getRegistryEntity() {
        return registryEntity;
    }

    public void setRegistryEntity(RegistryEntity registryEntity) {
        this.registryEntity = registryEntity;

        LOG.info("Registry entity has been set...");
    }

    public StrategyEntity getStrategyEntity() {
        return strategyEntity;
    }

    public void setStrategyEntity(StrategyEntity strategyEntity) {
        this.strategyEntity = strategyEntity;

        LOG.info("Strategy entity has been set...");
    }
    
    public MonitorEntity getMonitorEntity() {
        return monitorEntity;
    }

    public void setMonitorEntity(MonitorEntity monitorEntity) {
        this.monitorEntity = monitorEntity;
        
        LOG.info("Monitor entity has been set...");
    }
    
    public WebServiceEntity getWebServiceEntity() {
        return webServiceEntity;
    }

    public void setWebServiceEntity(WebServiceEntity webServiceEntity) {
        this.webServiceEntity = webServiceEntity;
        
        LOG.info("WebService entity has been set...");
    }
    
    public MQEntity getMQEntity() {        
        return mqEntity;
    }
    
    public void setMQEntity(MQEntity mqEntity) {
        this.mqEntity = mqEntity;
        
        LOG.info("MQ entity has been set...");
    }
    
    // 令牌控制器
    private SecurityBootstrap securityBootstrap;
    
    public SecurityBootstrap getSecurityBootstrap() {
        return securityBootstrap;
    }

    public void setSecurityBootstrap(SecurityBootstrap securityBootstrap) {
        this.securityBootstrap = securityBootstrap;
        
        LOG.info("Security bootstrap has been set...");
    }

    
    // 缓存Application config，从注册中心获得和持久化
    private ApplicationConfig applicationConfig;

    // 缓存服务端所有的Service config，key为interface，从注册中心获得和持久化
    private Map<String, ServiceConfig> serviceConfigMap = Maps.newConcurrentMap();

    // 缓存客户端所有的Reference config，key为interface，从注册中心获得和持久化
    private Map<String, ReferenceConfig> referenceConfigMap = Maps.newConcurrentMap();

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;

        LOG.info("Application config has been set...");
    }

    public Map<String, ServiceConfig> getServiceConfigMap() {
        return serviceConfigMap;
    }

    public Map<String, ReferenceConfig> getReferenceConfigMap() {
        return referenceConfigMap;
    }
}