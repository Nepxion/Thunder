package com.nepxion.thunder.testcase.user;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.common.entity.UserEntity;
import com.nepxion.thunder.common.entity.UserFactory;
import com.nepxion.thunder.common.entity.UserOperation;
import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.registry.RegistryInitializer;
import com.nepxion.thunder.registry.zookeeper.ZookeeperRegistryExecutor;
import com.nepxion.thunder.registry.zookeeper.ZookeeperRegistryInitializer;

public class UserTest {
    private static final Logger LOG = LoggerFactory.getLogger(UserTest.class);

    @Test
    public void test() throws Exception {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        // RedisSentinelPoolFactory.initialize(properties);

        RegistryEntity registryEntity = new RegistryEntity();
        registryEntity.setAddress("localhost:2181");
        
        ProtocolEntity protocolEntity = new ProtocolEntity();
        protocolEntity.setType(ProtocolType.NETTY);

        RegistryInitializer registryInitializer = new ZookeeperRegistryInitializer();
        registryInitializer.start(registryEntity, properties);
        
        RegistryExecutor registryExecutor = new ZookeeperRegistryExecutor();
        registryExecutor.setRegistryInitializer(registryInitializer);
        registryExecutor.setProtocolEntity(protocolEntity);

        UserEntity administrator = UserFactory.createAdministrator();
        registryExecutor.persistUser(administrator);
        
        UserEntity admin_user1 = UserFactory.createAdminUser("admin_user1", "111111");
        registryExecutor.persistUser(admin_user1);
        
        UserEntity admin_user2 = UserFactory.createAdminUser("admin_user2", "111111");
        registryExecutor.persistUser(admin_user2);
        
        UserEntity admin_user3 = UserFactory.createAdminUser("admin_user3", "111111");
        registryExecutor.persistUser(admin_user3);
        
        UserEntity user1 = UserFactory.createUser("user1", "111111", Arrays.asList(UserOperation.SERVICE_CONTROL, UserOperation.SERVICE_CONTROL));
        registryExecutor.persistUser(user1);
        
        UserEntity user2 = UserFactory.createUser("user2", "111111", Arrays.asList(UserOperation.REMOTE_CONTROL));
        registryExecutor.persistUser(user2);
        
        UserEntity user3 = UserFactory.createUser("user3", "111111", Arrays.asList(UserOperation.SAFETY_CONTROL));
        registryExecutor.persistUser(user3);
        
        UserEntity user4 = UserFactory.createUser("user4", "111111", Arrays.asList(UserOperation.SERVICE_CONTROL, UserOperation.REMOTE_CONTROL));
        registryExecutor.persistUser(user4);
        
        UserEntity user5 = UserFactory.createUser("user5", "111111", Arrays.asList(UserOperation.REMOTE_CONTROL, UserOperation.SAFETY_CONTROL));
        registryExecutor.persistUser(user5);
        
        UserEntity user6 = UserFactory.createUser("user6", "111111", new ArrayList<UserOperation>());
        registryExecutor.persistUser(user6);
        
        LOG.info(registryExecutor.retrieveUserList().toString());
        
        /*UserEntity admin_user4 = UserFactory.createAdminUser("admin", "111111");
        registryExecutor.persistUser(admin_user4);
        
        UserEntity user7 = UserFactory.createUser("user7", "111111", Arrays.asList(UserOperation.USER_CONTROL));
        registryExecutor.persistUser(user7);
        
        UserEntity user8 = UserFactory.createUser("admin", "111111", Arrays.asList(UserOperation.SERVICE_CONTROL));
        registryExecutor.persistUser(user8);
        
        registryExecutor.deleteUser(administrator);*/
    }
}