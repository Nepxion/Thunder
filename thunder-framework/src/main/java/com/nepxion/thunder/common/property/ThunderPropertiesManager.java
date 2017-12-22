package com.nepxion.thunder.common.property;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.spi.SpiLoader;

public class ThunderPropertiesManager {
    private static final Logger LOG = LoggerFactory.getLogger(ThunderPropertiesManager.class);
    private static final String DEFAULT_PATH = "thunder.properties";
    private static final String EXT_PATH = "thunder-ext.properties";

    private static ThunderProperties properties;
    private static ThunderProperties extProperties;
    private static ThunderProperties remoteProperties;

    static {
        initializeDefaultProperties();
        initializeExtProperties();
    }

    private static void initializeDefaultProperties() {
        try {
            LOG.info("Parse default property config file [{}]", DEFAULT_PATH);

            properties = new ThunderProperties(DEFAULT_PATH, ThunderConstants.ENCODING_FORMAT);
        } catch (Exception e) {
            LOG.error("Parse default property config file failed for [{}]", DEFAULT_PATH, e);
        }
    }

    private static void initializeExtProperties() {
        try {
            LOG.info("Parse ext property config file [{}]", EXT_PATH);

            extProperties = new ThunderProperties(EXT_PATH, ThunderConstants.ENCODING_FORMAT);
        } catch (Exception e) {
            LOG.warn("Parse ext property config file failed for [{}], maybe file doesn't exist, ignore", EXT_PATH);
        }

        if (properties != null && extProperties != null) {
            LOG.info("Merge ext property configs of [{}] to default property configs", EXT_PATH);

            try {
                properties.mergeProperties(extProperties);
            } catch (Exception e) {
                LOG.warn("Merge ext property configs failed", e);
            }
        }
    }

    public static ThunderPropertiesExecutor initializePropertiesExecutor() {
        ThunderPropertiesExecutor propertiesExecutor = null;
        try {
            propertiesExecutor = SpiLoader.load(ThunderPropertiesExecutor.class);

            LOG.info("Thunder properties executor is loaded from spi, class={}", propertiesExecutor.getClass().getName());
        } catch (Exception e) {
            LOG.info("Thunder properties executor isn't defined from spi, so use Register Center as remote properties storage");
        }

        return propertiesExecutor;
    }

    public static void initializeRemoteProperties(ThunderPropertiesExecutor propertiesExecutor, ApplicationEntity applicationEntity) throws Exception {
        if (propertiesExecutor == null) {
            return;
        }

        String property = propertiesExecutor.retrieveProperty(applicationEntity);
        if (StringUtils.isEmpty(property)) {
            if (extProperties != null) {
                property = extProperties.getContent();

                propertiesExecutor.persistProperty(property, applicationEntity);
            } else {
                LOG.warn("Local property configs are null, persistence is failed, ignore");
            }
        }

        if (StringUtils.isNotEmpty(property)) {
            remoteProperties = new ThunderProperties(property.getBytes(), ThunderConstants.ENCODING_FORMAT);

            LOG.info("Merge remote property configs to default property configs");
            LOG.info("---------------- Remote Property Config ----------------\r\n{}", remoteProperties.getContent());
            LOG.info("--------------------------------------------------------");

            try {
                properties.mergeProperties(remoteProperties);
            } catch (Exception e) {
                LOG.warn("Merge remote property configs failed", e);
            }
        } else {
            LOG.warn("Remote property configs are null, use default configs");
        }
    }

    public static ThunderProperties getProperties() {
        return properties;
    }

    public static ThunderProperties getExtProperties() {
        return extProperties;
    }

    public static ThunderProperties getRemoteProperties() {
        return remoteProperties;
    }
}