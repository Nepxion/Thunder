package com.nepxion.thunder.monitor;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.protocol.ProtocolMessage;

public interface MonitorExecutor extends ThunderDelegate {

    // 创建监控对象
    MonitorStat createMonitorStat(ProtocolMessage message);
    
    // 执行监控过程
    void execute(MonitorStat monitorStat) throws Exception;
}