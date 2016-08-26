package com.nepxion.thunder.common.delegate;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.property.ThunderProperties;

public interface ThunderDelegate {
    // 获取属性句柄容器
    ThunderProperties getProperties();
    
    void setProperties(ThunderProperties properties);
    
    // 获取缓存容器
    CacheContainer getCacheContainer();

    void setCacheContainer(CacheContainer cacheContainer);
    
    // 获取执行器句柄容器
    ExecutorContainer getExecutorContainer();
    
    void setExecutorContainer(ExecutorContainer executorContainer);
    
    // 反射创建Delegate类
    <T> T createDelegate(String delegateClassId) throws Exception;
}