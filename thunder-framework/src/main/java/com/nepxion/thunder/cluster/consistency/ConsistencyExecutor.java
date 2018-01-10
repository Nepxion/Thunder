package com.nepxion.thunder.cluster.consistency;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.event.registry.ServiceInstanceEvent;

public interface ConsistencyExecutor extends ThunderDelegate {
    // 接受注册中心上下线事件，本地缓存与注册中心保持一致
    void consist(ServiceInstanceEvent event) throws Exception;
}