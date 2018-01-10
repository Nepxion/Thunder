package com.nepxion.thunder.protocol.mq;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ConnectionFactoryType;
import com.nepxion.thunder.common.entity.DestinationType;
import com.nepxion.thunder.common.entity.MQEntity;
import com.nepxion.thunder.common.entity.MQPropertyEntity;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.protocol.ProtocolException;

public class MQContext {
    private static final Logger LOG = LoggerFactory.getLogger(MQContext.class);

    private CacheContainer cacheContainer;
    private ExecutorContainer executorContainer;
    private ProtocolEntity protocolEntity;

    private MQHierachy mqHierachy;
    private MQEntity mqEntity;
    private MQPropertyEntity mqPropertyEntity;
    private MQQueueDestinationContainer mqQueueDestinationContainer;
    private MQTopicDestinationContainer mqTopicDestinationContainer;

    private String queueClass;
    private String topicClass;
    private String url;

    private ScheduledExecutorService executor;

    public MQContext(MQExecutorDelegate executorDelegate) {
        this.cacheContainer = executorDelegate.getCacheContainer();
        this.executorContainer = executorDelegate.getExecutorContainer();

        this.protocolEntity = cacheContainer.getProtocolEntity();
        this.mqEntity = cacheContainer.getMQEntity();
        this.mqQueueDestinationContainer = MQCacheContainer.getMQQueueDestinationContainer();
        this.mqTopicDestinationContainer = MQCacheContainer.getMQTopicDestinationContainer();
    }

    public void initializeContext(String interfaze, String server) throws Exception {
        mqPropertyEntity = new MQPropertyEntity(interfaze, server, mqEntity);

        // Context配置方式有两种：JNDI和非JNDI，如果配置jndiName，则选用JNDI模式，否则选择非JNDI模式
        try {
            String jndiName = mqPropertyEntity.getString(ThunderConstant.MQ_JNDI_NAME_ATTRIBUTE_NAME);

            LOG.info("Use Jndi mode, Jndi name={}", jndiName);
            mqHierachy = new MQJndiHierachy();
        } catch (Exception e) {
            mqHierachy = new MQConnectionHierachy();
        }

        try {
            url = mqPropertyEntity.getString(ThunderConstant.MQ_URL_ATTRIBUTE_NAME);
            LOG.info("Attempt to connect to {}", url);

            String type = mqPropertyEntity.getString(ThunderConstant.MQ_CONNECTION_FACTORY_TYPE_ATTRIBUTE_NAME);
            ConnectionFactoryType connectionFactoryType = ConnectionFactoryType.fromString(type);
            LOG.info("Connection factory type is {}", connectionFactoryType);

            startRetryNotification();

            ProtocolType protocolType = protocolEntity.getType();
            mqHierachy.setProtocolType(protocolType);
            mqHierachy.setMQPropertyEntity(mqPropertyEntity);
            mqHierachy.initialize();

            queueClass = mqEntity.getQueueClass();
            topicClass = mqEntity.getTopicClass();
        } catch (Exception e) {
            throw new ProtocolException("Initialize connection context failed", e);
        }
    }

    public void startRetryNotification() throws Exception {
        if (executor == null) {
            executor = Executors.newSingleThreadScheduledExecutor();
        }

        final int retryNotificationDelay = mqPropertyEntity.getInteger(ThunderConstant.MQ_RETRY_NOTIFICATION_DELAY_ATTRIBUTE_NAME);
        executor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                LOG.info("Wait for connection response from {}, retry in {} ms...", url, retryNotificationDelay);
            }
        }, retryNotificationDelay, retryNotificationDelay, TimeUnit.MILLISECONDS);
    }

    public void stopRetryNotification() throws Exception {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public void initializeRequestContext(String interfaze, ApplicationEntity applicationEntity, boolean createHandler) throws Exception {
        try {
            Map<String, Destination> queueAsyncRequestDestinationMap = mqQueueDestinationContainer.getAsyncRequestDestinationMap();
            Destination queueAsyncRequestDestination = queueAsyncRequestDestinationMap.get(interfaze);
            if (queueAsyncRequestDestination == null) {
                queueAsyncRequestDestination = MQDestinationUtil.createDestination(queueClass, DestinationType.REQUEST_QUEUE_ASYNC, interfaze, applicationEntity);
                queueAsyncRequestDestinationMap.put(interfaze, queueAsyncRequestDestination);
            }

            Map<String, Destination> queueSyncRequestDestinationMap = mqQueueDestinationContainer.getSyncRequestDestinationMap();
            Destination queueSyncRequestDestination = queueSyncRequestDestinationMap.get(interfaze);
            if (queueSyncRequestDestination == null) {
                queueSyncRequestDestination = MQDestinationUtil.createDestination(queueClass, DestinationType.REQUEST_QUEUE_SYNC, interfaze, applicationEntity);
                queueSyncRequestDestinationMap.put(interfaze, queueSyncRequestDestination);
            }

            if (createHandler) {
                initializeClientHandler(queueAsyncRequestDestination, applicationEntity);
                initializeClientHandler(queueSyncRequestDestination, applicationEntity);
            }
        } catch (Exception e) {
            LOG.error("Initialize request context failed", e);
            throw new ProtocolException("Initialize request context failed", e);
        }
    }

    private void initializeClientHandler(Destination destination, ApplicationEntity applicationEntity) throws Exception {
        String requestSelector = applicationEntity.toUrl();

        MQClientHandler clientHandler = new MQClientHandler(mqPropertyEntity);
        clientHandler.setCacheContainer(cacheContainer);
        clientHandler.setExecutorContainer(executorContainer);
        mqHierachy.listen(destination, clientHandler, requestSelector, false);
    }

    public void initializeResponseContext(String interfaze, ApplicationEntity applicationEntity, boolean createHandler) throws Exception {
        try {
            Map<String, Destination> queueAsyncResponseDestinationMap = mqQueueDestinationContainer.getAsyncResponseDestinationMap();
            Destination queueAsyncResponseDestination = queueAsyncResponseDestinationMap.get(interfaze);
            if (queueAsyncResponseDestination == null) {
                queueAsyncResponseDestination = MQDestinationUtil.createDestination(queueClass, DestinationType.RESPONSE_QUEUE_ASYNC, interfaze, applicationEntity);
                queueAsyncResponseDestinationMap.put(interfaze, queueAsyncResponseDestination);
            }

            Map<String, Destination> queueSyncResponseDestinationMap = mqQueueDestinationContainer.getSyncResponseDestinationMap();
            Destination queueSyncResponseDestination = queueSyncResponseDestinationMap.get(interfaze);
            if (queueSyncResponseDestination == null) {
                queueSyncResponseDestination = MQDestinationUtil.createDestination(queueClass, DestinationType.RESPONSE_QUEUE_SYNC, interfaze, applicationEntity);
                queueSyncResponseDestinationMap.put(interfaze, queueSyncResponseDestination);
            }

            Map<String, Destination> topicAsyncResponseDestinationMap = mqTopicDestinationContainer.getAsyncResponseDestinationMap();
            Destination topicAsyncResponseDestination = topicAsyncResponseDestinationMap.get(interfaze);
            if (topicAsyncResponseDestination == null) {
                // 订阅的Response是Topic订阅模式
                topicAsyncResponseDestination = MQDestinationUtil.createDestination(topicClass, DestinationType.RESPONSE_TOPIC_ASYNC, interfaze, applicationEntity);
                topicAsyncResponseDestinationMap.put(interfaze, topicAsyncResponseDestination);
            }

            if (createHandler) {
                initializeServerHandler(queueAsyncResponseDestination, false);
                initializeServerHandler(queueSyncResponseDestination, false);
                initializeServerHandler(topicAsyncResponseDestination, true);
            }
        } catch (Exception e) {
            LOG.error("Initialize response context failed", e);
            throw new ProtocolException("Initialize response context failed", e);
        }
    }

    private void initializeServerHandler(Destination destination, boolean topic) throws Exception {
        MQProducer mqProducer = getMQProducer();

        MQServerHandler serverHandler = new MQServerHandler(mqProducer, mqPropertyEntity);
        serverHandler.setCacheContainer(cacheContainer);
        serverHandler.setExecutorContainer(executorContainer);
        mqHierachy.listen(destination, serverHandler, null, topic);
    }

    public MQTemplate getMQTemplate() {
        return mqHierachy.getMQTemplate();
    }

    public MQProducer getMQProducer() {
        return mqHierachy.getMQProducer();
    }
}