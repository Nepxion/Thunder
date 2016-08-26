package com.nepxion.thunder.common.entity;

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
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ConnectionCacheEntity {
    private Map<String, List<ConnectionEntity>> connectionEntityMap;
    
    public ConnectionCacheEntity() {
        connectionEntityMap = Maps.newConcurrentMap();
    }
    
    public synchronized Map<String, List<ConnectionEntity>> getConnectionEntityMap() {        
        return connectionEntityMap;
    }
    
    public synchronized List<ConnectionEntity> getConnectionEntityList(String interfaze) {        
        return connectionEntityMap.get(interfaze);
    }
    
    // 根据Interface获取应用实体的列表
    public synchronized List<ApplicationEntity> getApplicationEntityList(String interfaze) {
        List<ApplicationEntity> applicationEntityList = new ArrayList<ApplicationEntity>();
        
        List<ConnectionEntity> connectionEntityList = retrieveConnectionEntityList(interfaze);
        for (ConnectionEntity connectionEntity : connectionEntityList) {
            ApplicationEntity applicationEntity = connectionEntity.getApplicationEntity();
            if (!applicationEntityList.contains(applicationEntity)) {
                applicationEntityList.add(applicationEntity);
            }
        }
        
        return applicationEntityList;
    }
    
    // 复制已存在的连接实体
    public synchronized void duplicateConnectionEntity(String interfaze, ApplicationEntity applicationEntity) {
        ConnectionEntity connectionEntity = getConnectionEntity(applicationEntity);
        online(interfaze, connectionEntity);
    }
    
    // 判断缓存是否含有上下线信息，如果contains==true，说明上线，反之下线
    // 对于TCP调用，多个Interface共享一个通道
    // 对于HTTP调用，单个Interface占用一个连接
    public synchronized boolean contains(String interfaze, ApplicationEntity applicationEntity) {
        if (StringUtils.isNotEmpty(interfaze)) {
            return getConnectionEntity(interfaze, applicationEntity) != null;
        } else {
            return getConnectionEntity(applicationEntity) != null;
        }
    }
    
    // 客户端连接上线，加入缓存
    public synchronized void online(String interfaze, ConnectionEntity connectionEntity) {
        List<ConnectionEntity> connectionEntityList = retrieveConnectionEntityList(interfaze);
        if (!connectionEntityList.contains(connectionEntity)) {
            connectionEntityList.add(connectionEntity);
        }
    }
    
    // 客户端连接下线，移出缓存
    public synchronized void offline(String interfaze, ApplicationEntity applicationEntity) {
        if (StringUtils.isNotEmpty(interfaze)) {
            removeConnectionEntity(interfaze, applicationEntity);
        } else {
            removeConnectionEntity(applicationEntity);
        }
    }
    
    private synchronized ConnectionEntity getConnectionEntity(String interfaze, ApplicationEntity applicationEntity) {
        List<ConnectionEntity> connectionEntityList = retrieveConnectionEntityList(interfaze);
        if (CollectionUtils.isEmpty(connectionEntityList)) {
            return null;
        }

        for (ConnectionEntity connectionEntity : connectionEntityList) {
            ApplicationEntity entity = connectionEntity.getApplicationEntity();
            if (entity.equals(applicationEntity)) {
                return connectionEntity;
            }
        }
        
        return null;
    }
    
    private synchronized ConnectionEntity getConnectionEntity(ApplicationEntity applicationEntity) {
        for (Map.Entry<String, List<ConnectionEntity>> entry : connectionEntityMap.entrySet()) {
            List<ConnectionEntity> connectionEntityList = entry.getValue();
            for (ConnectionEntity connectionEntity : connectionEntityList) {
                ApplicationEntity entity = connectionEntity.getApplicationEntity();
                if (entity.equals(applicationEntity)) {
                    return connectionEntity;
                }
            }
        }
        
        return null;
    }
    
    private void removeConnectionEntity(String interfaze, ApplicationEntity applicationEntity) {
        List<ConnectionEntity> connectionEntityList = retrieveConnectionEntityList(interfaze);
        if (CollectionUtils.isEmpty(connectionEntityList)) {
            return;
        }

        for (ConnectionEntity connectionEntity : connectionEntityList) {
            ApplicationEntity entity = connectionEntity.getApplicationEntity();
            if (entity.equals(applicationEntity)) {
                connectionEntityList.remove(connectionEntity);
                
                break;
            }
        }
    }
    
    private void removeConnectionEntity(ApplicationEntity applicationEntity) {
        for (Map.Entry<String, List<ConnectionEntity>> entry : connectionEntityMap.entrySet()) {
            List<ConnectionEntity> connectionEntityList = entry.getValue();
            for (ConnectionEntity connectionEntity : connectionEntityList) {
                ApplicationEntity entity = connectionEntity.getApplicationEntity();
                if (entity.equals(applicationEntity)) {
                    connectionEntityList.remove(connectionEntity);
                    
                    break;
                }
            }
        }
    }
    
    // 获取ConnectionEntityList，如果是null，塞入一个新的List并返回它
    private synchronized List<ConnectionEntity> retrieveConnectionEntityList(String interfaze) {
        List<ConnectionEntity> connectionEntityList = getConnectionEntityList(interfaze);
        if (connectionEntityList == null) {
            connectionEntityList = Lists.newCopyOnWriteArrayList();
            connectionEntityMap.put(interfaze, connectionEntityList);
        }

        return connectionEntityList;
    }
}