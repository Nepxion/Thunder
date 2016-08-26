package com.nepxion.thunder.framework.parser;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.nepxion.thunder.cluster.consistency.ConsistencyExecutor;
import com.nepxion.thunder.cluster.loadbalance.LoadBalanceExecutor;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.LoadBalanceType;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.StrategyEntity;
import com.nepxion.thunder.framework.bean.StrategyFactoryBean;
import com.nepxion.thunder.framework.exception.FrameworkException;

public class StrategyBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyBeanDefinitionParser.class);

    public StrategyBeanDefinitionParser(ThunderDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        StrategyEntity strategyEntity = new StrategyEntity();
        
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        ProtocolType protocolType = protocolEntity.getType();
        if (protocolType.isLoadBalanceSupported()) {
            String loadBalanceAttributeName = ThunderConstants.LOAD_BALANCE_ATTRIBUTE_NAME;
            
            String loadBalance = element.getAttribute(loadBalanceAttributeName);
            LoadBalanceType loadBalanceType = null;
            if (StringUtils.isNotEmpty(loadBalance)) {
                loadBalanceType = LoadBalanceType.fromString(loadBalance);
            } else {
                loadBalanceType = LoadBalanceType.CONSISTENT_HASH;
            }

            LOG.info("Load balance type is {}", loadBalanceType);

            strategyEntity.setLoadBalanceType(loadBalanceType);
            LoadBalanceExecutor loadBalanceExecutor = createLoadBalanceExecutor(loadBalanceType);
            builder.addPropertyValue(createBeanName(LoadBalanceExecutor.class), loadBalanceExecutor);
            
            ConsistencyExecutor consistencyExecutor = createConsistencyExecutor();
            builder.addPropertyValue(createBeanName(ConsistencyExecutor.class), consistencyExecutor);
        }
        
        cacheContainer.setStrategyEntity(strategyEntity);
        builder.addPropertyValue(createBeanName(StrategyEntity.class), strategyEntity);
    }
    
    protected LoadBalanceExecutor createLoadBalanceExecutor(LoadBalanceType loadBalanceType) {
        LoadBalanceExecutor loadBalanceExecutor = executorContainer.getLoadBalanceExecutor();
        if (loadBalanceExecutor == null) {
            String consistentHashLoadBalanceExecutorId = ThunderConstants.CONSISTENT_HASH_LOAD_BALANCE_EXECUTOR_ID;
            String roundRobinLoadBalanceExecutorId = ThunderConstants.ROUND_ROBIN_LOAD_BALANCE_EXECUTOR_ID;
            String randomLoadBalanceExecutorId = ThunderConstants.RANDOM_LOAD_BALANCE_EXECUTOR_ID;
            try {
                switch (loadBalanceType) {
                    case CONSISTENT_HASH:
                        loadBalanceExecutor = createDelegate(consistentHashLoadBalanceExecutorId);
                        break;
                    case ROUND_ROBIN:
                        loadBalanceExecutor = createDelegate(roundRobinLoadBalanceExecutorId);
                        break;
                    case RANDOM:
                        loadBalanceExecutor = createDelegate(randomLoadBalanceExecutorId);
                        break;
                }
            } catch (Exception e) {
                throw new FrameworkException("Creat LoadBalanceExecutor failed", e);
            }

            executorContainer.setLoadBalanceExecutor(loadBalanceExecutor);
        }
        
        return loadBalanceExecutor;
    }
    
    protected ConsistencyExecutor createConsistencyExecutor() {
        ConsistencyExecutor consistencyExecutor = executorContainer.getConsistencyExecutor();
        if (consistencyExecutor == null) {
            String consistencyExecutorId = ThunderConstants.CONSISTENCY_EXECUTOR_ID;
            try {
                consistencyExecutor = createDelegate(consistencyExecutorId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ConsistencyExecutor failed", e);
            }
            
            executorContainer.setConsistencyExecutor(consistencyExecutor);
        }
        
        return consistencyExecutor;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return StrategyFactoryBean.class;
    }
}