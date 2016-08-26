package com.nepxion.thunder.protocol.mq;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import javax.jms.BytesMessage;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ConnectionFactoryType;
import com.nepxion.thunder.common.entity.MQPropertyEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.SelectorType;
import com.nepxion.thunder.common.util.ClassUtil;

public class MQHierachy {
    protected ProtocolType protocolType;

    protected ConnectionFactory connectionFactory;
    protected ConnectionFactoryType connectionFactoryType;
    
    protected MQPropertyEntity mqPropertyEntity;
    protected MQTemplate mqTemplate;
    protected MQProducer mqProducer;
    
    public void initialize() throws Exception {
        String type = mqPropertyEntity.getString(ThunderConstants.MQ_CONNECTION_FACTORY_TYPE_ATTRIBUTE_NAME);
        connectionFactoryType = ConnectionFactoryType.fromString(type);
        switch (connectionFactoryType) {
            case SINGLE_CONNECTION_FACTORY:
                initializeSingleConnectionFactory();
                break;
            case CACHING_CONNECTION_FACTORY:
                initializeCachingConnectionFactory();
                break;
            case POOLED_CONNECTION_FACTORY:
                initializePooledConnectionFactory();
                break;
        }
        
        mqTemplate = new MQTemplate();
        mqTemplate.setConnectionFactory(connectionFactory);

        mqProducer = new MQProducer();
        mqProducer.setProtocolType(protocolType);
        mqProducer.setMQTemplate(mqTemplate);
    }
    
    private void initializeSingleConnectionFactory() throws Exception {
        boolean reconnectOnException = mqPropertyEntity.getBoolean(ThunderConstants.MQ_RECONNECT_ON_EXCEPTION_ATTRIBUTE_NAME);
        
        SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory();
        singleConnectionFactory.setReconnectOnException(reconnectOnException);
        
        connectionFactory = singleConnectionFactory;
    }
    
    private void initializeCachingConnectionFactory() throws Exception {
        int sessionCacheSize = mqPropertyEntity.getInteger(ThunderConstants.MQ_SESSION_CACHE_SIZE_ATTRIBUTE_NAME);
        boolean cacheConsumers = mqPropertyEntity.getBoolean(ThunderConstants.MQ_CACHE_CONSUMERS_ATTRIBUTE_NAME);
        boolean cacheProducers = mqPropertyEntity.getBoolean(ThunderConstants.MQ_CACHE_PRODUCERS_ATTRIBUTE_NAME);
        boolean reconnectOnException = mqPropertyEntity.getBoolean(ThunderConstants.MQ_RECONNECT_ON_EXCEPTION_ATTRIBUTE_NAME);
        
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        cachingConnectionFactory.setCacheConsumers(cacheConsumers);
        cachingConnectionFactory.setCacheProducers(cacheProducers);
        cachingConnectionFactory.setReconnectOnException(reconnectOnException);

        connectionFactory = cachingConnectionFactory;
    }
    
    private void initializePooledConnectionFactory() throws Exception {
        String pooledConnectionFactoryClass = mqPropertyEntity.getMQEntity().getPooledConnectionFactoryClass();
        int maxConnections = mqPropertyEntity.getInteger(ThunderConstants.MQ_MAX_CONNECTIONS_ATTRIBUTE_NAME);
        int maximumActiveSessionPerConnection = mqPropertyEntity.getInteger(ThunderConstants.MQ_MAXIMUM_ACTIVE_SESSION_PER_CONNECTION_ATTRIBUTE_NAME);
        int idleTimeout = mqPropertyEntity.getInteger(ThunderConstants.MQ_IDLE_TIMEOUT_ATTRIBUTE_NAME);
        long expiryTimeout = mqPropertyEntity.getLong(ThunderConstants.MQ_EXPIRY_TIMEOUT_ATTRIBUTE_NAME);
        boolean blockIfSessionPoolIsFull = mqPropertyEntity.getBoolean(ThunderConstants.MQ_BLOCK_IF_SESSION_POOL_IS_FULL_ATTRIBUTE_NAME);
        long blockIfSessionPoolIsFullTimeout = mqPropertyEntity.getLong(ThunderConstants.MQ_BLOCK_IF_SESSION_POOL_IS_FULL_TIMEOUT_ATTRIBUTE_NAME);
        long timeBetweenExpirationCheckMillis = mqPropertyEntity.getLong(ThunderConstants.MQ_TIME_BETWEEN_EXPIRATION_CHECK_MILLIS_ATTRIBUTE_NAME);
        boolean createConnectionOnStartup = mqPropertyEntity.getBoolean(ThunderConstants.MQ_CREATE_CONNECTION_ON_STARTUP_ATTRIBUTE_NAME);
        boolean reconnectOnException = mqPropertyEntity.getBoolean(ThunderConstants.MQ_RECONNECT_ON_EXCEPTION_ATTRIBUTE_NAME);

        ConnectionFactory pooledConnectionFactory = ClassUtil.createInstance(pooledConnectionFactoryClass);
        ClassUtil.invoke(pooledConnectionFactory, "setMaxConnections", new Class<?>[] {int.class}, new Object[] {maxConnections});
        ClassUtil.invoke(pooledConnectionFactory, "setMaximumActiveSessionPerConnection", new Class<?>[] {int.class}, new Object[] {maximumActiveSessionPerConnection});
        ClassUtil.invoke(pooledConnectionFactory, "setIdleTimeout", new Class<?>[] {int.class}, new Object[] {idleTimeout});
        ClassUtil.invoke(pooledConnectionFactory, "setExpiryTimeout", new Class<?>[] {long.class}, new Object[] {expiryTimeout});
        ClassUtil.invoke(pooledConnectionFactory, "setBlockIfSessionPoolIsFull", new Class<?>[] {boolean.class}, new Object[] {blockIfSessionPoolIsFull});
        ClassUtil.invoke(pooledConnectionFactory, "setBlockIfSessionPoolIsFullTimeout", new Class<?>[] {long.class}, new Object[] {blockIfSessionPoolIsFullTimeout});
        ClassUtil.invoke(pooledConnectionFactory, "setTimeBetweenExpirationCheckMillis", new Class<?>[] {long.class}, new Object[] {timeBetweenExpirationCheckMillis});
        ClassUtil.invoke(pooledConnectionFactory, "setCreateConnectionOnStartup", new Class<?>[] {boolean.class}, new Object[] {createConnectionOnStartup});
        ClassUtil.invoke(pooledConnectionFactory, "setReconnectOnException", new Class<?>[] {boolean.class}, new Object[] {reconnectOnException});
        
        connectionFactory = pooledConnectionFactory;
    }

    public void setTargetConnectionFactory(ConnectionFactory targetConnectionFactory) throws Exception {
        switch (connectionFactoryType) {
            case SINGLE_CONNECTION_FACTORY:
                ClassUtil.invoke(connectionFactory, "setTargetConnectionFactory", new Class<?>[] {ConnectionFactory.class}, new Object[] {targetConnectionFactory});
                break;
            case CACHING_CONNECTION_FACTORY:
                ClassUtil.invoke(connectionFactory, "setTargetConnectionFactory", new Class<?>[] {ConnectionFactory.class}, new Object[] {targetConnectionFactory});
                break;
            case POOLED_CONNECTION_FACTORY:
                ClassUtil.invoke(connectionFactory, "setConnectionFactory", new Class<?>[] {Object.class}, new Object[] {targetConnectionFactory});
                break;
        }
    }
    
    public void afterPropertiesSet() throws Exception {
        if (connectionFactory instanceof InitializingBean) {
            InitializingBean initializingBean = (InitializingBean) connectionFactory;
            initializingBean.afterPropertiesSet();
        }
        mqTemplate.afterPropertiesSet();
    }
    
    public void listen(Destination destination, SessionAwareMessageListener<BytesMessage> messageListener, String requestSelector, boolean topic) throws Exception {
        int concurrentConsumers = mqPropertyEntity.getInteger(ThunderConstants.MQ_CONCURRENT_CONSUMERS_ATTRIBUTE_NAME);
        int maxConcurrentConsumers = mqPropertyEntity.getInteger(ThunderConstants.MQ_MAX_CONCURRENT_CONSUMERS_ATTRIBUTE_NAME);
        long receiveTimeout = mqPropertyEntity.getLong(ThunderConstants.MQ_RECEIVE_TIMEOUT_ATTRIBUTE_NAME);
        long recoveryInterval = mqPropertyEntity.getLong(ThunderConstants.MQ_RECOVERY_INTERVAL_ATTRIBUTE_NAME);
        int idleConsumerLimit = mqPropertyEntity.getInteger(ThunderConstants.MQ_IDLE_CONSUMER_LIMIT_ATTRIBUTE_NAME);
        int idleTaskExecutionLimit = mqPropertyEntity.getInteger(ThunderConstants.MQ_IDLE_TASK_EXECUTION_LIMIT_ATTRIBUTE_NAME);
        int cacheLevel = mqPropertyEntity.getInteger(ThunderConstants.MQ_CACHE_LEVEL_ATTRIBUTE_NAME);
        boolean acceptMessagesWhileStopping = mqPropertyEntity.getBoolean(ThunderConstants.MQ_ACCEPT_MESSAGES_WHILE_STOPPING_ATTRIBUTE_NAME);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
        messageListenerAdapter.setDelegate(messageListener);

        DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setDestination(destination);
        messageListenerContainer.setConnectionFactory(connectionFactory);
        messageListenerContainer.setMessageListener(messageListenerAdapter);
        if (StringUtils.isNotEmpty(requestSelector)) {
            messageListenerContainer.setMessageSelector(SelectorType.REQUEST_SELECTOR + " = '" + requestSelector + "'");
        }
        messageListenerContainer.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        messageListenerContainer.setPubSubDomain(topic);
        messageListenerContainer.setConcurrentConsumers(topic ? 1 : concurrentConsumers);
        messageListenerContainer.setMaxConcurrentConsumers(topic ? 1 : maxConcurrentConsumers);
        messageListenerContainer.setReceiveTimeout(receiveTimeout);
        messageListenerContainer.setRecoveryInterval(recoveryInterval);
        messageListenerContainer.setIdleConsumerLimit(idleConsumerLimit);
        messageListenerContainer.setIdleTaskExecutionLimit(idleTaskExecutionLimit);
        messageListenerContainer.setCacheLevel(cacheLevel);
        messageListenerContainer.setAcceptMessagesWhileStopping(acceptMessagesWhileStopping);
        messageListenerContainer.afterPropertiesSet();
        messageListenerContainer.start();
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public MQPropertyEntity getMQPropertyEntity() {
        return mqPropertyEntity;
    }

    public void setMQPropertyEntity(MQPropertyEntity mqPropertyEntity) {
        this.mqPropertyEntity = mqPropertyEntity;
    }

    public MQTemplate getMQTemplate() {
        return mqTemplate;
    }

    public MQProducer getMQProducer() {
        return mqProducer;
    }
}