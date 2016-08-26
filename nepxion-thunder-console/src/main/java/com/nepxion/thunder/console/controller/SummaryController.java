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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.SummaryEntity;
import com.nepxion.thunder.console.context.RegistryContext;
import com.nepxion.thunder.console.locale.ConsoleLocale;
import com.nepxion.thunder.registry.RegistryExecutor;

public class SummaryController {
    public static List<SummaryEntity> getServiceSummaries(ProtocolType protocolType) throws Exception {
        RegistryExecutor registryExecutor = RegistryContext.getRegistryExecutor(protocolType);

        List<SummaryEntity> summaryEntities = new ArrayList<SummaryEntity>();
        List<String> groups = registryExecutor.getGroupList();
        for (String group : groups) {
            List<String> applications = registryExecutor.getApplicationList(group);
            for (String application : applications) {
                ApplicationEntity applicationEntity = new ApplicationEntity();
                applicationEntity.setApplication(application);
                applicationEntity.setGroup(group);
                List<String> services = registryExecutor.getServiceList(application, group);
                for (String service : services) {
                    SummaryEntity summaryEntity = getServiceSummary(service, applicationEntity, protocolType);
                    if (summaryEntity != null) {
                        summaryEntities.add(summaryEntity);
                    }
                }
            }
        }

        return summaryEntities;
    }

    public static List<SummaryEntity> getReferenceSummaries(ProtocolType protocolType) throws Exception {
        RegistryExecutor registryExecutor = RegistryContext.getRegistryExecutor(protocolType);

        List<SummaryEntity> summaryEntities = new ArrayList<SummaryEntity>();
        List<String> groups = registryExecutor.getGroupList();
        for (String group : groups) {
            List<String> applications = registryExecutor.getApplicationList(group);
            for (String application : applications) {
                ApplicationEntity applicationEntity = new ApplicationEntity();
                applicationEntity.setApplication(application);
                applicationEntity.setGroup(group);
                List<String> references = registryExecutor.getReferenceList(application, group);
                for (String reference : references) {
                    SummaryEntity summaryEntity = getReferenceSummary(reference, applicationEntity, protocolType);
                    if (summaryEntity != null) {
                        summaryEntities.add(summaryEntity);
                    }
                }
            }
        }

        return summaryEntities;
    }

    public static SummaryEntity getServiceSummary(String interfaze, ApplicationEntity applicationEntity, ProtocolType protocolType) throws Exception {
        RegistryExecutor registryExecutor = RegistryContext.getRegistryExecutor(protocolType);

        List<ApplicationEntity> serviceInstanceList = registryExecutor.getServiceInstanceList(interfaze, applicationEntity);
        ServiceConfig serviceConfig = registryExecutor.retrieveService(interfaze, applicationEntity);

        SummaryEntity summaryEntity = new SummaryEntity();
        summaryEntity.setProtocolType(protocolType);
        summaryEntity.setApplicationType(ApplicationType.SERVICE);
        summaryEntity.setApplication(applicationEntity.getApplication());
        summaryEntity.setGroup(applicationEntity.getGroup());
        summaryEntity.setInterface(interfaze);
        if (serviceConfig != null) {
            summaryEntity.setMethods(serviceConfig.getMethods());
        }
        summaryEntity.setNotStartedDescription(ConsoleLocale.getString("not_start"));
        summaryEntity.setNotConnectedDescription(ConsoleLocale.getString("not_connect"));

        List<String> addresses = new ArrayList<String>();
        for (ApplicationEntity serviceInstance : serviceInstanceList) {
            addresses.add(serviceInstance.getHost() + ":" + serviceInstance.getPort());
        }
        summaryEntity.setAddresses(addresses);
        
        List<String> times = new ArrayList<String>();
        for (ApplicationEntity serviceInstance : serviceInstanceList) {
            times.add(new SimpleDateFormat(ThunderConstants.DATE_FORMAT).format(new Date(serviceInstance.getTime())));
        }
        summaryEntity.setTimes(times);
        
        return summaryEntity;
    }

    public static SummaryEntity getReferenceSummary(String interfaze, ApplicationEntity applicationEntity, ProtocolType protocolType) throws Exception {
        RegistryExecutor registryExecutor = RegistryContext.getRegistryExecutor(protocolType);

        List<ApplicationEntity> referenceInstanceList = registryExecutor.getReferenceInstanceList(interfaze, applicationEntity);

        SummaryEntity summaryEntity = new SummaryEntity();
        summaryEntity.setProtocolType(protocolType);
        summaryEntity.setApplicationType(ApplicationType.REFERENCE);
        summaryEntity.setApplication(applicationEntity.getApplication());
        summaryEntity.setGroup(applicationEntity.getGroup());
        summaryEntity.setInterface(interfaze);
        summaryEntity.setNotStartedDescription(ConsoleLocale.getString("not_start"));
        summaryEntity.setNotConnectedDescription(ConsoleLocale.getString("not_connect"));
        
        List<String> addresses = new ArrayList<String>();
        for (ApplicationEntity referenceInstance : referenceInstanceList) {
            addresses.add(referenceInstance.getHost() + ":" + referenceInstance.getPort());
        }
        summaryEntity.setAddresses(addresses);
        
        List<String> times = new ArrayList<String>();
        for (ApplicationEntity referenceInstance : referenceInstanceList) {
            times.add(new SimpleDateFormat(ThunderConstants.DATE_FORMAT).format(new Date(referenceInstance.getTime())));
        }
        summaryEntity.setTimes(times);

        return summaryEntity;
    }

    public static List<SummaryEntity> getMonitorSummaries() throws Exception {
        List<String> monitorInstanceList = RegistryContext.getDefaultRegistryExecutor().getMonitorInstanceList();

        List<SummaryEntity> summaryEntities = new ArrayList<SummaryEntity>();
        if (CollectionUtils.isEmpty(monitorInstanceList)) {
            return summaryEntities;
        }

        for (String monitorInstance : monitorInstanceList) {
            SummaryEntity summaryEntity = new SummaryEntity();
            summaryEntity.setHtmlStyle(false);
            summaryEntity.setAddresses(Arrays.asList(monitorInstance));
            summaryEntities.add(summaryEntity);
        }

        return summaryEntities;
    }
}