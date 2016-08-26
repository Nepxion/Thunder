package com.nepxion.thunder.cluster.loadbalance;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ConnectionEntity;
import com.nepxion.thunder.common.entity.LoadBalanceType;
import com.nepxion.thunder.common.entity.StrategyEntity;
import com.nepxion.thunder.common.util.StringUtil;

public abstract class AbstractLoadBalanceExecutor extends ThunderDelegateImpl implements LoadBalanceExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractLoadBalanceExecutor.class);

    // 只允许在主线程中运行
    @Override
    public ConnectionEntity loadBalance(String interfaze) throws Exception {
        boolean retry = properties.getBoolean(ThunderConstants.LOAD_BALANCE_RETRY_ATTRIBUTE_NAME);
        int retryTimes = properties.getInteger(ThunderConstants.LOAD_BALANCE_RETRY_TIMES_ATTRIBUTE_NAME);
        int retryDelay = properties.getInteger(ThunderConstants.LOAD_BALANCE_RETRY_DELAY_ATTRIBUTE_NAME);

        // 当服务集群全挂掉，提供两种方式：
        // 1. 不断的堵塞重试，等待集群恢复，Failover机制
        // 2. 直接抛出异常
        List<ConnectionEntity> connectionEntityList = getConnectionEntityList(interfaze);
        cacheConnectionEntityList(connectionEntityList);
        if (retry) {
            int i = 0;
            while (CollectionUtils.isEmpty(connectionEntityList) && i < retryTimes) {
                LOG.error("Service instance [{}] can't be retrieved at Registry Center...", interfaze);

                TimeUnit.MILLISECONDS.sleep(retryDelay);

                connectionEntityList = getConnectionEntityList(interfaze);
                cacheConnectionEntityList(connectionEntityList);
                
                i++;
            }
        } else {
            if (CollectionUtils.isEmpty(connectionEntityList)) {
                LoadBalanceException loadBalanceException = new LoadBalanceException("Service instance [" + interfaze + "] can't be retrieved at Registry Center");
                
                LOG.error("Services are all offline", loadBalanceException);
                
                throw loadBalanceException;
            }
        }

        // 注册中心节点上下线会延迟，偶尔会引起下面的问题，故通过内部堵塞重试解决
        ConnectionEntity connectionEntity = loadBalance(interfaze, connectionEntityList);
        int j = 0;
        while ((connectionEntity == null || !connectionEntity.isAvailable()) && j < 10) {
            LOG.error("Service instance [{}] may be offline, switch to another...", interfaze);

            TimeUnit.MILLISECONDS.sleep(3000);

            connectionEntity = loadBalance(interfaze, connectionEntityList);
            
            j++;
        }
        
        if (connectionEntity == null || !connectionEntity.isAvailable()) {
            LOG.error("Service is unavailable");
            
            return null;
        }
        
        boolean loadBalanceLogPrint = properties.getBoolean(ThunderConstants.LOAD_BALANCE_LOG_PRINT_ATTRIBUTE_NAME);
        if (loadBalanceLogPrint) {
            StrategyEntity strategyEntity = cacheContainer.getStrategyEntity();
            LoadBalanceType loadBalanceType = strategyEntity.getLoadBalanceType();
            ApplicationEntity applicationEntity = connectionEntity.getApplicationEntity();
            LOG.info("{} - Loadbalance to host={}, port={}, service={}", StringUtil.firstLetterToUpper(loadBalanceType.toString()), applicationEntity.getHost(), applicationEntity.getPort(), interfaze);
        }
        
        return connectionEntity;
    }

    protected List<ConnectionEntity> getConnectionEntityList(String interfaze) {
        return cacheContainer.getConnectionCacheEntity().getConnectionEntityList(interfaze);
    }

    // 用于更新一致性Hash的缓存，其它的负载均衡算法空实现
    protected void cacheConnectionEntityList(List<ConnectionEntity> connectionEntityList) {
        
    }
    
    protected abstract ConnectionEntity loadBalance(String interfaze, List<ConnectionEntity> connectionEntityList) throws Exception;
}