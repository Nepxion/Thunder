package com.nepxion.thunder.framework.bean;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.cluster.consistency.ConsistencyExecutor;
import com.nepxion.thunder.cluster.loadbalance.LoadBalanceExecutor;
import com.nepxion.thunder.common.entity.StrategyEntity;

public class StrategyFactoryBean extends AbstractFactoryBean {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyFactoryBean.class);

    private StrategyEntity strategyEntity;
    private LoadBalanceExecutor loadBalanceExecutor;
    private ConsistencyExecutor consistencyExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("StrategyFactoryBean has been initialized...");
    }

    @Override
    public StrategyEntity getObject() throws Exception {
        return strategyEntity;
    }

    @Override
    public Class<StrategyEntity> getObjectType() {
        return StrategyEntity.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setStrategyEntity(StrategyEntity strategyEntity) {
        this.strategyEntity = strategyEntity;
    }

    public StrategyEntity getStrategyEntity() {
        return strategyEntity;
    }

    public void setLoadBalanceExecutor(LoadBalanceExecutor loadBalanceExecutor) {
        this.loadBalanceExecutor = loadBalanceExecutor;
    }
    
    public LoadBalanceExecutor getLoadBalanceExecutor() {
        return loadBalanceExecutor;
    }
    
    public void setConsistencyExecutor(ConsistencyExecutor consistencyExecutor) {
        this.consistencyExecutor = consistencyExecutor;
    }

    public ConsistencyExecutor getConsistencyExecutor() {
        return consistencyExecutor;
    }
}