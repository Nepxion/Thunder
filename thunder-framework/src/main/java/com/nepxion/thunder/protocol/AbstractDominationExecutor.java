package com.nepxion.thunder.protocol;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.event.protocol.ProtocolEventFactory;
import com.nepxion.thunder.monitor.MonitorExecutor;

public class AbstractDominationExecutor extends ThunderDelegateImpl implements DominationExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDominationExecutor.class);

    @Override
    public void handleMonitor(ProtocolMessage message) {
        MonitorStat monitorStat = null;
        List<MonitorExecutor> monitorExecutors = getExecutorContainer().getMonitorExecutors();
        if (CollectionUtils.isNotEmpty(monitorExecutors)) {
            for (MonitorExecutor monitorExecutor : monitorExecutors) {
                if (monitorStat == null) {
                    monitorStat = monitorExecutor.createMonitorStat(message);
                }
                try {
                    monitorExecutor.execute(monitorStat);
                } catch (Exception e) {
                    LOG.error("Execute monitor failed, executor={}", monitorExecutor.getClass().getName());
                }
            }
        }
    }

    @Override
    public void handleEvent(ProtocolMessage message, ApplicationType applicationType) {
        ProtocolType protocolType = getCacheContainer().getProtocolEntity().getType();

        ProtocolEventFactory.postConsumerEvent(applicationType, protocolType, message);
    }
}