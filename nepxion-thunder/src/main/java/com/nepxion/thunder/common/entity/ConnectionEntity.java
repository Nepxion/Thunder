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

import java.io.Serializable;

public class ConnectionEntity implements Serializable {
    private static final long serialVersionUID = 2902789635426674363L;

    private ApplicationEntity applicationEntity;

    // HTTP连接的Url，不适用TCP，MQ等方式
    private String url;

    // 客户端连接句柄(例如：Netty是ChannelFuture，Hessian是Proxy)
    private Object connectionHandler;

    public ApplicationEntity getApplicationEntity() {
        return applicationEntity;
    }

    public void setApplicationEntity(ApplicationEntity applicationEntity) {
        this.applicationEntity = applicationEntity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getConnectionHandler() {
        return connectionHandler;
    }

    public void setConnectionHandler(Object connectionHandler) {
        this.connectionHandler = connectionHandler;
    }
    
    public boolean isAvailable() {
        return applicationEntity != null && connectionHandler != null;
    }
    
    // 调试用
    public String getEntityString() {
        StringBuilder builder = new StringBuilder();
        builder.append("applicationEntity=");
        builder.append(applicationEntity);
        builder.append(", url=");
        builder.append(url);
        builder.append(", connectionHandler=");
        builder.append(connectionHandler);
        
        return builder.toString();
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (applicationEntity != null) {
            result = 37 * result + applicationEntity.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConnectionEntity)) {
            return false;
        }

        ConnectionEntity connectionEntity = (ConnectionEntity) object;
        if (this.applicationEntity.equals(connectionEntity.applicationEntity)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("applicationEntity=");
        builder.append(applicationEntity);

        return builder.toString();
    }
}