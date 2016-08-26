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
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonitorStat implements Serializable {
    private static final long serialVersionUID = 4890301457045630258L;

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String MESSAGE_TYPE_REQUEST = "Request";
    public static final String MESSAGE_TYPE_RESPONSE = "Response";

    private String traceId;
    private String messageId;
    private String messageType;
    
    private String fromCluster;
    private String fromUrl;
    private String toCluster;
    private String toUrl;
    
    // 调用的业务处理起始时间
    private long processStartTime;
    // 调用的业务处理结束时间
    private long processEndTime;
    // 调用的网络传输起始时间
    private long deliverStartTime;
    // 调用的网络传输结束时间
    private long deliverEndTime;
    
    private String interfaze;
    private String method;
    private String parameterTypes;
    private String protocol;
    private String application;
    private String group;
    private boolean async;
    private String callback;
    private long timeout;
    private boolean broadcast;
    private String loadBalance;
    private boolean feedback;
    private String exception;
    
    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
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
    
    public String getInterfaze() {
        return interfaze;
    }

    public void setInterfaze(String interfaze) {
        this.interfaze = interfaze;
    }

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
        this.parameterTypes = parameterTypes;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
    
    public String getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }
    
    public boolean isFeedback() {
        return feedback;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("traceId=");
        builder.append(traceId);
        builder.append(", messageId=");
        builder.append(messageId);
        builder.append(", messageType=");
        builder.append(messageType);
        builder.append(", fromCluster=");
        builder.append(fromCluster);
        builder.append(", fromUrl=");
        builder.append(fromUrl);
        builder.append(", toCluster=");
        builder.append(toCluster);
        builder.append(", toUrl=");
        builder.append(toUrl);
        builder.append(", processStartTime=");
        builder.append(new SimpleDateFormat(DATE_FORMAT).format(new Date(processStartTime)));
        builder.append(", processEndTime=");
        builder.append(new SimpleDateFormat(DATE_FORMAT).format(new Date(processEndTime)));
        builder.append(", deliverStartTime=");
        builder.append(new SimpleDateFormat(DATE_FORMAT).format(new Date(deliverStartTime)));
        builder.append(", deliverEndTime=");
        builder.append(new SimpleDateFormat(DATE_FORMAT).format(new Date(deliverEndTime)));
        builder.append(", processedTime=");
        builder.append((processEndTime - processStartTime) + " ms");
        builder.append(", deliveredTime=");
        builder.append((deliverEndTime - deliverStartTime) + " ms");
        builder.append(", totalTime=");
        builder.append((processEndTime - processStartTime + deliverEndTime - deliverStartTime) + " ms");
        builder.append(", interface=");
        builder.append(interfaze);
        builder.append(", method=");
        builder.append(method);
        builder.append(", parameterTypes=");
        builder.append(parameterTypes);
        builder.append(", protocol=");
        builder.append(protocol);
        builder.append(", application=");
        builder.append(application);
        builder.append(", group=");
        builder.append(group);
        builder.append(", async=");
        builder.append(async);
        builder.append(", callback=");
        builder.append(callback);
        builder.append(", timeout=");
        builder.append(timeout);
        builder.append(", broadcast=");
        builder.append(broadcast);
        builder.append(", loadBalance=");
        builder.append(loadBalance);
        builder.append(", feedback=");
        builder.append(feedback);

        return builder.toString();
    }
}