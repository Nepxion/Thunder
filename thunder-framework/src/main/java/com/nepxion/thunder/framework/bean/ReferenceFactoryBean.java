package com.nepxion.thunder.framework.bean;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;

import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.MethodEntity;
import com.nepxion.thunder.common.entity.MethodKey;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.ReferenceEntity;
import com.nepxion.thunder.common.util.ClassUtil;
import com.nepxion.thunder.framework.exception.FrameworkException;
import com.nepxion.thunder.framework.exception.FrameworkExceptionFactory;
import com.nepxion.thunder.protocol.ClientExecutor;
import com.nepxion.thunder.protocol.ClientExecutorAdapter;
import com.nepxion.thunder.protocol.ClientInterceptor;
import com.nepxion.thunder.protocol.ClientInterceptorAdapter;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.serialization.SerializerException;

@SuppressWarnings("all")
public class ReferenceFactoryBean extends AbstractFactoryBean implements BeanClassLoaderAware {
    private static final Logger LOG = LoggerFactory.getLogger(ReferenceFactoryBean.class);

    private Class<?> interfaze;
    private ClassLoader classLoader;
    private Object proxy;

    // 全局方法实体
    private MethodEntity methodEntity;
    private Map<MethodKey, MethodEntity> method;
    private ReferenceEntity referenceEntity;
    private ClientExecutor clientExecutor;
    private ClientExecutorAdapter clientExecutorAdapter;
    private ClientInterceptorAdapter clientInterceptorAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        cacheReference();

        registerReference();

        startClients();

        watchServiceInstance();

        initializeClientInterceptor();

        LOG.info("ReferenceFactoryBean has been initialized...");
    }

    // 缓存Reference
    protected void cacheReference() {
        String interfaceName = interfaze.getName();

        String namespace = properties.getString(ThunderConstant.NAMESPACE_ELEMENT_NAME);

        // 如果用户在Spring Xml未定义对应的Method，系统将自动创建全局方法的配置
        // 如果用户在Spring Xml只定义部分的Method，那么系统创建和用户自定义将Merge在一起，用户自定义的将覆盖系统自动创建的方法
        Map<MethodKey, MethodEntity> methodMap = ClassUtil.convertMethodMap(interfaze, methodEntity);
        Map<MethodKey, MethodEntity> cloneMethod = new HashMap<MethodKey, MethodEntity>();
        for (Map.Entry<MethodKey, MethodEntity> entry : method.entrySet()) {
            MethodKey methodKey = entry.getKey();
            MethodEntity methodEntity = entry.getValue();

            // 首先，判断用户的Method是否定义正确，即是否可以在接口类中找到对应的方法名
            MethodKey key = ClassUtil.getMethodKey(methodMap, methodKey.getMethod());
            if (key != null) {
                // 其次，判断用户的ParameterTypes是否定义正确
                // 在一个接口类不存在同名方法的前提下，ParameterTypes可以为空。如果不为空，必须跟接口类中的对应方法的参数匹配，否则抛错
                if (StringUtils.isNotEmpty(methodEntity.getParameterTypes())) {
                    boolean hasMethodKey = ClassUtil.hasMethodKey(methodMap, methodKey);
                    if (!hasMethodKey) {
                        throw FrameworkExceptionFactory.createMethodKeyNotFoundException(namespace, methodKey);
                    }
                } else {
                    // 再次，如果用户设置ParameterTypes为空，判断对应的方法是否存在同名方法，否则抛错
                    boolean duplicatedMethodKey = ClassUtil.duplicatedMethodKey(methodMap, methodKey.getMethod());
                    if (duplicatedMethodKey) {
                        throw FrameworkExceptionFactory.createMethodParameterTypesMissingException(namespace, methodKey);
                    }

                    // 然后，把接口类中的ParameterTypes覆盖到用户定义的MethodKey和MethodEntity
                    String parameterTypes = key.getParameterTypes();
                    methodKey.setParameterTypes(parameterTypes);
                    methodEntity.setParameterTypes(parameterTypes);
                }
            } else {
                // 用户定义的Method无法在接口类中找到
                throw FrameworkExceptionFactory.createMethodNameNotFoundException(namespace, methodKey);
            }

            cloneMethod.put(methodKey.clone(), methodEntity.clone());
        }

        // 最后，如果接口类中的方法，用户并没有定义，那么赋予它默认的同步调用方式，达到用户无需定义的目的
        for (Map.Entry<MethodKey, MethodEntity> entry : methodMap.entrySet()) {
            MethodKey methodKey = entry.getKey();
            MethodEntity methodEntity = entry.getValue();
            if (!cloneMethod.containsKey(methodKey)) {
                cloneMethod.put(methodKey, methodEntity);
            }
        }

        referenceEntity.setMethodMap(cloneMethod);

        Map<String, ReferenceEntity> referenceEntityMap = cacheContainer.getReferenceEntityMap();
        referenceEntityMap.put(interfaceName, referenceEntity);
    }

    // 注册Reference，并把服务的应用信息写入
    protected void registerReference() throws Exception {
        String interfaceName = interfaze.getName();

        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

        RegistryExecutor registryExecutor = executorContainer.getRegistryExecutor();

        // 注册Service目录，用于ServiceInstanceWatcher监听
        // 当注册中心未注册该Service，需要在Reference代替注册该Service，才能实现监听
        registryExecutor.registerServiceCategory(interfaceName, applicationEntity);

        // 持久化Service配置信息
        // 当Reference代替注册该Service的时候，需要给给默认值，但method方法列表为空
        ServiceConfig serviceConfig = null;
        try {
            serviceConfig = registryExecutor.retrieveService(interfaceName, applicationEntity);
        } catch (SerializerException e) {
            LOG.warn("ServiceConfig class was upgraded, Registry Center will initialize it again");
        }
        if (serviceConfig == null) {
            serviceConfig = new ServiceConfig();
            serviceConfig.setInterface(interfaceName);
            serviceConfig.setSecretKey(properties.getString(ThunderConstant.SECRET_KEY_ATTRIBUTE_NAME));
            serviceConfig.setVersion(properties.getInteger(ThunderConstant.VERSION_ATTRIBUTE_NAME));
            serviceConfig.setToken(properties.getLong(ThunderConstant.TOKEN_ATTRIBUTE_NAME));

            registryExecutor.persistService(serviceConfig, applicationEntity);
        }

        // 注册Reference
        registryExecutor.registerReference(interfaceName, applicationEntity);

        // 持久化Reference配置信息
        ReferenceConfig referenceConfig = null;
        try {
            referenceConfig = registryExecutor.retrieveReference(interfaceName, applicationEntity);
        } catch (SerializerException e) {
            LOG.warn("ReferenceConfig class was upgraded, Registry Center will initialize it again");
        }
        if (referenceConfig == null) {
            referenceConfig = new ReferenceConfig();
            referenceConfig.setInterface(interfaceName);
            referenceConfig.setSecretKey(properties.getString(ThunderConstant.SECRET_KEY_ATTRIBUTE_NAME));
            referenceConfig.setVersion(properties.getInteger(ThunderConstant.VERSION_ATTRIBUTE_NAME));

            registryExecutor.persistReference(referenceConfig, applicationEntity);
        }

        Map<String, ReferenceConfig> referenceConfigMap = cacheContainer.getReferenceConfigMap();
        referenceConfigMap.put(interfaceName, referenceConfig);

        // Reference配置信息更改后通知客户端，包括密钥
        registryExecutor.addReferenceConfigWatcher(interfaceName, applicationEntity);
    }

    // 找到所有可连的应用
    protected List<ApplicationEntity> retrieveServiceInstanceList() throws Exception {
        String interfaceName = interfaze.getName();

        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

        RegistryExecutor registryExecutor = executorContainer.getRegistryExecutor();

        return registryExecutor.getServiceInstanceList(interfaceName, applicationEntity);
    }

    // 启动客户端集群
    protected void startClients() throws Exception {
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        ProtocolType protocolType = protocolEntity.getType();
        // 负载均衡模式下，启动多客户端，做负载均衡
        // 非负载均衡模式下(一般是消息队列模式)，只需启动一个客户端
        if (protocolType.isLoadBalanceSupported()) {
            List<ApplicationEntity> serviceInstanceList = retrieveServiceInstanceList();
            if (CollectionUtils.isNotEmpty(serviceInstanceList)) {
                for (ApplicationEntity applicationEntity : serviceInstanceList) {
                    startClient(applicationEntity);
                }
            } else {
                LOG.warn("Service instance [" + interfaze + "] can't be retrieved at Registry Center");
            }
        } else {
            ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();
            startClient(applicationEntity);
        }
    }

    // 启动客户端
    protected void startClient(ApplicationEntity applicationEntity) throws Exception {
        String interfaceName = interfaze.getName();

        try {
            clientExecutor.start(interfaceName, applicationEntity);
        } catch (Exception e) {
            LOG.error("Start client failed", e);
        }
    }

    // 监听Service上下线，用来保持注册中心和本地缓存一致
    protected void watchServiceInstance() throws Exception {
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        ProtocolType protocolType = protocolEntity.getType();
        if (!protocolType.isLoadBalanceSupported()) {
            return;
        }

        String interfaceName = interfaze.getName();

        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

        RegistryExecutor registryExecutor = executorContainer.getRegistryExecutor();
        registryExecutor.addServiceInstanceWatcher(interfaceName, applicationEntity);
    }

    // Spring AOP 面向切面的动态代理反射
    protected void initializeClientInterceptor() throws Exception {
        ClientInterceptor clientInterceptor = createClientInterceptor();

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setOptimize(false);
        proxyFactory.addInterface(interfaze);
        proxyFactory.addAdvice(clientInterceptor);
        proxy = proxyFactory.getProxy(classLoader);
    }

    // 创建反射
    protected ClientInterceptor createClientInterceptor() throws Exception {
        String interfaceName = interfaze.getName();

        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        String clientInterceptorId = protocolEntity.getClientInterceptorId();

        ClientInterceptor clientInterceptor = null;
        try {
            clientInterceptor = createDelegate(clientInterceptorId);
        } catch (Exception e) {
            throw new FrameworkException("Creat ClientInterceptor failed", e);
        }

        clientInterceptor.setInterface(interfaceName);

        return clientInterceptor;
    }

    @Override
    public Object getObject() throws Exception {
        return proxy;
    }

    @Override
    public Class getObjectType() {
        return interfaze;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setInterface(Class<?> interfaze) {
        this.interfaze = interfaze;
    }

    public Class<?> getInterface() {
        return interfaze;
    }

    public void setMethodEntity(MethodEntity methodEntity) {
        this.methodEntity = methodEntity;
    }

    public MethodEntity getMethodEntity() {
        return methodEntity;
    }

    public void setMethod(Map<MethodKey, MethodEntity> method) {
        this.method = method;
    }

    public Map<MethodKey, MethodEntity> getMethod() {
        return method;
    }

    public void setReferenceEntity(ReferenceEntity referenceEntity) {
        this.referenceEntity = referenceEntity;
    }

    public ReferenceEntity getReferenceEntity() {
        return referenceEntity;
    }

    public void setClientExecutor(ClientExecutor clientExecutor) {
        this.clientExecutor = clientExecutor;
    }

    public ClientExecutor getClientExecutor() {
        return clientExecutor;
    }

    public void setClientExecutorAdapter(ClientExecutorAdapter clientExecutorAdapter) {
        this.clientExecutorAdapter = clientExecutorAdapter;
    }

    public ClientExecutorAdapter getClientExecutorAdapter() {
        return clientExecutorAdapter;
    }

    public void setClientInterceptorAdapter(ClientInterceptorAdapter clientInterceptorAdapter) {
        this.clientInterceptorAdapter = clientInterceptorAdapter;
    }

    public ClientInterceptorAdapter getClientInterceptorAdapter() {
        return clientInterceptorAdapter;
    }
}