package com.nepxion.thunder.common.constant;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class ThunderConstant {
    public static final String NAMESPACE_ELEMENT_NAME = "namespace";
    public static final String APPLICATION_ELEMENT_NAME = "application";
    public static final String CONFIGURATION_ELEMENT_NAME = "configuration";
    public static final String USER_ELEMENT_NAME = "user";
    public static final String MONITOR_ELEMENT_NAME = "monitor";
    public static final String PROTOCOL_ELEMENT_NAME = "protocol";
    public static final String REGISTRY_ELEMENT_NAME = "registry";
    public static final String STRATEGY_ELEMENT_NAME = "strategy";
    public static final String SERVICE_ELEMENT_NAME = "service";
    public static final String REFERENCE_ELEMENT_NAME = "reference";
    public static final String METHOD_ELEMENT_NAME = "method";

    public static final String APPLICATION_ATTRIBUTE_NAME = "application";
    public static final String GROUP_ATTRIBUTE_NAME = "group";
    public static final String CLUSTER_ATTRIBUTE_NAME = "cluster";
    public static final String TYPE_ATTRIBUTE_NAME = "type";
    public static final String HOST_ATTRIBUTE_NAME = "host";
    public static final String PORT_ATTRIBUTE_NAME = "port";
    public static final String ADDRESS_ATTRIBUTE_NAME = "address";
    public static final String CONFIG_ATTRIBUTE_NAME = "config";
    public static final String LOAD_BALANCE_ATTRIBUTE_NAME = "loadbalance";
    public static final String INTERFACE_ATTRIBUTE_NAME = "interface";
    public static final String METHOD_ATTRIBUTE_NAME = "method";
    public static final String REF_ATTRIBUTE_NAME = "ref";
    public static final String PARAMETER_TYPES_ATTRIBUTE_NAME = "parameterTypes";
    public static final String TRACE_ID_INDEX_ATTRIBUTE_NAME = "traceIdIndex";
    public static final String ASYNC_ATTRIBUTE_NAME = "async";
    public static final String SYNC_ATTRIBUTE_NAME = "sync";
    public static final String TIMEOUT_ATTRIBUTE_NAME = "timeout";
    public static final String ASYNC_TIMEOUT_ATTRIBUTE_NAME = "asyncTimeout";
    public static final String SYNC_TIMEOUT_ATTRIBUTE_NAME = "syncTimeout";
    public static final String ASYNC_SCAN_ATTRIBUTE_NAME = "asyncScan";
    public static final String BROADCAST_ATTRIBUTE_NAME = "broadcast";
    public static final String CALLBACK_ATTRIBUTE_NAME = "callback";
    public static final String CALLBACK_TYPE_ATTRIBUTE_NAME = "callbackType";
    public static final String SERVER_ATTRIBUTE_NAME = "server";
    public static final String PATH_ATTRIBUTE_NAME = "path";

    public static final String EVENT_NOTIFICATION_ATTRIBUTE_NAME = "eventNotification";

    public static final String SMTP_NOTIFICATION_ATTRIBUTE_NAME = "smtpNotification";
    public static final String SMTP_NOTIFICATION_EXCLUSION_ATTRIBUTE_NAME = "smtpNotificationExclusion";
    public static final String SMTP_SSL_ATTRIBUTE_NAME = "smtpSsl";
    public static final String SMTP_HOST_ATTRIBUTE_NAME = "smtpHost";
    public static final String SMTP_USER_ATTRIBUTE_NAME = "smtpUser";
    public static final String SMTP_PASSWORD_ATTRIBUTE_NAME = "smtpPassword";
    public static final String SMTP_MAIL_FROM_ATTRIBUTE_NAME = "smtpMailFrom";
    public static final String SMTP_MAIL_TO_ATTRIBUTE_NAME = "smtpMailTo";
    public static final String SMTP_MAIL_CC_ATTRIBUTE_NAME = "smtpMailCC";
    public static final String SMTP_MAIL_BCC_ATTRIBUTE_NAME = "smtpMailBCC";

    public static final String LOAD_BALANCE_RETRY_ATTRIBUTE_NAME = "loadBalanceRetry";
    public static final String LOAD_BALANCE_RETRY_TIMES_ATTRIBUTE_NAME = "loadBalanceRetryTimes";
    public static final String LOAD_BALANCE_RETRY_DELAY_ATTRIBUTE_NAME = "loadBalanceRetryDelay";

    public static final String FREQUENCY_ATTRIBUTE_NAME = "frequency";
    public static final String SECRET_KEY_ATTRIBUTE_NAME = "secretKey";
    public static final String VERSION_ATTRIBUTE_NAME = "version";
    public static final String TOKEN_ATTRIBUTE_NAME = "token";

    public static final String SERVER_EXECUTOR_ADAPTER_ID = "serverExecutorAdapter";
    public static final String CLIENT_EXECUTOR_ADAPTER_ID = "clientExecutorAdapter";
    public static final String CLIENT_INTERCEPTOR_ADAPTER_ID = "clientInterceptorAdapter";

    public static final String THREAD_POOL_MULTI_MODE_ATTRIBUTE_NAME = "threadPoolMultiMode";
    public static final String THREAD_POOL_SERVER_CORE_POOL_SIZE_ATTRIBUTE_NAME = "threadPoolServerCorePoolSize";
    public static final String THREAD_POOL_SERVER_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME = "threadPoolServerMaximumPoolSize";
    public static final String THREAD_POOL_SERVER_KEEP_ALIVE_TIME_ATTRIBUTE_NAME = "threadPoolServerKeepAliveTime";
    public static final String THREAD_POOL_SERVER_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME = "threadPoolServerAllowCoreThreadTimeout";
    public static final String THREAD_POOL_CLIENT_CORE_POOL_SIZE_ATTRIBUTE_NAME = "threadPoolClientCorePoolSize";
    public static final String THREAD_POOL_CLIENT_MAXIMUM_POOL_SIZE_ATTRIBUTE_NAME = "threadPoolClientMaximumPoolSize";
    public static final String THREAD_POOL_CLIENT_KEEP_ALIVE_TIME_ATTRIBUTE_NAME = "threadPoolClientKeepAliveTime";
    public static final String THREAD_POOL_CLIENT_ALLOW_CORE_THREAD_TIMEOUT_ATTRIBUTE_NAME = "threadPoolClientAllowCoreThreadTimeout";
    public static final String THREAD_POOL_REJECTED_POLICY_ATTRIBUTE_NAME = "threadPoolRejectedPolicy";
    public static final String THREAD_POOL_QUEUE_ATTRIBUTE_NAME = "threadPoolQueue";
    public static final String THREAD_POOL_QUEUE_CAPACITY_ATTRIBUTE_NAME = "threadPoolQueueCapacity";

    public static final String FST_OBJECT_POOL_MAX_TOTAL_ATTRIBUTE_NAME = "fstObjectPoolMaxTotal";
    public static final String FST_OBJECT_POOL_MAX_IDLE_ATTRIBUTE_NAME = "fstObjectPoolMaxIdle";
    public static final String FST_OBJECT_POOL_MIN_IDLE_ATTRIBUTE_NAME = "fstObjectPoolMinIdle";
    public static final String FST_OBJECT_POOL_MAX_WAIT_MILLIS_ATTRIBUTE_NAME = "fstObjectPoolMaxWaitMillis";
    public static final String FST_OBJECT_POOL_TIME_BETWEEN_EVICTION_RUN_MILLIS_ATTRIBUTE_NAME = "fstObjectPoolTimeBetweenEvictionRunsMillis";
    public static final String FST_OBJECT_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME = "fstObjectPoolMinEvictableIdleTimeMillis";
    public static final String FST_OBJECT_POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME = "fstObjectPoolSoftMinEvictableIdleTimeMillis";
    public static final String FST_OBJECT_POOL_BLOCK_WHEN_EXHAUSTED_ATTRIBUTE_NAME = "fstObjectPoolBlockWhenExhausted";
    public static final String FST_OBJECT_POOL_LIFO_ATTRIBUTE_NAME = "fstObjectPoolLifo";
    public static final String FST_OBJECT_POOL_FAIRNESS_ATTRIBUTE_NAME = "fstObjectPoolFairness";

    public static final String NETTY_SERVER_EXECUTOR_ID = "nettyServerExecutor";
    public static final String NETTY_CLIENT_EXECUTOR_ID = "nettyClientExecutor";
    public static final String NETTY_CLIENT_INTERCEPTOR_ID = "nettyClientInterceptor";
    public static final String NETTY_SO_BACKLOG_ATTRIBUTE_NAME = "nettySoBacklog";
    public static final String NETTY_SO_SNDBUF_ATTRIBUTE_NAME = "nettySoSendBuffer";
    public static final String NETTY_SO_RCVBUF_ATTRIBUTE_NAME = "nettySoReceiveBuffer";
    public static final String NETTY_WRITE_BUFFER_HIGH_WATER_MARK_ATTRIBUTE_NAME = "nettyWriteBufferHighWaterMark";
    public static final String NETTY_WRITE_BUFFER_LOW_WATER_MARK_ATTRIBUTE_NAME = "nettyWriteBufferLowWaterMark";
    public static final String NETTY_MAX_MESSAGE_SIZE_ATTRIBUTE_NAME = "nettyMaxMessageSize";
    public static final String NETTY_WRITE_IDLE_TIME_ATTRIBUTE_NAME = "nettyWriteIdleTime";
    public static final String NETTY_READ_IDLE_TIME_ATTRIBUTE_NAME = "nettyReadIdleTime";
    public static final String NETTY_ALL_IDLE_TIME_ATTRIBUTE_NAME = "nettyAllIdleTime";
    public static final String NETTY_WRITE_TIMEOUT_ATTRIBUTE_NAME = "nettyWriteTimeout";
    public static final String NETTY_READ_TIMEOUT_ATTRIBUTE_NAME = "nettyReadTimeout";
    public static final String NETTY_CONNECT_TIMEOUT_ATTRIBUTE_NAME = "nettyConnectTimeout";
    public static final String NETTY_RECONNECT_DELAY_ATTRIBUTE_NAME = "nettyReconnectDelay";

    public static final String HESSIAN_SERVER_EXECUTOR_ID = "hessianServerExecutor";
    public static final String HESSIAN_CLIENT_EXECUTOR_ID = "hessianClientExecutor";
    public static final String HESSIAN_CLIENT_INTERCEPTOR_ID = "hessianClientInterceptor";
    public static final String HESSIAN_SERVICE_EXPORTER_ID = "hessianServiceExporter";
    public static final String HESSIAN_READ_TIMEOUT_ATTRIBUTE_NAME = "hessianReadTimeout";
    public static final String HESSIAN_CONNECT_TIMEOUT_ATTRIBUTE_NAME = "hessianConnectTimeout";
    public static final String HESSIAN_SERVLET_FILE_ATTRIBUTE_NAME = "hessianServletFile";

    public static final String REDIS_OBJECT_POOL_MAX_TOTAL_ATTRIBUTE_NAME = "redisObjectPoolMaxTotal";
    public static final String REDIS_OBJECT_POOL_MAX_IDLE_ATTRIBUTE_NAME = "redisObjectPoolMaxIdle";
    public static final String REDIS_OBJECT_POOL_MIN_IDLE_ATTRIBUTE_NAME = "redisObjectPoolMinIdle";
    public static final String REDIS_OBJECT_POOL_MAX_WAIT_MILLIS_ATTRIBUTE_NAME = "redisObjectPoolMaxWaitMillis";
    public static final String REDIS_OBJECT_POOL_TIME_BETWEEN_EVICTION_RUN_MILLIS_ATTRIBUTE_NAME = "redisObjectPoolTimeBetweenEvictionRunsMillis";
    public static final String REDIS_OBJECT_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME = "redisObjectPoolMinEvictableIdleTimeMillis";
    public static final String REDIS_OBJECT_POOL_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS_ATTRIBUTE_NAME = "redisObjectPoolSoftMinEvictableIdleTimeMillis";
    public static final String REDIS_OBJECT_POOL_BLOCK_WHEN_EXHAUSTED_ATTRIBUTE_NAME = "redisObjectPoolBlockWhenExhausted";
    public static final String REDIS_OBJECT_POOL_LIFO_ATTRIBUTE_NAME = "redisObjectPoolLifo";
    public static final String REDIS_OBJECT_POOL_FAIRNESS_ATTRIBUTE_NAME = "redisObjectPoolFairness";
    public static final String REDIS_SO_TIMEOUT_ATTRIBUTE_NAME = "redisSoTimeout";
    public static final String REDIS_CONNECTION_TIMEOUT_ATTRIBUTE_NAME = "redisConnectionTimeout";
    public static final String REDIS_DATA_EXPIRATION_ATTRIBUTE_NAME = "redisDataExpiration";
    public static final String REDIS_RECONNECTION_WAIT_ATTRIBUTE_NAME = "redisReconnectionWait";
    public static final String REDIS_SENTINEL_ATTRIBUTE_NAME = "redisSentinel";
    public static final String REDIS_MASTER_NAME_ATTRIBUTE_NAME = "redisMasterName";
    public static final String REDIS_CLIENT_NAME_ATTRIBUTE_NAME = "redisClientName";
    public static final String REDIS_PASSWORD_ATTRIBUTE_NAME = "redisPassword";
    public static final String REDIS_DATABASE_ATTRIBUTE_NAME = "redisDatabase";
    public static final String REDIS_CLUSTER_ATTRIBUTE_NAME = "redisCluster";
    public static final String REDIS_MAX_REDIRECTIONS_ATTRIBUTE_NAME = "redisMaxRedirections";

    public static final String KAFKA_SERVER_EXECUTOR_ID = "kafkaServerExecutor";
    public static final String KAFKA_CLIENT_EXECUTOR_ID = "kafkaClientExecutor";
    public static final String KAFKA_CLIENT_INTERCEPTOR_ID = "kafkaClientInterceptor";
    public static final String KAFKA_PRODUCER_ATTRIBUTE_NAME = "kafka.producer";
    public static final String KAFKA_CONSUMER_ATTRIBUTE_NAME = "kafka.consumer";
    public static final String KAFKA_PRODUCER_BOOTSTRAP_SERVERS_ATTRIBUTE_NAME = "kafka.producer.bootstrap.servers";
    public static final String KAFKA_CONSUMER_BOOTSTRAP_SERVERS_ATTRIBUTE_NAME = "kafka.consumer.bootstrap.servers";
    public static final String KAFKA_CONSUMER_SERVER_POLL_TIMEOUT_ATTRIBUTE_NAME = "kafka.consumer.server.poll.timeout.ms";
    public static final String KAFKA_CONSUMER_CLIENT_POLL_TIMEOUT_ATTRIBUTE_NAME = "kafka.consumer.client.poll.timeout.ms";

    public static final String MQ_SERVER_EXECUTOR_ID = "mqServerExecutor";
    public static final String MQ_CLIENT_EXECUTOR_ID = "mqClientExecutor";
    public static final String MQ_CLIENT_INTERCEPTOR_ID = "mqClientInterceptor";
    public static final String MQ_RETRY_NOTIFICATION_DELAY_ATTRIBUTE_NAME = "mqRetryNotificationDelay";
    public static final String MQ_RECONNECT_ON_EXCEPTION_ATTRIBUTE_NAME = "mqReconnectOnException";

    public static final String MQ_SESSION_CACHE_SIZE_ATTRIBUTE_NAME = "mqSessionCacheSize";
    public static final String MQ_CACHE_CONSUMERS_ATTRIBUTE_NAME = "mqCacheConsumers";
    public static final String MQ_CACHE_PRODUCERS_ATTRIBUTE_NAME = "mqCacheProducers";

    public static final String MQ_MAX_CONNECTIONS_ATTRIBUTE_NAME = "mqMaxConnections";
    public static final String MQ_MAXIMUM_ACTIVE_SESSION_PER_CONNECTION_ATTRIBUTE_NAME = "mqMaximumActiveSessionPerConnection";
    public static final String MQ_IDLE_TIMEOUT_ATTRIBUTE_NAME = "mqIdleTimeout";
    public static final String MQ_EXPIRY_TIMEOUT_ATTRIBUTE_NAME = "mqExpiryTimeout";
    public static final String MQ_BLOCK_IF_SESSION_POOL_IS_FULL_ATTRIBUTE_NAME = "mqBlockIfSessionPoolIsFull";
    public static final String MQ_BLOCK_IF_SESSION_POOL_IS_FULL_TIMEOUT_ATTRIBUTE_NAME = "mqBlockIfSessionPoolIsFullTimeout";
    public static final String MQ_TIME_BETWEEN_EXPIRATION_CHECK_MILLIS_ATTRIBUTE_NAME = "mqTimeBetweenExpirationCheckMillis";
    public static final String MQ_CREATE_CONNECTION_ON_STARTUP_ATTRIBUTE_NAME = "mqCreateConnectionOnStartup";

    public static final String MQ_CONCURRENT_CONSUMERS_ATTRIBUTE_NAME = "mqConcurrentConsumers";
    public static final String MQ_MAX_CONCURRENT_CONSUMERS_ATTRIBUTE_NAME = "mqMaxConcurrentConsumers";
    public static final String MQ_RECEIVE_TIMEOUT_ATTRIBUTE_NAME = "mqReceiveTimeout";
    public static final String MQ_RECOVERY_INTERVAL_ATTRIBUTE_NAME = "mqRecoveryInterval";
    public static final String MQ_IDLE_CONSUMER_LIMIT_ATTRIBUTE_NAME = "mqIdleConsumerLimit";
    public static final String MQ_IDLE_TASK_EXECUTION_LIMIT_ATTRIBUTE_NAME = "mqIdleTaskExecutionLimit";
    public static final String MQ_CACHE_LEVEL_ATTRIBUTE_NAME = "mqCacheLevel";
    public static final String MQ_ACCEPT_MESSAGES_WHILE_STOPPING_ATTRIBUTE_NAME = "mqAcceptMessagesWhileStopping";

    public static final String MQ_URL_ATTRIBUTE_NAME = "mqUrl";
    public static final String MQ_CONNECTION_FACTORY_TYPE_ATTRIBUTE_NAME = "mqConnectionFactoryType";
    public static final String MQ_USER_NAME_ATTRIBUTE_NAME = "mqUserName";
    public static final String MQ_PASSWORD_ATTRIBUTE_NAME = "mqPassword";
    public static final String MQ_JNDI_NAME_ATTRIBUTE_NAME = "mqJndiName";

    public static final String ACTIVE_MQ_QUEUE_ID = "activeMQQueue";
    public static final String ACTIVE_MQ_TOPIC_ID = "activeMQTopic";
    public static final String ACTIVE_MQ_JNDI_INITIAL_CONTEXT_FACTORY_ID = "activeMQJndiInitialContextFactory";
    public static final String ACTIVE_MQ_INITIAL_CONNECTION_FACTORY_ID = "activeMQInitialConnectionFactory";
    public static final String ACTIVE_MQ_POOLED_CONNECTION_FACTORY_ID = "activeMQPooledConnectionFactory";

    public static final String TIBCO_QUEUE_ID = "tibcoQueue";
    public static final String TIBCO_TOPIC_ID = "tibcoTopic";
    public static final String TIBCO_JNDI_INITIAL_CONTEXT_FACTORY_ID = "tibcoJndiInitialContextFactory";
    public static final String TIBCO_INITIAL_CONNECTION_FACTORY_ID = "tibcoInitialConnectionFactory";
    public static final String TIBCO_POOLED_CONNECTION_FACTORY_ID = "tibcoPooledConnectionFactory";

    public static final String APACHE_BACKLOG_SIZE_ATTRIBUTE_NAME = "apacheBacklogSize";
    public static final String APACHE_SNDBUF_SIZE_ATTRIBUTE_NAME = "apacheSendBufferSize";
    public static final String APACHE_RCVBUF_SIZE_ATTRIBUTE_NAME = "apacheReceiveBufferSize";
    public static final String APACHE_SO_TIMEOUT_ATTRIBUTE_NAME = "apacheSoTimeout";
    public static final String APACHE_CONNECT_TIMEOUT_ATTRIBUTE_NAME = "apacheConnectTimeout";
    public static final String APACHE_MAX_TOTAL_ATTRIBUTE_NAME = "apacheMaxTotal";

    public static final String ZOOKEEPER_REGISTRY_INITIALIZER_ID = "zookeeperRegistryInitializer";
    public static final String ZOOKEEPER_REGISTRY_EXECUTOR_ID = "zookeeperRegistryExecutor";
    public static final String ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME = "zookeeperAddress";
    public static final String ZOOKEEPER_SESSION_TIMOUT_ATTRIBUTE_NAME = "zookeeperSessionTimeout";
    public static final String ZOOKEEPER_CONNECT_TIMEOUT_ATTRIBUTE_NAME = "zookeeperConnectTimeout";
    public static final String ZOOKEEPER_CONNECT_WAIT_TIME_ATTRIBUTE_NAME = "zookeeperConnectWaitTime";

    public static final String SPLUNK_HOST_ATTRIBUTE_NAME = "splunkHost";
    public static final String SPLUNK_PORT_ATTRIBUTE_NAME = "splunkPort";
    public static final String SPLUNK_USER_NAME_ATTRIBUTE_NAME = "splunkUserName";
    public static final String SPLUNK_PASSWORD_ATTRIBUTE_NAME = "splunkPassword";
    public static final String SPLUNK_SOURCE_TYPE_ATTRIBUTE_NAME = "splunkSourceType";
    public static final String SPLUNK_MAXIMUM_TIME_ATTRIBUTE_NAME = "splunkMaximumTime";
    public static final String SPLUNK_EARLIEST_TIME_ATTRIBUTE_NAME = "splunkEarliestTime";
    public static final String SPLUNK_LATEST_TIME_ATTRIBUTE_NAME = "splunkLatestTime";

    public static final String CONSISTENCY_EXECUTOR_ID = "consistencyExecutor";
    public static final String CONSISTENT_HASH_LOAD_BALANCE_EXECUTOR_ID = "consistentHashLoadBalanceExecutor";
    public static final String ROUND_ROBIN_LOAD_BALANCE_EXECUTOR_ID = "roundRobinLoadBalanceExecutor";
    public static final String RANDOM_LOAD_BALANCE_EXECUTOR_ID = "randomLoadBalanceExecutor";

    public static final String SECURITY_EXECUTOR_ID = "securityExecutor";

    public static final String LOG_SERVICE_MONITOR_EXECUTOR_ID = "logServiceMonitorExecutor";
    public static final String REDIS_SERVICE_MONITOR_EXECUTOR_ID = "redisServiceMonitorExecutor";
    public static final String WEB_SERVICE_MONITOR_EXECUTOR_ID = "webServiceMonitorExecutor";

    public static final String BINARY_SERIALIZER_ATTRIBUTE_NAME = "binarySerializer";
    public static final String JSON_SERIALIZER_ATTRIBUTE_NAME = "jsonSerializer";

    public static final String COMPRESSOR_ATTRIBUTE_NAME = "compressor";
    public static final String COMPRESS_ATTRIBUTE_NAME = "compress";

    public static final String HOST_PARAMETER_NAME = "ThunderHost";
    public static final String PORT_PARAMETER_NAME = "ThunderPort";
    public static final String REGISTRY_ADDRESS_PARAMETER_NAME = "ThunderRegistryAddress";

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_GBK = "GBK";
    public static final String ENCODING_ISO_8859_1 = "ISO-8859-1";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String USER_ADMIN_NAME = "admin";
    public static final String USER_ADMIN_PASSWORD = "admin";

    public static final String TRACE_ID = "traceId";

    public static final String EVENT_BUS = "eventBus";

    public static final String LOAD_BALANCE_LOG_PRINT_ATTRIBUTE_NAME = "loadBalanceLogPrint";
    public static final String TRANSPORT_LOG_PRINT_ATTRIBUTE_NAME = "transportLogPrint";
    public static final String HEART_BEAT_LOG_PRINT_ATTRIBUTE_NAME = "heartBeatLogPrint";
    public static final String SERIALIZER_LOG_PRINT_ATTRIBUTE_NAME = "serializerLogPrint";

    public static final int CPUS = Math.max(2, Runtime.getRuntime().availableProcessors());
}