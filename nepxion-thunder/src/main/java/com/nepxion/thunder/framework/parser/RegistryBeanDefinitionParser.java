package com.nepxion.thunder.framework.parser;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.PropertyType;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.common.entity.RegistryType;
import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.common.property.ThunderPropertiesExecutor;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;
import com.nepxion.thunder.event.protocol.ProtocolEventFactory;
import com.nepxion.thunder.event.smtp.SmtpEventFactory;
import com.nepxion.thunder.framework.bean.RegistryFactoryBean;
import com.nepxion.thunder.framework.exception.FrameworkException;
import com.nepxion.thunder.protocol.redis.cluster.RedisClusterFactory;
import com.nepxion.thunder.protocol.redis.sentinel.RedisSentinelPoolFactory;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.registry.RegistryInitializer;
import com.nepxion.thunder.serialization.SerializerException;
import com.nepxion.thunder.serialization.SerializerFactory;
import com.nepxion.thunder.serialization.compression.CompressorFactory;

public class RegistryBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(RegistryBeanDefinitionParser.class);
    
    public RegistryBeanDefinitionParser(ThunderDelegate delegate) {
        super(delegate);
    }
    
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        
        String typeAttributeName = ThunderConstants.TYPE_ATTRIBUTE_NAME;
        String addressAttributeName = ThunderConstants.ADDRESS_ATTRIBUTE_NAME;
        String addressParameterName = ThunderConstants.REGISTRY_ADDRESS_PARAMETER_NAME;
        String configAttributeName = ThunderConstants.CONFIG_ATTRIBUTE_NAME;
        
        String type = element.getAttribute(typeAttributeName);
        RegistryType registryType = null;
        if (StringUtils.isNotEmpty(type)) {
            registryType = RegistryType.fromString(type);
        } else {
            registryType = RegistryType.ZOOKEEPER;
        }
        
        LOG.info("Registry type is {}", registryType);
        
        String config = element.getAttribute(configAttributeName);
        PropertyType propertyType = null;
        if (StringUtils.isNotEmpty(config)) {
            propertyType = PropertyType.fromString(config);
        } else {
            propertyType = PropertyType.REMOTE;
        }
        
        LOG.info("Property type is {}", propertyType);
        
        // 在开启远程配置的前提下，先从外部参数化平台获取远程配置，如果失败，再从注册中心获取
        initializeConfiguration(propertyType);
        
        // 通过-DThunderRegistryAddress获取值
        String address = System.getProperty(addressParameterName);
        if (StringUtils.isEmpty(address)) {
            address = element.getAttribute(addressAttributeName);
            if (StringUtils.isEmpty(address)) {
                try {
                    address = properties.getString(ThunderConstants.ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME);
                } catch (Exception e) {

                }
                if (StringUtils.isEmpty(address)) {
                    throw new FrameworkException("Registry address is null");
                }
            }
        }
        
        LOG.info("Registry address is {}", address);
        
        RegistryEntity registryEntity = new RegistryEntity();
        registryEntity.setType(registryType);
        registryEntity.setAddress(address);
        registryEntity.setPropertyType(propertyType);
                
        cacheContainer.setRegistryEntity(registryEntity);
        builder.addPropertyValue(createBeanName(RegistryEntity.class), registryEntity);

        RegistryInitializer registryInitializer = createRegistryInitializer(registryType);
        builder.addPropertyValue(createBeanName(RegistryInitializer.class), registryInitializer);
        
        RegistryExecutor registryExecutor = createRegistryExecutor(registryType);
        builder.addPropertyValue(createBeanName(RegistryExecutor.class), registryExecutor);
        
        try {
            initializeRegistry(registryInitializer, registryExecutor, registryEntity);
        } catch (Exception e) {
            LOG.error("Initialize registry center failed", e);
        }
        
        try {
            initializeConfiguration(registryExecutor, propertyType);
        } catch (Exception e) {
            LOG.error("Initialize configuration failed", e);
        }
        
        initializeFactory();
        
        try {
            initializeEnvironment(registryExecutor);
            
            initializeApplication(registryExecutor);
            
            LOG.info("Connect and register to Registry Center successfully, address={}", registryEntity.getAddress());
        } catch (Exception e) {
            LOG.error("Connect and register to Registry Center failed", e);
        }
    }
    
    protected RegistryInitializer createRegistryInitializer(RegistryType registryType) {
        RegistryInitializer registryInitializer = executorContainer.getRegistryInitializer();
        if (registryInitializer == null) {
            String zookeeperRegistryInitializerId = ThunderConstants.ZOOKEEPER_REGISTRY_INITIALIZER_ID;
            try {
                switch (registryType) {
                    case ZOOKEEPER:
                        registryInitializer = createDelegate(zookeeperRegistryInitializerId);
                        break;
                }
            } catch (Exception e) {
                throw new FrameworkException("Creat RegistryInitializer failed", e);
            }
            
            executorContainer.setRegistryInitializer(registryInitializer);
        }
        
        return registryInitializer;
    }
    
    protected RegistryExecutor createRegistryExecutor(RegistryType registryType) {
        RegistryExecutor registryExecutor = executorContainer.getRegistryExecutor();
        if (registryExecutor == null) {
            String zookeeperRegistryExecutorId = ThunderConstants.ZOOKEEPER_REGISTRY_EXECUTOR_ID;
            try {
                switch (registryType) {
                    case ZOOKEEPER:
                        registryExecutor = createDelegate(zookeeperRegistryExecutorId);
                        break;
                }
            } catch (Exception e) {
                throw new FrameworkException("Creat RegistryExecutor failed", e);
            }
            
            executorContainer.setRegistryExecutor(registryExecutor);
        }
        
        return registryExecutor;
    }
    
    // 初始化注册中心连接
    protected void initializeRegistry(RegistryInitializer registryInitializer, RegistryExecutor registryExecutor, RegistryEntity registryEntity) throws Exception {
        // 启动和注册中心的连接
        registryInitializer.start(registryEntity);
        registryExecutor.setRegistryInitializer(registryInitializer);
    }
    
    // 初始化远程配置信息，通过外部参数化平台的实现类(SPI方式)
    protected void initializeConfiguration(PropertyType propertyType) {
        if (propertyType != PropertyType.REMOTE) {
            return;
        }
        
        try {
            ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

            ThunderPropertiesExecutor propertiesExecutor = ThunderPropertiesManager.initializePropertiesExecutor();

            ThunderPropertiesManager.initializeRemoteProperties(propertiesExecutor, applicationEntity);
        } catch (Exception e) {

        }
    }
    
    // 初始化远程配置信息，通过注册中心的参数化平台存储基地
    protected void initializeConfiguration(RegistryExecutor registryExecutor, PropertyType propertyType) throws Exception {
        if (propertyType != PropertyType.REMOTE) {
            return;
        }
        
        // 如果已经从外部参数化平台初始化过RemoteProperties，就不再从注册中心读取远程配置了
        if (ThunderPropertiesManager.getRemoteProperties() != null) {
            return;
        }
        
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        // 初始化注册中心Configuration相关环境
        registryExecutor.registerConfigurationEnvironment();
        
        // 注册Configuration
        registryExecutor.registerConfiguration(applicationEntity);
        
        // 初始化注册中心的Configuration下远程配置
        ThunderPropertiesManager.initializeRemoteProperties(registryExecutor, applicationEntity);
    }
    
    // 初始化工厂
    protected void initializeFactory() {
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        SerializerFactory.initialize(properties);
        CompressorFactory.initialize(properties);
        ProtocolEventFactory.initialize(properties);
        SmtpEventFactory.initialize(properties);
        RedisSentinelPoolFactory.initialize(properties);
        RedisClusterFactory.initialize(properties);
    }
    
    // 初始化注册环境
    protected void initializeEnvironment(RegistryExecutor registryExecutor) throws Exception {
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();

        // 设置上下文对象
        registryExecutor.setProtocolEntity(protocolEntity);
        
        // 初始化注册中心Application相关环境
        registryExecutor.registerApplicationEnvironment(applicationEntity);
        
        // 初始化注册中心Monitor相关环境
        registryExecutor.registerMonitorEnvironment();
        
        // 初始化注册中心User相关环境
        registryExecutor.registerUserEnvironment();
    }
    
    // 初始化应用
    protected void initializeApplication(RegistryExecutor registryExecutor) throws Exception {
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
        
        // 注册Application
        registryExecutor.registerApplication(applicationEntity);

        // 持久化Application配置信息
        ApplicationConfig applicationConfig = null;
        try {
            applicationConfig = registryExecutor.retrieveApplication(applicationEntity);
        } catch (SerializerException e) {
            LOG.warn("ApplicationConfig class was upgraded, Registry Center will initialize it again");
        }
        if (applicationConfig == null) {
            applicationConfig = new ApplicationConfig();
            applicationConfig.setApplication(applicationEntity.getApplication());
            applicationConfig.setGroup(applicationEntity.getGroup());
            applicationConfig.setFrequency(properties.getInteger(ThunderConstants.FREQUENCY_ATTRIBUTE_NAME));
            
            registryExecutor.persistApplication(applicationConfig);
        }
        cacheContainer.setApplicationConfig(applicationConfig);
        
        // Application配置信息更改后通知服务端和客户端
        registryExecutor.addApplicationConfigWatcher(applicationConfig);
        
        // 应用与注册中心断开后重连成功，应该重新写入应用信息
        registryExecutor.addReconnectionListener();
    }
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return RegistryFactoryBean.class;
    }
}