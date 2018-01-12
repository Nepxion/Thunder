package com.nepxion.thunder.testcase.controller;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.junit.Test;

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.registry.RegistryLauncher;
import com.nepxion.thunder.registry.zookeeper.ZookeeperRegistryLauncher;

public class RegistryController {

    @Test
    public void test() throws Exception {
        // 启动注册中心连接
        RegistryLauncher registryLauncher = new ZookeeperRegistryLauncher();
        registryLauncher.start("localhost:2181", ProtocolType.NETTY);

        RegistryExecutor registryExecutor = registryLauncher.getRegistryExecutor();

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApplication("APP-IOS");
        applicationEntity.setGroup("MY_GROUP");
        String interfaze = "com.nepxion.thunder.test.core.UserService";

        registryExecutor.resetApplication(applicationEntity);
        registryExecutor.resetService(interfaze, applicationEntity);
        registryExecutor.resetReference(interfaze, applicationEntity);
//        registryExecutor.modifyApplicationFrequency(applicationEntity, 60000);
//        registryExecutor.modifyServiceSecretKey(interfaze, applicationEntity, "123456");
//        registryExecutor.modifyServiceVersion(interfaze, applicationEntity, 1);
//        registryExecutor.modifyServiceToken(interfaze, applicationEntity, 10);
//        registryExecutor.modifyReferenceSecretKey(interfaze, applicationEntity, "123456");
//        registryExecutor.modifyReferenceVersion(interfaze, applicationEntity, 1);

        // 停止注册中心连接
        // registryLauncher.stop();

        System.in.read();
    }
}