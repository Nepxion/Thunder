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

public class ApplicationEntity implements Serializable {
    private static final long serialVersionUID = 580343173350443705L;

    private String id;
    private String application;
    private String group;
    private String cluster;
    private String host;
    private int port;
    private long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    
    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public String toUrl() {
        return host + ":" + port;
    }
    
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int result = 17;
        
        if (application != null) {
            result = 37 * result + application.hashCode();
        }

        if (group != null) {
            result = 37 * result + group.hashCode();
        }
        
        if (cluster != null) {
            result = 37 * result + cluster.hashCode();
        }

        if (host != null) {
            result = 37 * result + host.hashCode();
        }
        
        result = 37 * result + port;

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApplicationEntity)) {
            return false;
        }

        ApplicationEntity applicationEntity = (ApplicationEntity) object;
        if (StringUtils.equals(this.application, applicationEntity.application)
                && StringUtils.equals(this.group, applicationEntity.group)
                && StringUtils.equals(this.cluster, applicationEntity.cluster)
                && StringUtils.equals(this.host, applicationEntity.host)
                && this.port == applicationEntity.port) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("application=");
        builder.append(application);
        builder.append(", group=");
        builder.append(group);
        builder.append(", cluster=");
        builder.append(cluster);
        builder.append(", host=");
        builder.append(host);
        builder.append(", port=");
        builder.append(port);

        return builder.toString();
    }
}