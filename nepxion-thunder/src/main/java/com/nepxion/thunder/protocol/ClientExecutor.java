package com.nepxion.thunder.protocol;

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
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ConnectionEntity;

public interface ClientExecutor extends ThunderDelegate {

    // 客户端启动连接
    void start(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 客户端是否启动
    boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    // 客户端上线，更新缓存
    ConnectionEntity online(String interfaze, ApplicationEntity applicationEntity, Object connnectionHandler) throws Exception;

    // 客户端下线，更新缓存
    void offline(String interfaze, ApplicationEntity applicationEntity) throws Exception;
}