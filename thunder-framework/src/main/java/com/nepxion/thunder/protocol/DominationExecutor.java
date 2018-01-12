package com.nepxion.thunder.protocol;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.ApplicationType;

public interface DominationExecutor extends ThunderDelegate {

    // 监控处理
    void handleMonitor(ProtocolMessage message);

    // EventBus异步事件处理
    void handleEvent(ProtocolMessage message, ApplicationType applicationType);
}