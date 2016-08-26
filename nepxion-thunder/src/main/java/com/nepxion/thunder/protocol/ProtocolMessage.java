package com.nepxion.thunder.protocol;

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

import com.nepxion.thunder.common.entity.MethodKey;

public class ProtocolMessage implements Serializable {
    private static final long serialVersionUID = 6395330487058794296L;

    protected String messageId;
    protected String traceId;
    protected String interfaze;
    protected String method;
    protected Class<?>[] parameterTypes;
    protected Object[] parameters;
    protected boolean async;
    protected String callback;
    protected long timeout;
    protected boolean broadcast;
    protected boolean heartbeat;
    protected boolean feedback;
    
    // 调用的业务处理起始时间
    protected long processStartTime;
    // 调用的业务处理结束时间
    protected long processEndTime;
    // 调用的网络传输起始时间
    protected long deliverStartTime;
    // 调用的网络传输结束时间
    protected long deliverEndTime;
    
    protected String fromCluster;
    protected String fromUrl;
    protected String toCluster;
    protected String toUrl;
    
    protected Exception exception;
    
    // 只作为MQ Selector用
    protected Object requestSource;
    protected Object responseSource;
    
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
    
    public String getInterface() {
        return interfaze;
    }

    public void setInterface(String interfaze) {
        this.interfaze = interfaze;
    }
    
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
    
    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
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

    public boolean isHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(boolean heartbeat) {
        this.heartbeat = heartbeat;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }
    
    public long getProcessStartTime() {
        return processStartTime;
    }

    public void setProcessStartTime(long processStartTime) {
        this.processStartTime = processStartTime;
    }

    public long getProcessEndTime() {
        return processEndTime;
    }

    public void setProcessEndTime(long processEndTime) {
        this.processEndTime = processEndTime;
    }
    
    public long getDeliverStartTime() {
        return deliverStartTime;
    }

    public void setDeliverStartTime(long deliverStartTime) {
        this.deliverStartTime = deliverStartTime;
    }

    public long getDeliverEndTime() {
        return deliverEndTime;
    }

    public void setDeliverEndTime(long deliverEndTime) {
        this.deliverEndTime = deliverEndTime;
    }
    
    public String getFromCluster() {
        return fromCluster;
    }

    public void setFromCluster(String fromCluster) {
        this.fromCluster = fromCluster;
    }
    
    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }
    
    public String getToCluster() {
        return toCluster;
    }

    public void setToCluster(String toCluster) {
        this.toCluster = toCluster;
    }

    public String getToUrl() {
        return toUrl;
    }

    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }
    
    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    public Object getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(Object requestSource) {
        this.requestSource = requestSource;
    }

    public Object getResponseSource() {
        return responseSource;
    }

    public void setResponseSource(Object responseSource) {
        this.responseSource = responseSource;
    }
    
    public MethodKey createMethodKey() {        
        return MethodKey.create(method, parameterTypes);
    }
}