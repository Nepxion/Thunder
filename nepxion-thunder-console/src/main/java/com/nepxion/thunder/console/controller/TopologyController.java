package com.nepxion.thunder.console.controller;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.swing.element.ElementNode;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.console.context.PropertiesContext;
import com.nepxion.thunder.console.context.RegistryContext;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.registry.RegistryExecutor;

public class TopologyController {
    public static ElementNode[] getProtocolNodes() {
        int index = 0;
        ElementNode[] protocolNodes = new ElementNode[ProtocolType.values().length];
        for (ProtocolType protocolType : ProtocolType.values()) {
            ElementNode protocolNode = new ElementNode(protocolType.toString(), protocolType.toString(), ConsoleIconFactory.getSwingIcon("netbean/media_16.png"), protocolType.toString(), protocolType);
            protocolNodes[index] = protocolNode;
            index++;
        }

        return protocolNodes;
    }

    public static ElementNode[] getTimerNodes() {
        return new ElementNode[] {
                new ElementNode(ConsoleLocale.getString("second_5"), ConsoleIconFactory.getSwingIcon("component/text_pane_16.png"), ConsoleLocale.getString("second_5"), new Integer(5000)),
                new ElementNode(ConsoleLocale.getString("second_10"), ConsoleIconFactory.getSwingIcon("component/text_pane_16.png"), ConsoleLocale.getString("second_10"), new Integer(10000)),
                new ElementNode(ConsoleLocale.getString("second_30"), ConsoleIconFactory.getSwingIcon("component/text_pane_16.png"), ConsoleLocale.getString("second_30"), new Integer(30000)),
                new ElementNode(ConsoleLocale.getString("minute_1"), ConsoleIconFactory.getSwingIcon("component/text_pane_16.png"), ConsoleLocale.getString("minute_1"), new Integer(60000)),
                new ElementNode(ConsoleLocale.getString("minute_5"), ConsoleIconFactory.getSwingIcon("component/text_pane_16.png"), ConsoleLocale.getString("minute_5"), new Integer(300000)),
                new ElementNode(ConsoleLocale.getString("minute_10"), ConsoleIconFactory.getSwingIcon("component/text_pane_16.png"), ConsoleLocale.getString("minute_10"), new Integer(600000)) };
    }

    public static List<ApplicationEntity> getServiceInstances(ProtocolType protocolType, ApplicationEntity applicationEntity) throws Exception {
        RegistryExecutor registryExecutor = RegistryContext.getRegistryExecutor(protocolType);

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        List<ApplicationEntity> serviceInstances = new ArrayList<ApplicationEntity>();
        List<String> services = registryExecutor.getServiceList(application, group);
        for (String service : services) {
            List<ApplicationEntity> serviceInstanceList = registryExecutor.getServiceInstanceList(service, applicationEntity);
            for (ApplicationEntity serviceInstance : serviceInstanceList) {
                if (!serviceInstances.contains(serviceInstance)) {
                    serviceInstances.add(serviceInstance);
                }
            }
        }

        return CollectionUtils.isNotEmpty(serviceInstances) ? serviceInstances : null;
    }

    public static List<ApplicationEntity> getReferenceInstances(ProtocolType protocolType, ApplicationEntity applicationEntity) throws Exception {
        RegistryExecutor registryExecutor = RegistryContext.getRegistryExecutor(protocolType);

        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        List<ApplicationEntity> referenceInstances = new ArrayList<ApplicationEntity>();
        List<String> references = registryExecutor.getReferenceList(application, group);
        for (String reference : references) {
            List<ApplicationEntity> referenceInstanceList = registryExecutor.getReferenceInstanceList(reference, applicationEntity);
            for (ApplicationEntity referenceInstance : referenceInstanceList) {
                if (!referenceInstances.contains(referenceInstance)) {
                    referenceInstances.add(referenceInstance);
                }
            }
        }

        return CollectionUtils.isNotEmpty(referenceInstances) ? referenceInstances : null;
    }

    public static List<String> getRegistryUrls() {
        String registryValue = PropertiesContext.getRegistryAddress();
        if (StringUtils.isNotEmpty(registryValue)) {
            String[] registryArray = StringUtils.split(registryValue, ",");
            List<String> registryUrls = new ArrayList<String>();
            for (String registry : registryArray) {
                registryUrls.add(registry);
            }

            return registryUrls;
        }

        return null;
    }

    @SuppressWarnings("incomplete-switch")
    public static List<String> getMQUrls(ProtocolType protocolType, ApplicationEntity applicationEntity) throws Exception {
        ThunderProperties properties = PropertyController.getProperties(applicationEntity);
        Map<String, ThunderProperties> propertiesMap = properties.extractProperties(protocolType.toString());

        List<String> mqUrls = new ArrayList<String>();

        try {
            // 局部变量
            for (Map.Entry<String, ThunderProperties> entry : propertiesMap.entrySet()) {
                ThunderProperties mqProperties = entry.getValue();
                String mqUrl = null;
                switch (protocolType) {
                    case KAFKA:
                        mqUrl = mqProperties.getString(ThunderConstants.KAFKA_PRODUCER_BOOTSTRAP_SERVERS_ATTRIBUTE_NAME);
                        break;
                    case ACTIVE_MQ:
                        mqUrl = mqProperties.getString(ThunderConstants.MQ_URL_ATTRIBUTE_NAME);
                        break;
                    case TIBCO:
                        mqUrl = mqProperties.getString(ThunderConstants.MQ_URL_ATTRIBUTE_NAME);
                        break;
                }

                if (StringUtils.isNotEmpty(mqUrl)) {
                    mqUrls.add(mqUrl);
                }
            }

            // 全局变量
            String mqUrl = null;
            switch (protocolType) {
                case KAFKA:
                    mqUrl = properties.getString(ThunderConstants.KAFKA_PRODUCER_BOOTSTRAP_SERVERS_ATTRIBUTE_NAME);
                    break;
                case ACTIVE_MQ:
                    mqUrl = properties.getString(ThunderConstants.MQ_URL_ATTRIBUTE_NAME);
                    break;
                case TIBCO:
                    mqUrl = properties.getString(ThunderConstants.MQ_URL_ATTRIBUTE_NAME);
                    break;
            }

            if (StringUtils.isNotEmpty(mqUrl)) {
                mqUrls.add(mqUrl);
            }
        } catch (Exception e) {

        }

        return CollectionUtils.isNotEmpty(mqUrls) ? mqUrls : null;
    }

    public static List<String> getCacheUrls(ApplicationEntity applicationEntity) throws Exception {
        ThunderProperties properties = PropertyController.getProperties(applicationEntity);

        String sentinelValue = properties.getString(ThunderConstants.REDIS_SENTINEL_ATTRIBUTE_NAME);
        if (StringUtils.isNotEmpty(sentinelValue)) {
            String[] sentinelArray = StringUtils.split(sentinelValue, ";");
            List<String> cacheUrls = new ArrayList<String>();
            for (String sentinel : sentinelArray) {
                cacheUrls.add(sentinel);
            }

            return cacheUrls;
        }
        
        String clusterValue = properties.getString(ThunderConstants.REDIS_CLUSTER_ATTRIBUTE_NAME);
        if (StringUtils.isNotEmpty(clusterValue)) {
            String[] clusterArray = StringUtils.split(clusterValue, ";");
            List<String> cacheUrls = new ArrayList<String>();
            for (String cluster : clusterArray) {
                cacheUrls.add(cluster);
            }

            return cacheUrls;
        }

        return null;
    }

    public static List<String> getLoggerUrls(ApplicationEntity applicationEntity) throws Exception {
        ThunderProperties properties = PropertyController.getProperties(applicationEntity);
        
        String host = properties.getString(ThunderConstants.SPLUNK_HOST_ATTRIBUTE_NAME);
        int port = properties.getInteger(ThunderConstants.SPLUNK_PORT_ATTRIBUTE_NAME);
        
        return Arrays.asList(host + ":" + port);
    }
}