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

import org.apache.commons.lang3.StringUtils;

public class ProtocolEntity implements Serializable {
    private static final long serialVersionUID = 6618175017107274953L;

    private ProtocolType type;

    private String serverExecutorId;
    private String clientExecutorId;
    private String clientInterceptorId;

    public ProtocolType getType() {
        return type;
    }

    public void setType(ProtocolType type) {
        this.type = type;
    }

    public String getServerExecutorId() {
        return serverExecutorId;
    }

    public void setServerExecutorId(String serverExecutorId) {
        this.serverExecutorId = serverExecutorId;
    }

    public String getClientExecutorId() {
        return clientExecutorId;
    }

    public void setClientExecutorId(String clientExecutorId) {
        this.clientExecutorId = clientExecutorId;
    }

    public String getClientInterceptorId() {
        return clientInterceptorId;
    }

    public void setClientInterceptorId(String clientInterceptorId) {
        this.clientInterceptorId = clientInterceptorId;
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (type != null) {
            result = 37 * result + type.hashCode();
        }

        if (serverExecutorId != null) {
            result = 37 * result + serverExecutorId.hashCode();
        }

        if (clientExecutorId != null) {
            result = 37 * result + clientExecutorId.hashCode();
        }

        if (clientInterceptorId != null) {
            result = 37 * result + clientInterceptorId.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProtocolEntity)) {
            return false;
        }

        ProtocolEntity protocolEntity = (ProtocolEntity) object;
        if (this.type == protocolEntity.type
                && StringUtils.equals(this.serverExecutorId, protocolEntity.serverExecutorId)
                && StringUtils.equals(this.clientExecutorId, protocolEntity.clientExecutorId)
                && StringUtils.equals(this.clientInterceptorId, protocolEntity.clientInterceptorId)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("type=");
        builder.append(type);
        builder.append(", serverExecutorId=");
        builder.append(serverExecutorId);
        builder.append(", clientExecutorId=");
        builder.append(clientExecutorId);
        builder.append(", clientInterceptorId=");
        builder.append(clientInterceptorId);
        
        return builder.toString();
    }
}