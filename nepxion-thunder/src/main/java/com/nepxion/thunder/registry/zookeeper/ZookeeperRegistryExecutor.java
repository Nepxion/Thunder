package com.nepxion.thunder.registry.zookeeper;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.UserEntity;
import com.nepxion.thunder.common.entity.UserFactory;
import com.nepxion.thunder.common.entity.UserOperation;
import com.nepxion.thunder.common.entity.UserType;
import com.nepxion.thunder.common.invocation.MethodInvocation;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.registry.RegistryInitializer;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperException;
import com.nepxion.thunder.registry.zookeeper.common.ZookeeperInvoker;

public class ZookeeperRegistryExecutor extends ThunderDelegateImpl implements RegistryExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperRegistryExecutor.class);

    private ZookeeperInvoker invoker;
    private CuratorFramework client;
    
    private ProtocolEntity protocolEntity;
    private String namespace;
    
    public ZookeeperRegistryExecutor() {
        this(null, null, null);
    }
    
    public ZookeeperRegistryExecutor(ZookeeperRegistryInitializer initializer, ProtocolEntity protocolEntity) {
        this(initializer, protocolEntity, null);
    }
        
    public ZookeeperRegistryExecutor(ZookeeperRegistryInitializer initializer, ProtocolEntity protocolEntity, String namespace) {
        setRegistryInitializer(initializer);
        setProtocolEntity(protocolEntity);
        setNamespace(namespace);
    }
    
    @Override
    public void setRegistryInitializer(RegistryInitializer registryInitializer) {
        if (registryInitializer == null) {
            return;
        }
        
        ZookeeperRegistryInitializer initializer = (ZookeeperRegistryInitializer) registryInitializer;
        this.invoker = initializer.getInvoker();
        this.client = initializer.getClient();
    }
    
    @Override 
    public void setProtocolEntity(ProtocolEntity protocolEntity) {
        this.protocolEntity = protocolEntity;
    }
    
    @Override 
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public ZookeeperInvoker getInvoker() {
        return invoker;
    }
    
    public CuratorFramework getClient() {
        return client;
    }
    
    @Override
    public void registerApplicationEnvironment(ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createApplicationCategoryPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            LOG.info("Register application environment [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    }
    
    @Override
    public void registerConfigurationEnvironment() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        StringBuilder builder = createConfigurationCategoryPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            LOG.info("Register configuration environment [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    } 
    
    @Override
    public void registerMonitorEnvironment() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        StringBuilder builder = createMonitorCategoryPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            LOG.info("Register monitor environment [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    }
    
    @Override
    public void registerUserEnvironment() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        StringBuilder builder = createUserCategoryPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            LOG.info("Register user environment [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    }

    @Override
    public void registerApplication(ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createApplicationPath(application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            LOG.info("Register application [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    }
    
    @Override
    public void registerServiceCategory(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createServiceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            LOG.info("Register service category [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    }
    
    @Override
    public void registerService(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createServiceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
        
        List<String> childPathList = invoker.getChildPathList(client, path);
        for (String childPath : childPathList) {
            String applicationJson = childPath.substring(childPath.lastIndexOf("/") + 1);
            ApplicationEntity entity = ZookeeperApplicationEntityFactory.fromJson(applicationJson);
            if (entity.equals(applicationEntity)) {
                LOG.info("Delete expired service [{}]", childPath);
                if (invoker.pathExist(client, childPath)) {
                    invoker.deletePath(client, childPath);
                }
            }
        }

        builder.append("/");
        builder.append(ZookeeperApplicationEntityFactory.toJson(applicationEntity));
        path = builder.toString();
        
        LOG.info("Register service [{}]", path);
        invoker.createPath(client, path, CreateMode.EPHEMERAL);
    }
    
    @Override
    public void registerReferenceCategory(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createReferenceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            LOG.info("Register reference category [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    }

    @Override
    public void registerReference(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createReferenceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
        
        List<String> childPathList = invoker.getChildPathList(client, path);
        for (String childPath : childPathList) {
            String applicationJson = childPath.substring(childPath.lastIndexOf("/") + 1);
            ApplicationEntity entity = ZookeeperApplicationEntityFactory.fromJson(applicationJson);
            if (entity.equals(applicationEntity)) {
                LOG.info("Delete expired reference [{}]", childPath);
                if (invoker.pathExist(client, childPath)) {
                    invoker.deletePath(client, childPath);
                }
            }
        }
        
        builder.append("/");
        builder.append(ZookeeperApplicationEntityFactory.toJson(applicationEntity));
        path = builder.toString();

        LOG.info("Register reference [{}]", path);
        invoker.createPath(client, path, CreateMode.EPHEMERAL);
    }
    
    @Override
    public void registerConfiguration(ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createConfigurationApplicationPath(application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            LOG.info("Register configuration [{}]", path);
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }
    }
    
    @Override
    public void registerMonitor(String address) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
        
        StringBuilder builder = createMonitorCategoryPath();
        builder.append("/");
        builder.append(address);
        String path = builder.toString();
        
        if (invoker.pathExist(client, path)) {
            LOG.info("Delete expired monitor [{}]", path);
            invoker.deletePath(client, path);
        }

        LOG.info("Register monitor [{}]", path);
        invoker.createPath(client, path, CreateMode.EPHEMERAL);
    }

    @Override
    public ApplicationConfig retrieveApplication(ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createApplicationPath(application, group);
        String path = builder.toString();
        
        byte[] data = invoker.getData(client, path);
        if (ArrayUtils.isNotEmpty(data)) {
            LOG.info("Retrieved application config [{}]", path);

            ApplicationConfig applicationConfig = invoker.getObject(data, ApplicationConfig.class);

            return applicationConfig;
        }

        return null;
    }

    @Override
    public ServiceConfig retrieveService(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createServiceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        byte[] data = invoker.getData(client, path);
        if (ArrayUtils.isNotEmpty(data)) {
            LOG.info("Retrieved service config [{}]", path);

            ServiceConfig serviceConfig = invoker.getObject(data, ServiceConfig.class);

            return serviceConfig;
        }

        return null;
    }

    @Override
    public ReferenceConfig retrieveReference(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createReferenceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        byte[] data = invoker.getData(client, path);
        if (ArrayUtils.isNotEmpty(data)) {
            LOG.info("Retrieved reference config [{}]", path);

            ReferenceConfig referenceConfig = invoker.getObject(data, ReferenceConfig.class);

            return referenceConfig;
        }

        return null;
    }
    
    @Override
    public String retrieveProperty(ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createConfigurationApplicationPath(application, group);
        String path = builder.toString();
        
        byte[] data = invoker.getData(client, path);
        if (ArrayUtils.isNotEmpty(data)) {
            LOG.info("Retrieved property [{}]", path);

            String property = new String(data, ThunderConstants.ENCODING_FORMAT);

            return property;
        }

        return null;
    }

    @Override
    public void persistApplication(ApplicationConfig applicationConfig) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationConfig.getApplication();
        String group = applicationConfig.getGroup();
        
        StringBuilder builder = createApplicationPath(application, group);
        String path = builder.toString();
        
        LOG.info("Persist application config [{}]", path);
        invoker.setData(client, path, applicationConfig);
    }

    @Override
    public void persistService(ServiceConfig serviceConfig, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        String interfaze = serviceConfig.getInterface();

        StringBuilder builder = createServiceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        LOG.info("Persist service config [{}]", path);
        invoker.setData(client, path, serviceConfig);
    }

    @Override
    public void persistReference(ReferenceConfig referenceConfig, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        String interfaze = referenceConfig.getInterface();
        
        StringBuilder builder = createReferenceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        LOG.info("Persist reference config [{}]", path);
        invoker.setData(client, path, referenceConfig);
    }
    
    @Override
    public void persistProperty(String property, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        StringBuilder builder = createConfigurationApplicationPath(application, group);
        String path = builder.toString();
        
        LOG.info("Persist property [{}]", path);
        invoker.setData(client, path, property.getBytes());
    }

    @Override
    public List<ApplicationEntity> getServiceInstanceList(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createServiceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        List<String> applicationJsonList = invoker.getChildNameList(client, path);
        List<ApplicationEntity> applicationEntityList = ZookeeperApplicationEntityFactory.fromJson(applicationJsonList);

        return applicationEntityList;
    }
    
    @Override
    public List<ApplicationEntity> getReferenceInstanceList(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createReferenceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        List<String> applicationJsonList = invoker.getChildNameList(client, path);
        List<ApplicationEntity> applicationEntityList = ZookeeperApplicationEntityFactory.fromJson(applicationJsonList);
        
        return applicationEntityList;
    }
    
    @Override
    public List<String> getMonitorInstanceList() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createMonitorCategoryPath();
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        List<String> monitorList = invoker.getChildNameList(client, path);
        
        return monitorList;
    }
    
    @Override
    public boolean isServiceInstanceOnline(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createServiceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        List<String> applicationJsonList = invoker.getChildNameList(client, path);
        List<ApplicationEntity> applicationEntityList = ZookeeperApplicationEntityFactory.fromJson(applicationJsonList);

        return applicationEntityList.contains(applicationEntity);
    }

    @Override
    public boolean isReferenceInstanceOnline(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        StringBuilder builder = createReferenceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        List<String> applicationJsonList = invoker.getChildNameList(client, path);
        List<ApplicationEntity> applicationEntityList = ZookeeperApplicationEntityFactory.fromJson(applicationJsonList);

        return applicationEntityList.contains(applicationEntity);
    }
    
    @Override
    public boolean isMonitorInstanceOnline(String monitorInstance) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createMonitorCategoryPath();
        builder.append("/");
        builder.append(monitorInstance);
        String path = builder.toString();
        
        return invoker.pathExist(client, path);
    }
    
    @Override
    public List<UserEntity> retrieveUserList() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createUserCategoryPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        LOG.info("Get user list [{}]", path);

        List<UserEntity> userEntityList = new ArrayList<UserEntity>();
        List<String> childPathList = invoker.getChildPathList(client, path);
        for (String childPath : childPathList) {
            byte[] data = invoker.getData(client, childPath);
            if (ArrayUtils.isNotEmpty(data)) {
                UserEntity userEntity = invoker.getObject(data, UserEntity.class);
                userEntityList.add(userEntity);
            }
        }
        
        // 超级管理员排在第一位，管理员用户次之，一般用户排最后
        Collections.sort(userEntityList, new Comparator<UserEntity>() {
            @Override
            public int compare(UserEntity userEntity1, UserEntity userEntity2) {
                String name1 = userEntity1.getName();
                String name2 = userEntity2.getName();
                
                UserType userType1 = userEntity1.getType();
                UserType userType2 = userEntity2.getType();
                
                int value1 = UserFactory.getUserCompareValue(userType1);
                int value2 = UserFactory.getUserCompareValue(userType2);
                
                if (value1 > value2) {
                    return 1;
                }

                if (value1 == value2) {
                    return name1.compareToIgnoreCase(name2);
                }

                return -1;
            }
        });

        return userEntityList;
    }
    
    @Override
    public UserEntity retrieveUser(String name) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createUserPath(name);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        byte[] data = invoker.getData(client, path);
        if (ArrayUtils.isNotEmpty(data)) {
            LOG.info("Retrieved user [{}]", path);

            UserEntity userEntity = invoker.getObject(data, UserEntity.class);

            return userEntity;
        }
        
        return null;
    }

    @Override
    public void persistUser(UserEntity userEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String name = userEntity.getName();
        UserType type = userEntity.getType();
        List<UserOperation> operations = userEntity.getOperations();
        
        if ((StringUtils.equals(name, ThunderConstants.USER_ADMIN_NAME) && type != UserType.ADMINISTRATOR) || (!StringUtils.equals(name, ThunderConstants.USER_ADMIN_NAME) && type == UserType.ADMINISTRATOR)) {
            throw new ZookeeperException("User can't be persisted, because name=" + name + ", type=" + type);
        }
        
        if (type == UserType.USER && operations.contains(UserOperation.USER_CONTROL)) {
            throw new ZookeeperException("User type [" + UserType.USER + "] has no permission for " + UserOperation.USER_CONTROL);
        }
        
        StringBuilder builder = createUserCategoryPath();
        builder.append("/");
        builder.append(name);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            invoker.createPath(client, path, CreateMode.PERSISTENT);
        }

        LOG.info("Persist user [{}]", path);
        invoker.setData(client, path, userEntity);
    }

    @Override
    public void deleteUser(UserEntity userEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String name = userEntity.getName();
        if (StringUtils.equals(name, ThunderConstants.USER_ADMIN_NAME)) {
            throw new ZookeeperException("Administrator [" + name + "] can't be deleted");
        }
        
        StringBuilder builder = createUserCategoryPath();
        builder.append("/");
        builder.append(name);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("User [" + name + "] doesn't exist");
        }

        LOG.info("Delete user [{}]", path);
        invoker.deletePath(client, path);
    }
    
    public void addUserWatcher(UserEntity userEntity, ZookeeperUserWatcherCallback<UserEntity> callback) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String name = userEntity.getName(); 
        
        StringBuilder builder = createUserCategoryPath();
        builder.append("/");
        builder.append(name);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("User [" + name + "] doesn't exist");
        }

        LOG.info("Add user watcher [{}]", path);

        new ZookeeperUserWatcher(client, invoker, callback, path);
    }

    @Override
    public void addApplicationConfigWatcher(ApplicationConfig applicationConfig) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationConfig.getApplication();
        String group = applicationConfig.getGroup();

        StringBuilder builder = createApplicationPath(application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        LOG.info("Add application config watcher [{}]", path);

        new ZookeeperApplicationConfigWatcher(client, invoker, cacheContainer, path);
    }
    
    @Override
    public void addServiceConfigWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createServiceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        LOG.info("Add service config watcher [{}]", path);

        new ZookeeperServiceConfigWatcher(client, interfaze, invoker, cacheContainer, path);
    }

    @Override
    public void addReferenceConfigWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createReferenceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        LOG.info("Add reference config watcher [{}]", path);

        new ZookeeperReferenceConfigWatcher(client, interfaze, invoker, cacheContainer, path);
	}

    @Override
    public void addServiceInstanceWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
                
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createServiceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        LOG.info("Add service instance watcher [{}]", path);

        new ZookeeperInstanceWatcher(client, invoker, executorContainer, ApplicationType.SERVICE, interfaze, path);
    }
    
    @Override
    public void addReferenceInstanceWatcher(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }
                
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        StringBuilder builder = createReferenceInterfacePath(interfaze, application, group);
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }

        LOG.info("Add reference instance watcher [{}]", path);

        new ZookeeperInstanceWatcher(client, invoker, executorContainer, ApplicationType.REFERENCE, interfaze, path);
    }

    @Override
    public void addMonitorInstanceWatcher() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createMonitorCategoryPath();
        String path = builder.toString();
        
        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        LOG.info("Add monitor instance watcher [{}]", path);

        new ZookeeperMonitorInstanceWatcher(client, invoker, cacheContainer, path);
    }

    @Override
    public void addReconnectionListener() {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        client.getConnectionStateListenable().addListener(new ZookeeperReconnectionListener(this), Executors.newFixedThreadPool(1));
    }
    
    @Override
    public List<String> getCategoryList() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createRootPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        List<String> childNameList = invoker.getChildNameList(client, path);
        
        return childNameList;
    }
    
    @Override
    public List<String> getProtocolList() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createApplicationCategoryPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        List<String> childNameList = invoker.getChildNameList(client, path);
        
        return childNameList;
    }
    
    @Override
    public List<String> getGroupList() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createProtocolCategoryPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        List<String> childNameList = invoker.getChildNameList(client, path);
        
        return childNameList;
    }

    @Override
    public List<String> getApplicationList(String group) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createGroupPath(group);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        List<String> childNameList = invoker.getChildNameList(client, path);
        
        return childNameList;
    }

    @Override
    public List<String> getServiceList(String application, String group) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createServiceCategoryPath(application, group);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        List<String> childNameList = invoker.getChildNameList(client, path);
        
        return childNameList;
    }

    @Override
    public List<String> getReferenceList(String application, String group) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createReferenceCategoryPath(application, group);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        List<String> childNameList = invoker.getChildNameList(client, path);
        
        return childNameList;
    }

    @Override
    public List<String> getConfigurationGroupList() throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createConfigurationCategoryPath();
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        List<String> childNameList = invoker.getChildNameList(client, path);
        
        return childNameList;
    }

    @Override
    public List<String> getConfigurationApplicationList(String group) throws Exception {
        if (client == null) {
            throw new ZookeeperException("Zookeeper client is null");
        }

        StringBuilder builder = createConfigurationGroupPath(group);
        String path = builder.toString();

        if (!invoker.pathExist(client, path)) {
            throw new ZookeeperException("Path [" + path + "] doesn't exist");
        }
        
        List<String> childNameList = invoker.getChildNameList(client, path);
        
        return childNameList;
    }
    
    @Override
    public void resetApplication(ApplicationEntity applicationEntity) throws Exception {  
        MethodInvocation frequencyInvocation = new MethodInvocation();
        frequencyInvocation.setMethodName("setFrequency");
        frequencyInvocation.setParameterTypes(new Class<?>[] {int.class});
        frequencyInvocation.setParameters(new Object[] {properties.getInteger(ThunderConstants.FREQUENCY_ATTRIBUTE_NAME)});

        modifyApplication(applicationEntity, frequencyInvocation);
    }
    
    @Override
    public void resetService(String interfaze, ApplicationEntity applicationEntity) throws Exception {                                
        MethodInvocation scretKeyInvocation = new MethodInvocation();
        scretKeyInvocation.setMethodName("setSecretKey");
        scretKeyInvocation.setParameterTypes(new Class<?>[] {String.class});
        scretKeyInvocation.setParameters(new Object[] {properties.getString(ThunderConstants.SECRET_KEY_ATTRIBUTE_NAME)});
        
        MethodInvocation versionInvocation = new MethodInvocation();
        versionInvocation.setMethodName("setVersion");
        versionInvocation.setParameterTypes(new Class<?>[] {int.class});
        versionInvocation.setParameters(new Object[] {properties.getInteger(ThunderConstants.VERSION_ATTRIBUTE_NAME)});
        
        MethodInvocation tokenInvocation = new MethodInvocation();
        tokenInvocation.setMethodName("setToken");
        tokenInvocation.setParameterTypes(new Class<?>[] {long.class});
        tokenInvocation.setParameters(new Object[] {properties.getLong(ThunderConstants.TOKEN_ATTRIBUTE_NAME)});        

        List<MethodInvocation> serviceInvocationList = new ArrayList<MethodInvocation>();
        serviceInvocationList.add(scretKeyInvocation);
        serviceInvocationList.add(versionInvocation);
        serviceInvocationList.add(tokenInvocation);
        
        modifyService(interfaze, applicationEntity, serviceInvocationList);
    }
    
    @Override
    public void resetReference(String interfaze, ApplicationEntity applicationEntity) throws Exception {                                
        MethodInvocation scretKeyInvocation = new MethodInvocation();
        scretKeyInvocation.setMethodName("setSecretKey");
        scretKeyInvocation.setParameterTypes(new Class<?>[] {String.class});
        scretKeyInvocation.setParameters(new Object[] {properties.getString(ThunderConstants.SECRET_KEY_ATTRIBUTE_NAME)});
        
        MethodInvocation versionInvocation = new MethodInvocation();
        versionInvocation.setMethodName("setVersion");
        versionInvocation.setParameterTypes(new Class<?>[] {int.class});
        versionInvocation.setParameters(new Object[] {properties.getInteger(ThunderConstants.VERSION_ATTRIBUTE_NAME)});
        
        List<MethodInvocation> referenceInvocationList = new ArrayList<MethodInvocation>();
        referenceInvocationList.add(scretKeyInvocation);
        referenceInvocationList.add(versionInvocation);
        
        modifyReference(interfaze, applicationEntity, referenceInvocationList);
    }
    
    @Override
    public void modifyApplicationFrequency(ApplicationEntity applicationEntity, int frequency) throws Exception {
        MethodInvocation frequencyInvocation = new MethodInvocation();
        frequencyInvocation.setMethodName("setFrequency");
        frequencyInvocation.setParameterTypes(new Class<?>[] {int.class});
        frequencyInvocation.setParameters(new Object[] {frequency});
        
        modifyApplication(applicationEntity, frequencyInvocation);
    }
    
    @Override
    public void modifyApplication(ApplicationEntity applicationEntity, MethodInvocation invocation) throws Exception {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        ApplicationConfig applicationConfig = retrieveApplication(applicationEntity);
        if (applicationConfig == null) {
            throw new ZookeeperException("No application config persisted for application=" + application + ", group=" + group);
        }
        
        invocation.invoke(applicationConfig);

        persistApplication(applicationConfig);
    }
    
    @Override
    public void modifyApplication(ApplicationEntity applicationEntity, List<MethodInvocation> invocationList) throws Exception {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        ApplicationConfig applicationConfig = retrieveApplication(applicationEntity);
        if (applicationConfig == null) {
            throw new ZookeeperException("No application config persisted for application=" + application + ", group=" + group);
        }
        
        for (MethodInvocation invocation : invocationList) {
            invocation.invoke(applicationConfig);
        }

        persistApplication(applicationConfig);
    }
    
    @Override
    public void modifyServiceSecretKey(String interfaze, ApplicationEntity applicationEntity, String secretKey) throws Exception {
        MethodInvocation scretKeyInvocation = new MethodInvocation();
        scretKeyInvocation.setMethodName("setSecretKey");
        scretKeyInvocation.setParameterTypes(new Class<?>[] {String.class});
        scretKeyInvocation.setParameters(new Object[] {secretKey});
        
        modifyService(interfaze, applicationEntity, scretKeyInvocation);
    }
    
    @Override
    public void modifyServiceVersion(String interfaze, ApplicationEntity applicationEntity, int version) throws Exception {
        MethodInvocation versionInvocation = new MethodInvocation();
        versionInvocation.setMethodName("setVersion");
        versionInvocation.setParameterTypes(new Class<?>[] {int.class});
        versionInvocation.setParameters(new Object[] {version});
        
        modifyService(interfaze, applicationEntity, versionInvocation);
    }
    
    @Override
    public void modifyServiceToken(String interfaze, ApplicationEntity applicationEntity, long token) throws Exception {
        MethodInvocation tokenInvocation = new MethodInvocation();
        tokenInvocation.setMethodName("setToken");
        tokenInvocation.setParameterTypes(new Class<?>[] {long.class});
        tokenInvocation.setParameters(new Object[] {token});
        
        modifyService(interfaze, applicationEntity, tokenInvocation);
    }
    
    @Override
    public void modifyService(String interfaze, ApplicationEntity applicationEntity, MethodInvocation invocation) throws Exception {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        ServiceConfig serviceConfig = retrieveService(interfaze, applicationEntity);
        if (serviceConfig == null) {
            throw new ZookeeperException("No service config persisted for interface=" + interfaze + ", application=" + application + ", group=" + group);
        }
        
        invocation.invoke(serviceConfig);

        persistService(serviceConfig, applicationEntity);
    }

    @Override
    public void modifyService(String interfaze, ApplicationEntity applicationEntity, List<MethodInvocation> invocationList) throws Exception {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        ServiceConfig serviceConfig = retrieveService(interfaze, applicationEntity);
        if (serviceConfig == null) {
            throw new ZookeeperException("No service config persisted for interface=" + interfaze + ", application=" + application + ", group=" + group);
        }
        
        for (MethodInvocation invocation : invocationList) {
            invocation.invoke(serviceConfig);
        }

        persistService(serviceConfig, applicationEntity);
    }
    
    @Override
    public void modifyReferenceSecretKey(String interfaze, ApplicationEntity applicationEntity, String secretKey) throws Exception {
        MethodInvocation scretKeyInvocation = new MethodInvocation();
        scretKeyInvocation.setMethodName("setSecretKey");
        scretKeyInvocation.setParameterTypes(new Class<?>[] {String.class});
        scretKeyInvocation.setParameters(new Object[] {secretKey});
        
        modifyReference(interfaze, applicationEntity, scretKeyInvocation);
    }
    
    @Override
    public void modifyReferenceVersion(String interfaze, ApplicationEntity applicationEntity, int version) throws Exception {
        MethodInvocation versionInvocation = new MethodInvocation();
        versionInvocation.setMethodName("setVersion");
        versionInvocation.setParameterTypes(new Class<?>[] {int.class});
        versionInvocation.setParameters(new Object[] {version});
        
        modifyReference(interfaze, applicationEntity, versionInvocation);
    }

    @Override
    public void modifyReference(String interfaze, ApplicationEntity applicationEntity, List<MethodInvocation> invocationList) throws Exception {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        ReferenceConfig referenceConfig = retrieveReference(interfaze, applicationEntity);
        if (referenceConfig == null) {
            throw new ZookeeperException("No reference config persisted for interface=" + interfaze + ", application=" + application + ", group=" + group);
        }

        for (MethodInvocation invocation : invocationList) {
            invocation.invoke(referenceConfig);
        }

        persistReference(referenceConfig, applicationEntity);
    }

    @Override
    public void modifyReference(String interfaze, ApplicationEntity applicationEntity, MethodInvocation invocation) throws Exception {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        ReferenceConfig referenceConfig = retrieveReference(interfaze, applicationEntity);
        if (referenceConfig == null) {
            throw new ZookeeperException("No reference config persisted for interface=" + interfaze + ", application=" + application + ", group=" + group);
        }

        invocation.invoke(referenceConfig);

        persistReference(referenceConfig, applicationEntity); 
    }

    public StringBuilder createRootPath() {        
        StringBuilder builder = new StringBuilder();
        builder.append("/");
        builder.append(StringUtils.isEmpty(namespace) ? properties.getString(ThunderConstants.NAMESPACE_ELEMENT_NAME) : namespace);
        
        return builder;
    }
        
    public StringBuilder createApplicationCategoryPath() {
        StringBuilder builder = createRootPath();
        builder.append("/");
        builder.append(ThunderConstants.APPLICATION_ELEMENT_NAME);
        
        return builder;
    }
        
    public StringBuilder createProtocolCategoryPath() {
        ProtocolType protocolType = protocolEntity.getType();
        
        StringBuilder builder = createApplicationCategoryPath();
        builder.append("/");
        builder.append(protocolType);
        
        return builder;
    }

    public StringBuilder createGroupPath(String group) {
        StringBuilder builder = createProtocolCategoryPath();
        builder.append("/");
        builder.append(group);
        
        return builder;
    }
    
    public StringBuilder createApplicationPath(String application, String group) {
        StringBuilder builder = createGroupPath(group);
        builder.append("/");
        builder.append(application);
        
        return builder;
    }
    
    public StringBuilder createServiceCategoryPath(String application, String group) {
        StringBuilder builder = createApplicationPath(application, group);
        builder.append("/");
        builder.append(ThunderConstants.SERVICE_ELEMENT_NAME);
        
        return builder;
    }
    
    public StringBuilder createServiceInterfacePath(String interfaze, String application, String group) {                
        StringBuilder builder = createServiceCategoryPath(application, group);
        builder.append("/");
        builder.append(interfaze);
        
        return builder;
    }
    
    public StringBuilder createReferenceCategoryPath(String application, String group) { 
        StringBuilder builder = createApplicationPath(application, group);
        builder.append("/");
        builder.append(ThunderConstants.REFERENCE_ELEMENT_NAME);

        return builder;
    }
    
    public StringBuilder createReferenceInterfacePath(String interfaze, String application, String group) { 
        StringBuilder builder = createReferenceCategoryPath(application, group);
        builder.append("/");
        builder.append(interfaze);

        return builder;
    }
    
    public StringBuilder createConfigurationCategoryPath() {        
        StringBuilder builder = createRootPath();
        builder.append("/");
        builder.append(ThunderConstants.CONFIGURATION_ELEMENT_NAME);
        
        return builder;
    }
    
    public StringBuilder createConfigurationGroupPath(String group) {
        StringBuilder builder = createConfigurationCategoryPath();
        builder.append("/");
        builder.append(group);
        
        return builder;
    }
    
    public StringBuilder createConfigurationApplicationPath(String application, String group) {
        StringBuilder builder = createConfigurationGroupPath(group);
        builder.append("/");
        builder.append(application);
        
        return builder;
    }
    
    public StringBuilder createMonitorCategoryPath() {        
        StringBuilder builder = createRootPath();
        builder.append("/");
        builder.append(ThunderConstants.MONITOR_ELEMENT_NAME);
        
        return builder;
    }
    
    public StringBuilder createUserCategoryPath() {        
        StringBuilder builder = createRootPath();
        builder.append("/");
        builder.append(ThunderConstants.USER_ELEMENT_NAME);
        
        return builder;
    }
    
    public StringBuilder createUserPath(String name) {        
        StringBuilder builder = createUserCategoryPath();
        builder.append("/");
        builder.append(name);
        
        return builder;
    }
}