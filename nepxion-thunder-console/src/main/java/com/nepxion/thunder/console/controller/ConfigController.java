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

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.swing.element.ElementNode;
import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.console.context.RegistryContext;
import com.nepxion.thunder.console.icon.ConsoleIconFactory;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.registry.RegistryExecutor;
import com.nepxion.thunder.registry.zookeeper.ZookeeperRegistryExecutor;

public class ConfigController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

    public static ElementNode assembleRoot(String category) throws Exception {
        Map<ProtocolType, RegistryExecutor> registryExecutorMap = RegistryContext.getRegistryExecutorMap();

        ElementNode root = new ElementNode(ConsoleLocale.getString("registry_center"), ConsoleLocale.getString("registry_center"), null, ConsoleLocale.getString("registry_center"));

        if (StringUtils.equals(category, ThunderConstants.APPLICATION_ELEMENT_NAME)) {
            for (Map.Entry<ProtocolType, RegistryExecutor> entry : registryExecutorMap.entrySet()) {
                ProtocolType protocolType = entry.getKey();
                RegistryExecutor registryExecutor = entry.getValue();
                assembleApplication(registryExecutor, protocolType, root);
            }
        } else if (StringUtils.equals(category, ThunderConstants.CONFIGURATION_ELEMENT_NAME)) {
            RegistryExecutor registryExecutor = RegistryContext.getDefaultRegistryExecutor();
            assembleConfiguration(registryExecutor, root);
        }

        return root;
    }

    private static void assembleApplication(RegistryExecutor registryExecutor, ProtocolType protocolType, ElementNode root) throws Exception {
        ElementNode protocolNode = new ElementNode(protocolType.toString(), protocolType.toString(), null, protocolType.toString());
        root.add(protocolNode);

        List<String> groups = null;
        try {
            groups = registryExecutor.getGroupList();
        } catch (Exception e) {
            LOG.warn("Get group list failed, protocol={}", protocolType);
        }

        if (CollectionUtils.isNotEmpty(groups)) {
            for (String group : groups) {
                ElementNode groupNode = new ElementNode(protocolType.toString(), group, null, group);
                protocolNode.add(groupNode);

                List<String> applications = registryExecutor.getApplicationList(group);
                for (String application : applications) {
                    ElementNode applicationNode = new ElementNode(protocolType.toString(), application, null, application);
                    groupNode.add(applicationNode);
                }
            }
        }
    }

    private static void assembleConfiguration(RegistryExecutor registryExecutor, ElementNode root) throws Exception {
        List<String> groups = null;
        try {
            groups = registryExecutor.getConfigurationGroupList();
        } catch (Exception e) {
            LOG.warn("Get configuration group list failed");
        }

        if (CollectionUtils.isNotEmpty(groups)) {
            for (String group : groups) {
                ElementNode groupNode = new ElementNode(group, null, group);
                root.add(groupNode);

                List<String> applications = registryExecutor.getConfigurationApplicationList(group);
                for (String application : applications) {
                    ElementNode applicationNode = new ElementNode(application, null, application);
                    groupNode.add(applicationNode);
                }
            }
        }
    }

    public static ElementNode[] getServiceNodes(ApplicationEntity applicationEntity, ProtocolType protocolType) throws Exception {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        List<String> serviceList = RegistryContext.getRegistryExecutor(protocolType).getServiceList(application, group);

        ElementNode[] serviceNodes = new ElementNode[serviceList.size()];
        for (int i = 0; i < serviceList.size(); i++) {
            String interfaze = serviceList.get(i);
            serviceNodes[i] = new ElementNode(interfaze, interfaze, ConsoleIconFactory.getSwingIcon("netbean/application_16.png"), interfaze);
        }

        return serviceNodes;

    }

    public static ElementNode[] getReferenceNodes(ApplicationEntity applicationEntity, ProtocolType protocolType) throws Exception {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();

        List<String> referenceList = RegistryContext.getRegistryExecutor(protocolType).getReferenceList(application, group);

        ElementNode[] referenceNodes = new ElementNode[referenceList.size()];
        for (int i = 0; i < referenceList.size(); i++) {
            String interfaze = referenceList.get(i);
            referenceNodes[i] = new ElementNode(interfaze, interfaze, ConsoleIconFactory.getSwingIcon("netbean/application_16.png"), interfaze);
        }

        return referenceNodes;
    }

    public static ApplicationConfig getApplicationConfig(ProtocolType protocolType, ApplicationEntity applicationEntity) throws Exception {
        return RegistryContext.getRegistryExecutor(protocolType).retrieveApplication(applicationEntity);
    }

    public static ServiceConfig getServiceConfig(ProtocolType protocolType, String service, ApplicationEntity applicationEntity) throws Exception {
        return RegistryContext.getRegistryExecutor(protocolType).retrieveService(service, applicationEntity);
    }

    public static ReferenceConfig getReferenceConfig(ProtocolType protocolType, String reference, ApplicationEntity applicationEntity) throws Exception {
        return RegistryContext.getRegistryExecutor(protocolType).retrieveReference(reference, applicationEntity);
    }

    public static void modifyApplicationFrequency(ProtocolType protocolType, ApplicationEntity applicationEntity, int frequency) throws Exception {
        RegistryContext.getRegistryExecutor(protocolType).modifyApplicationFrequency(applicationEntity, frequency);
    }

    public static void modifyServiceToken(ProtocolType protocolType, String service, ApplicationEntity applicationEntity, long token) throws Exception {
        RegistryContext.getRegistryExecutor(protocolType).modifyServiceToken(service, applicationEntity, token);
    }

    public static void modifyServiceSecretKey(ProtocolType protocolType, String service, ApplicationEntity applicationEntity, String secretKey) throws Exception {
        RegistryContext.getRegistryExecutor(protocolType).modifyServiceSecretKey(service, applicationEntity, secretKey);
    }

    public static void modifyReferenceSecretKey(ProtocolType protocolType, String reference, ApplicationEntity applicationEntity, String secretKey) throws Exception {
        RegistryContext.getRegistryExecutor(protocolType).modifyReferenceSecretKey(reference, applicationEntity, secretKey);
    }

    public static void modifyServiceVersion(ProtocolType protocolType, String service, ApplicationEntity applicationEntity, int version) throws Exception {
        RegistryContext.getRegistryExecutor(protocolType).modifyServiceVersion(service, applicationEntity, version);
    }

    public static void modifyReferenceVersion(ProtocolType protocolType, String reference, ApplicationEntity applicationEntity, int version) throws Exception {
        RegistryContext.getRegistryExecutor(protocolType).modifyReferenceVersion(reference, applicationEntity, version);
    }

    public static void resetAll(ProtocolType protocolType) throws Exception {
        RegistryExecutor registryExecutor = RegistryContext.getRegistryExecutor(protocolType);
        List<String> groups = null;
        try {
            groups = registryExecutor.getGroupList();
        } catch (Exception e) {
            LOG.warn("Get group list failed, protocol={}", protocolType);
        }

        if (CollectionUtils.isNotEmpty(groups)) {
            for (String group : groups) {
                List<String> applications = registryExecutor.getApplicationList(group);
                for (String application : applications) {
                    ApplicationEntity applicationEntity = new ApplicationEntity();
                    applicationEntity.setApplication(application);
                    applicationEntity.setGroup(group);
                    List<String> services = registryExecutor.getServiceList(application, group);
                    registryExecutor.resetApplication(applicationEntity);
                    for (String service : services) {
                        try {
                            registryExecutor.resetService(service, applicationEntity);
                        } catch (Exception e) {
                            LOG.warn("Reset service config failed, protocol={}, application={}, group={}", protocolType, application, group);
                        }

                        try {
                            registryExecutor.resetReference(service, applicationEntity);
                        } catch (Exception e) {
                            LOG.warn("Reset reference config failed, protocol={}, application={}, group={}", protocolType, application, group);
                        }
                    }
                }
            }
        }
    }
    
    public static void deletePath(String path) throws Exception {
        ZookeeperRegistryExecutor registryExecutor = (ZookeeperRegistryExecutor) RegistryContext.getDefaultRegistryExecutor();
        registryExecutor.getInvoker().deletePath(registryExecutor.getClient(), path);
    }
}