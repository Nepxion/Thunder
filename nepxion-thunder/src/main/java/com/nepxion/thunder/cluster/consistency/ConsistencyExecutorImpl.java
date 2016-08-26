package com.nepxion.thunder.cluster.consistency;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.event.registry.InstanceEventType;
import com.nepxion.thunder.event.registry.ServiceInstanceEvent;
import com.nepxion.thunder.protocol.ClientExecutor;

public class ConsistencyExecutorImpl extends ThunderDelegateImpl implements ConsistencyExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ConsistencyExecutorImpl.class);

    private byte[] lock = new byte[0];
    
    @Override
    public void consist(ServiceInstanceEvent event) throws Exception {
        synchronized (lock) {
            consistOnce(event);
        }
    }
    
    protected void consistOnce(ServiceInstanceEvent event) {
        InstanceEventType eventType = event.getEventType();
        String interfaze = event.getInterface();
        ApplicationEntity applicationEntity = event.getApplicationEntity();

        try {
            switch (eventType) {
                case ONLINE:
                    consistClient(interfaze, applicationEntity, true);
                    LOG.info("服务 [{}] 上线 ：[{}]", interfaze, applicationEntity);
                    break;
                case OFFLINE:
                    consistClient(interfaze, applicationEntity, false);
                    LOG.info("服务 [{}] 下线 ：[{}]", interfaze, applicationEntity);
                    break;
            }
        } catch (Exception e) {
            LOG.error("Registry Center update failed", e);
        }
        
        LOG.info("------------------------ 注册中心消息 ------------------------");
   
        summary(interfaze);

        LOG.info("------------------------------------------------------------");
    }
    
    protected void consistBatch(ServiceInstanceEvent event) throws Exception {
        InstanceEventType eventType = event.getEventType();
        String interfaze = event.getInterface();
        ApplicationEntity applicationEntity = event.getApplicationEntity();
        List<ApplicationEntity> applicationEntityList = event.getApplicationEntityList();

        switch (eventType) {
            case ONLINE:
                LOG.info("服务 [{}] 上线 ：[{}]", interfaze, applicationEntity);
                break;
            case OFFLINE:
                LOG.info("服务 [{}] 下线 ：[{}]", interfaze, applicationEntity);
                break;
        }

        try {
            consistBatch(interfaze, applicationEntityList);
        } catch (Exception e) {
            LOG.error("Registry Center update failed", e);
        }

        LOG.info("------------------------ 注册中心消息 ------------------------");

        summary(interfaze);

        LOG.info("------------------------------------------------------------");
    }
    
    private void consistBatch(String interfaze, List<ApplicationEntity> remoteList) throws Exception {
        List<ApplicationEntity> localList = cacheContainer.getConnectionCacheEntity().getApplicationEntityList(interfaze);
        if (!CollectionUtils.isEqualCollection(localList, remoteList)) {
            List<ApplicationEntity> intersectedList = (List<ApplicationEntity>) CollectionUtils.intersection(localList, remoteList);
            List<ApplicationEntity> onlineList = (List<ApplicationEntity>) CollectionUtils.subtract(remoteList, intersectedList);
            List<ApplicationEntity> offlineList = (List<ApplicationEntity>) CollectionUtils.subtract(localList, intersectedList);

            consistBatchClient(interfaze, onlineList, true);
            consistBatchClient(interfaze, offlineList, false);
        }
    }
    
    private void consistBatchClient(String interfaze, List<ApplicationEntity> changedList, boolean online) throws Exception {
        for (ApplicationEntity applicationEntity : changedList) {
            consistClient(interfaze, applicationEntity, online);
        }
    }
    
    private void consistClient(String interfaze, ApplicationEntity applicationEntity, boolean online) throws Exception {
        ClientExecutor clientExecutor = executorContainer.getClientExecutor();
        if (online) {
            clientExecutor.start(interfaze, applicationEntity);
        } else {
            clientExecutor.offline(interfaze, applicationEntity);
        }
    }

    private void summary(String interfaze) {
        List<ApplicationEntity> applicationEntityList = cacheContainer.getConnectionCacheEntity().getApplicationEntityList(interfaze);
        if (CollectionUtils.isEmpty(applicationEntityList)) {
            LOG.info("服务 [{}] 全部下线", interfaze);

            return;
        }

        LOG.info("服务 [{}] 在线概况:", interfaze);
        int i = 1;
        for (ApplicationEntity entity : applicationEntityList) {
            LOG.info(i + ". [{}]", entity);
            i++;
        }
    }
}