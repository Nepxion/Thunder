package com.nepxion.thunder.monitor;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.LoadBalanceType;
import com.nepxion.thunder.common.entity.MethodKey;
import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.StrategyEntity;
import com.nepxion.thunder.common.util.ExceptionUtil;
import com.nepxion.thunder.protocol.ProtocolMessage;
import com.nepxion.thunder.protocol.ProtocolRequest;

public abstract class AbstractMonitorExecutor extends ThunderDelegateImpl implements MonitorExecutor {

    @Override
    public MonitorStat createMonitorStat(ProtocolMessage message) {
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        StrategyEntity strategyEntity = cacheContainer.getStrategyEntity();
        ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

        MonitorStat monitorStat = new MonitorStat();
        monitorStat.setTraceId(message.getTraceId());
        monitorStat.setMessageId(message.getMessageId());
        monitorStat.setMessageType(message instanceof ProtocolRequest ? MonitorStat.MESSAGE_TYPE_REQUEST : MonitorStat.MESSAGE_TYPE_RESPONSE);
        monitorStat.setFromCluster(message.getFromCluster());
        monitorStat.setFromUrl(message.getFromUrl());
        monitorStat.setToCluster(message.getToCluster());
        monitorStat.setToUrl(message.getToUrl());
        monitorStat.setProcessStartTime(message.getProcessStartTime());
        monitorStat.setProcessEndTime(message.getProcessEndTime());
        monitorStat.setDeliverStartTime(message.getDeliverStartTime());
        monitorStat.setDeliverEndTime(message.getDeliverEndTime());
        monitorStat.setProtocol(protocolEntity.getType().toString());
        monitorStat.setApplication(applicationEntity.getApplication());
        monitorStat.setGroup(applicationEntity.getGroup());
        monitorStat.setInterfaze(message.getInterface());
        monitorStat.setParameterTypes(MethodKey.toParameterTypes(message.getParameterTypes()));
        monitorStat.setMethod(message.getMethod());
        monitorStat.setAsync(message.isAsync());
        monitorStat.setCallback(message.getCallback());
        monitorStat.setTimeout(message.getTimeout());
        monitorStat.setBroadcast(message.isBroadcast());
        if (strategyEntity != null) {
            LoadBalanceType loadBalanceType = strategyEntity.getLoadBalanceType();
            if (loadBalanceType != null) {
                monitorStat.setLoadBalance(loadBalanceType.toString());
            }
        }
        monitorStat.setFeedback(message.isFeedback());
        if (message.getException() != null) {
            monitorStat.setException(ExceptionUtil.toExceptionString(message.getException()));
        }

        return monitorStat;
    }
}