package com.nepxion.thunder.registry;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.UserEntity;
import com.nepxion.thunder.common.invocation.MethodInvocation;
import com.nepxion.thunder.common.property.ThunderPropertiesExecutor;

/*
 * MethodInvocation用法：
 * MethodInvocation invocation = new MethodInvocation();
 * invocation.setMethodName("setName");
 * invocation.setParameterTypes(new Class<?>[] {String.class});
 * invocation.setParameters(new Object[] {"Zhangsan"});
 * invocation.invoke(object);
 */

public interface RegistryExecutor extends ThunderDelegate, ThunderPropertiesExecutor {

    // 设置注册中心初始化器
    void setRegistryInitializer(RegistryInitializer registryInitializer);

    // 设置协议实体
    void setProtocolEntity(ProtocolEntity protocolEntity);

    // 设置名称空间
    void setNamespace(String namespace);

    // 初始化注册中心Application相关环境
    void registerApplicationEnvironment(ApplicationEntity applicationEntity) throws Exception;

    // 初始化注册中心Configuration相关环境
    void registerConfigurationEnvironment() throws Exception;

    // 初始化注册中心Monitor相关环境
    void registerMonitorEnvironment() throws Exception;

    // 初始化注册中心User相关环境
    void registerUserEnvironment() throws Exception;

    // 注册Application
    void registerApplication(ApplicationEntity applicationEntity) throws Exception;

    // 注册Service目录
    void registerServiceCategory(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 注册Service，并把服务所在的应用信息写入
    void registerService(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 注册Reference目录
    void registerReferenceCategory(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 注册Reference，并把服务所在的应用信息写入
    void registerReference(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 注册Configuration
    void registerConfiguration(ApplicationEntity applicationEntity) throws Exception;

    // 注册Monitor
    void registerMonitor(String address) throws Exception;


    // 获取Application配置信息
    ApplicationConfig retrieveApplication(ApplicationEntity applicationEntity) throws Exception;

    // 获取Service配置信息
    ServiceConfig retrieveService(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 获取Reference配置信息
    ReferenceConfig retrieveReference(String interfaze, ApplicationEntity applicationEntity) throws Exception;


    // 持久化Application配置信息
    void persistApplication(ApplicationConfig applicationConfig) throws Exception;

    // 持久化Service配置信息
    void persistService(ServiceConfig serviceConfig, ApplicationEntity applicationEntity) throws Exception;

    // 持久化Reference配置信息
    void persistReference(ReferenceConfig referenceConfig, ApplicationEntity applicationEntity) throws Exception;


    // 获取服务实例列表
    List<ApplicationEntity> getServiceInstanceList(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 获取调用实例列表
    List<ApplicationEntity> getReferenceInstanceList(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 获取监控实例列表
    List<String> getMonitorInstanceList() throws Exception;

    // 判断服务实例是否Online
    boolean isServiceInstanceOnline(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 判断调用实例是否Online
    boolean isReferenceInstanceOnline(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 判断监控实例是否Online
    boolean isMonitorInstanceOnline(String monitorInstance) throws Exception;

    // 获取用户列表
    List<UserEntity> retrieveUserList() throws Exception;

    // 获得用户
    UserEntity retrieveUser(String name) throws Exception;

    // 持久化用户
    void persistUser(UserEntity userEntity) throws Exception;

    // 删除用户
    void deleteUser(UserEntity userEntity) throws Exception;


    // 监听Application配置信息变更
    void addApplicationConfigWatcher(ApplicationConfig applicationConfig) throws Exception;

    // 监听Service配置信息变更
    void addServiceConfigWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 监听Reference配置信息变更
    void addReferenceConfigWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 监听Service上下线
    void addServiceInstanceWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 监听Reference上下线
    void addReferenceInstanceWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 监听Monitor上下线，用来保持注册中心和本地缓存一致
    void addMonitorInstanceWatcher() throws Exception;

    // 监听注册中心应用与注册中心断开后重连成功后，触发事件
    void addReconnectionListener();


    // 获取Category名称列表
    List<String> getCategoryList() throws Exception;

    // 获取Protocol名称列表
    List<String> getProtocolList() throws Exception;

    // 获取Group名称列表
    List<String> getGroupList() throws Exception;

    // 获取Application名称列表
    List<String> getApplicationList(String group) throws Exception;

    // 获取Service名称列表
    List<String> getServiceList(String application, String group) throws Exception;

    // 获取Reference名称列表
    List<String> getReferenceList(String application, String group) throws Exception;

    // 获取Configuration Group名称列表
    List<String> getConfigurationGroupList() throws Exception;

    // 获取Configuration Application名称列表
    List<String> getConfigurationApplicationList(String group) throws Exception;


    // 重置ApplicationConfig，所有属性值恢复为默认值
    void resetApplication(ApplicationEntity applicationEntity) throws Exception;

    // 重置ServiceConfig，所有属性值恢复为默认值
    void resetService(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 重置ReferenceConfig，所有属性值恢复为默认值
    void resetReference(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 更改ApplicationConfig令牌刷新频率， ApplicationEntity只需要带application和group(下同)
    void modifyApplicationFrequency(ApplicationEntity applicationEntity, int frequency) throws Exception;

    // 更改ApplicationConfig单个属性
    void modifyApplication(ApplicationEntity applicationEntity, MethodInvocation invocation) throws Exception;

    // 更改ApplicationConfig批量属性
    void modifyApplication(ApplicationEntity applicationEntity, List<MethodInvocation> invocationList) throws Exception;

    // 更改ServiceConfig密钥
    void modifyServiceSecretKey(String interfaze, ApplicationEntity applicationEntity, String secretKey) throws Exception;

    // 更改ServiceConfig版本
    void modifyServiceVersion(String interfaze, ApplicationEntity applicationEntity, int version) throws Exception;

    // 更改ServiceConfig令牌数
    void modifyServiceToken(String interfaze, ApplicationEntity applicationEntity, long token) throws Exception;

    // 更改ServiceConfig单个属性
    void modifyService(String interfaze, ApplicationEntity applicationEntity, MethodInvocation invocation) throws Exception;

    // 更改ServiceConfig批量属性
    void modifyService(String interfaze, ApplicationEntity applicationEntity, List<MethodInvocation> invocationList) throws Exception;

    // 更改ReferenceConfig密钥
    void modifyReferenceSecretKey(String interfaze, ApplicationEntity applicationEntity, String secretKey) throws Exception;

    // 更改ServiceConfig版本
    void modifyReferenceVersion(String interfaze, ApplicationEntity applicationEntity, int version) throws Exception;

    // 更改ReferenceConfig单个属性
    void modifyReference(String interfaze, ApplicationEntity applicationEntity, MethodInvocation invocation) throws Exception;

    // 更改ReferenceConfig批量属性
    void modifyReference(String interfaze, ApplicationEntity applicationEntity, List<MethodInvocation> invocationList) throws Exception;
}