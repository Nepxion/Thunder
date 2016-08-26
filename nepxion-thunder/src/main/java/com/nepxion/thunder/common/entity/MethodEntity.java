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

import com.nepxion.thunder.common.callback.ThunderCallback;

public class MethodEntity implements Serializable {
    private static final long serialVersionUID = 510490348802294208L;

    private String method;
    private String parameterTypes;
    private int traceIdIndex = 0;
    private boolean async;
    private long timeout;
    private boolean broadcast;
    private ThunderCallback<?> callback;
    private CallbackType callbackType;
    
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String parameterTypes) {
        if (StringUtils.isNotEmpty(parameterTypes)) {
            parameterTypes = parameterTypes.replace(" ", "");
        }
        this.parameterTypes = parameterTypes;
    }
    
    public int getTraceIdIndex() {
        return traceIdIndex;
    }

    public void setTraceIdIndex(int traceIdIndex) {
        this.traceIdIndex = traceIdIndex;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }
    
    public ThunderCallback<?> getCallback() {
        return callback;
    }

    public void setCallback(ThunderCallback<?> callback) {
        this.callback = callback;
    }
    
    public CallbackType getCallbackType() {
        return callbackType;
    }

    public void setCallbackType(CallbackType callbackType) {
        this.callbackType = callbackType;
    }
    
    public boolean isCallback() {
        return callback != null || callbackType != null;
    }
    
    public MethodEntity clone() {
        MethodEntity methodEntity = new MethodEntity();
        
        methodEntity.setMethod(method);
        methodEntity.setParameterTypes(parameterTypes);
        methodEntity.setTraceIdIndex(traceIdIndex);
        methodEntity.setAsync(async);
        methodEntity.setTimeout(timeout);
        methodEntity.setBroadcast(broadcast);
        methodEntity.setCallback(callback);
        methodEntity.setCallbackType(callbackType);

        return methodEntity;
    }

    @Override
    public int hashCode() {
        int result = 17;
        
        if (method != null) {
            result = 37 * result + method.hashCode();
        }
        
        if (parameterTypes != null) {
            result = 37 * result + parameterTypes.hashCode();
        }
        
        result = 37 * result + traceIdIndex;
        result = 37 * result + (async ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode());
        result = 37 * result + String.valueOf(timeout).hashCode();
        result = 37 * result + (broadcast ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode());
        if (callbackType != null) {
            result = 37 * result + callbackType.hashCode();
        }
        
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MethodEntity)) {
            return false;
        }
        
        MethodEntity methodEntity = (MethodEntity) object;
        if (StringUtils.equals(this.method, methodEntity.method)
                && StringUtils.equals(this.parameterTypes, methodEntity.parameterTypes)
                && this.traceIdIndex == methodEntity.traceIdIndex				
                && Boolean.valueOf(this.async).equals(Boolean.valueOf(methodEntity.async))
                && this.timeout == methodEntity.timeout
                && Boolean.valueOf(this.broadcast).equals(Boolean.valueOf(methodEntity.broadcast))
                && this.callbackType == methodEntity.callbackType) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("method=");
        builder.append(method);
        builder.append(", parameterTypes=");
        builder.append(parameterTypes);
        builder.append(", traceIdIndex=");
        builder.append(traceIdIndex);
        builder.append(", async=");
        builder.append(async);
        builder.append(", timeout=");
        builder.append(timeout);
        builder.append(", broadcast=");
        builder.append(broadcast);
        builder.append(", callback=");
        builder.append(callback);
        builder.append(", callbackType=");
        builder.append(callbackType);

        return builder.toString();
    }
}