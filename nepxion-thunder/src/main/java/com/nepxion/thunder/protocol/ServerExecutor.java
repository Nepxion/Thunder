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

public interface ServerExecutor extends ThunderDelegate {
    // 服务端启动
    void start(String interfaze, ApplicationEntity applicationEntity) throws Exception;
    
    // 服务端是否启动
    boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception;
}