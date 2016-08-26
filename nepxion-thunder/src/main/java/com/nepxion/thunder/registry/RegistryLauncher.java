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

import com.nepxion.thunder.common.entity.ProtocolType;

// 提供给外部程序所用
public interface RegistryLauncher {

    // 启动注册中心连接
    void start(String address, ProtocolType protocolType) throws Exception;

    // 停止注册中心连接
    void stop() throws Exception;

    // 获取注册中心执行器
    RegistryExecutor getRegistryExecutor();
}