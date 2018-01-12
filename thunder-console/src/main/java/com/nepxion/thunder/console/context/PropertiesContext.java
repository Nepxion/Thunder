package com.nepxion.thunder.console.context;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;
import com.nepxion.thunder.serialization.SerializerFactory;

public class PropertiesContext {
    public static final String LOGGER_TAB_SELECTION_ATTRIBUTE_NAME = "loggerTabSelection";
    public static final String LOGGER_FILE_PATH_ATTRIBUTE_NAME = "loggerFilePath";
    public static final String LOGGER_TRACE_ID_ATTRIBUTE_NAME = "loggerTraceId";

    private static ThunderProperties properties = ThunderPropertiesManager.getProperties();

    public static void initialize() {
        ObjectPoolFactory.initialize(properties);
        ThreadPoolFactory.initialize(properties);
        SerializerFactory.initialize(properties);
    }

    public static String getRegistryAddress() {
        try {
            return properties.getString(ThunderConstant.ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setRegistryAddress(String registryAddress) {
        registryAddress = registryAddress.replaceAll(";", ",");
        properties.put(ThunderConstant.ZOOKEEPER_ADDRESS_ATTRIBUTE_NAME, registryAddress);
    }

    public static String getRedisSentinel() {
        try {
            return properties.getString(ThunderConstant.REDIS_SENTINEL_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRedisMasterName() {
        try {
            return properties.getString(ThunderConstant.REDIS_MASTER_NAME_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRedisClientName() {
        try {
            return properties.getString(ThunderConstant.REDIS_CLIENT_NAME_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRedisPassword() {
        try {
            return properties.getString(ThunderConstant.REDIS_PASSWORD_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRedisCluster() {
        try {
            return properties.getString(ThunderConstant.REDIS_CLUSTER_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getLoggerHost() {
        try {
            return properties.getString(ThunderConstant.SPLUNK_HOST_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getLoggerPort() {
        try {
            return properties.getInteger(ThunderConstant.SPLUNK_PORT_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getLoggerUserName() {
        try {
            return properties.getString(ThunderConstant.SPLUNK_USER_NAME_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getLoggerPassword() {
        try {
            return properties.getString(ThunderConstant.SPLUNK_PASSWORD_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isLoggerTabSelection() {
        boolean loggerTabSelection = false;
        try {
            loggerTabSelection = properties.getBoolean(LOGGER_TAB_SELECTION_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return false;
        }

        return loggerTabSelection;
    }

    public static String getLoggerFilePath() {
        String loggerFilePath = null;
        try {
            loggerFilePath = properties.getString(LOGGER_FILE_PATH_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }

        if (StringUtils.isNotEmpty(loggerFilePath)) {
            loggerFilePath = loggerFilePath.replace("//", "\\");
        }

        return loggerFilePath;
    }

    public static String getLoggerTraceId() {
        try {
            return properties.getString(LOGGER_TRACE_ID_ATTRIBUTE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    public static void addPropertiesMap(Map<String, String> value) {
        for (Map.Entry<String, String> entry : value.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
    }

    public static ThunderProperties getProperties() {
        return properties;
    }
}