package com.nepxion.thunder.common.property;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.entity.ApplicationEntity;

// 从外部接口获取配置文件
public interface ThunderPropertiesExecutor {

    // 获取Property文本配置信息
    String retrieveProperty(ApplicationEntity applicationEntity) throws Exception;

    // 持久化Property文本配置信息
    void persistProperty(String property, ApplicationEntity applicationEntity) throws Exception;
}