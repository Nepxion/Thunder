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

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.MQEntity;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.WebServiceEntity;
import com.nepxion.thunder.framework.bean.ProtocolFactoryBean;
import com.nepxion.thunder.framework.exception.FrameworkException;

@SuppressWarnings("all")
public class ProtocolBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolBeanDefinitionParser.class);

    public ProtocolBeanDefinitionParser(ThunderDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        String typeAttributeName = ThunderConstants.TYPE_ATTRIBUTE_NAME;
        String pathAttributeName = ThunderConstants.PATH_ATTRIBUTE_NAME;

        String nettyServerExecutorId = ThunderConstants.NETTY_SERVER_EXECUTOR_ID;
        String nettyClientExecutorId = ThunderConstants.NETTY_CLIENT_EXECUTOR_ID;
        String nettyClientInterceptorId = ThunderConstants.NETTY_CLIENT_INTERCEPTOR_ID;

        String hessianServerExecutorId = ThunderConstants.HESSIAN_SERVER_EXECUTOR_ID;
        String hessianClientExecutorId = ThunderConstants.HESSIAN_CLIENT_EXECUTOR_ID;
        String hessianClientInterceptorId = ThunderConstants.HESSIAN_CLIENT_INTERCEPTOR_ID;

        String mqServerExecutorId = ThunderConstants.MQ_SERVER_EXECUTOR_ID;
        String mqClientExecutorId = ThunderConstants.MQ_CLIENT_EXECUTOR_ID;
        String mqClientInterceptorId = ThunderConstants.MQ_CLIENT_INTERCEPTOR_ID;

        String kafkaServerExecutorId = ThunderConstants.KAFKA_SERVER_EXECUTOR_ID;
        String kafkaClientExecutorId = ThunderConstants.KAFKA_CLIENT_EXECUTOR_ID;
        String kafkaClientInterceptorId = ThunderConstants.KAFKA_CLIENT_INTERCEPTOR_ID;
        
        String activeMQQueueId = ThunderConstants.ACTIVE_MQ_QUEUE_ID;
        String activeMQTopicId = ThunderConstants.ACTIVE_MQ_TOPIC_ID;
        String activeMQJndiInitialContextFactoryId = ThunderConstants.ACTIVE_MQ_JNDI_INITIAL_CONTEXT_FACTORY_ID;
        String activeMQInitialConnectionFactory = ThunderConstants.ACTIVE_MQ_INITIAL_CONNECTION_FACTORY_ID;
        String activeMQPooledConnectionFactoryId = ThunderConstants.ACTIVE_MQ_POOLED_CONNECTION_FACTORY_ID;
        
        String tibcoQueueId = ThunderConstants.TIBCO_QUEUE_ID;
        String tibcoTopicId = ThunderConstants.TIBCO_TOPIC_ID;
        String tibcoJndiInitialContextFactoryId = ThunderConstants.TIBCO_JNDI_INITIAL_CONTEXT_FACTORY_ID;
        String tibcoInitialConnectionFactory = ThunderConstants.TIBCO_INITIAL_CONNECTION_FACTORY_ID;
        String tibcoPooledConnectionFactoryId = ThunderConstants.TIBCO_POOLED_CONNECTION_FACTORY_ID;

        String type = element.getAttribute(typeAttributeName);
        ProtocolType protocolType = null;
        if (StringUtils.isNotEmpty(type)) {
            protocolType = ProtocolType.fromString(type);
        } else {
            protocolType = ProtocolType.NETTY;
        }

        LOG.info("Protocol type is {}", protocolType);

        ProtocolEntity protocolEntity = new ProtocolEntity();
        protocolEntity.setType(protocolType);

        switch (protocolType) {
            case NETTY:
                protocolEntity.setServerExecutorId(nettyServerExecutorId);
                protocolEntity.setClientExecutorId(nettyClientExecutorId);
                protocolEntity.setClientInterceptorId(nettyClientInterceptorId);
                break;
            case HESSIAN:
                protocolEntity.setServerExecutorId(hessianServerExecutorId);
                protocolEntity.setClientExecutorId(hessianClientExecutorId);
                protocolEntity.setClientInterceptorId(hessianClientInterceptorId);
                
                String path = properties.getString(pathAttributeName);
                if (StringUtils.isEmpty(path)) {
                    throw new FrameworkException("Web path is missing for " + protocolType);
                }
                WebServiceEntity webServiceEntity = new WebServiceEntity();
                webServiceEntity.setPath(path);
                cacheContainer.setWebServiceEntity(webServiceEntity);
                break;
            case KAFKA:
                protocolEntity.setServerExecutorId(kafkaServerExecutorId);
                protocolEntity.setClientExecutorId(kafkaClientExecutorId);
                protocolEntity.setClientInterceptorId(kafkaClientInterceptorId);
                
                MQEntity kafkaEntity = new MQEntity();
                kafkaEntity.extractProperties(properties, protocolType);
                cacheContainer.setMQEntity(kafkaEntity);
                break;
            case ACTIVE_MQ:
                protocolEntity.setServerExecutorId(mqServerExecutorId);
                protocolEntity.setClientExecutorId(mqClientExecutorId);
                protocolEntity.setClientInterceptorId(mqClientInterceptorId);
                
                MQEntity activeMQEntity = new MQEntity();
                activeMQEntity.setQueueId(activeMQQueueId);
                activeMQEntity.setTopicId(activeMQTopicId);
                activeMQEntity.setJndiInitialContextFactoryId(activeMQJndiInitialContextFactoryId);
                activeMQEntity.setInitialConnectionFactoryId(activeMQInitialConnectionFactory);
                activeMQEntity.setPooledConnectionFactoryId(activeMQPooledConnectionFactoryId);
                activeMQEntity.extractProperties(properties, protocolType);
                cacheContainer.setMQEntity(activeMQEntity);
                break;
            case TIBCO:
                protocolEntity.setServerExecutorId(mqServerExecutorId);
                protocolEntity.setClientExecutorId(mqClientExecutorId);
                protocolEntity.setClientInterceptorId(mqClientInterceptorId);
                
                MQEntity tibcoEntity = new MQEntity();
                tibcoEntity.setQueueId(tibcoQueueId);
                tibcoEntity.setTopicId(tibcoTopicId);
                tibcoEntity.setJndiInitialContextFactoryId(tibcoJndiInitialContextFactoryId);
                tibcoEntity.setInitialConnectionFactoryId(tibcoInitialConnectionFactory);
                tibcoEntity.setPooledConnectionFactoryId(tibcoPooledConnectionFactoryId);
                tibcoEntity.extractProperties(properties, protocolType);
                cacheContainer.setMQEntity(tibcoEntity);
                break;
        }

        cacheContainer.setProtocolEntity(protocolEntity);
        builder.addPropertyValue(createBeanName(ProtocolEntity.class), protocolEntity);
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ProtocolFactoryBean.class;
    }
}