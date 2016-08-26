package com.nepxion.thunder.registry;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.RegistryEntity;
import com.nepxion.thunder.common.property.ThunderProperties;

public interface RegistryInitializer extends ThunderDelegate {
    
    // 启动和注册中心的连接
    void start(RegistryEntity registryEntity) throws Exception;

    // 启动和注册中心的连接
    void start(RegistryEntity registryEntity, ThunderProperties properties) throws Exception;

    // 停止注册中心的连接
    void stop() throws Exception;
}