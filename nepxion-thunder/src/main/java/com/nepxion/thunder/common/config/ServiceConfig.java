package com.nepxion.thunder.common.config;

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
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ServiceConfig implements Serializable {
    private static final long serialVersionUID = 5777064003531668211L;

    private String interfaze;
    private List<String> methods;
    private String secretKey;
    private int version;
    private long token;

    private byte[] lock = new byte[0];

    public String getInterface() {
        return interfaze;
    }

    public void setInterface(String interfaze) {
        this.interfaze = interfaze;
    }
    
    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        synchronized (lock) {
            this.secretKey = secretKey;
        }
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        synchronized (lock) {
            this.version = version;
        }
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        synchronized (lock) {
            this.token = token;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (interfaze != null) {
            result = 37 * result + interfaze.hashCode();
        }
        
        if (methods != null) {
            result = 37 * result + methods.hashCode();
        }

        if (secretKey != null) {
            result = 37 * result + secretKey.hashCode();
        }

        result = 37 * result + version;
        result = 37 * result + String.valueOf(token).hashCode();

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ServiceConfig)) {
            return false;
        }

        ServiceConfig serviceConfig = (ServiceConfig) object;
        if (StringUtils.equals(this.interfaze, serviceConfig.interfaze)
                && StringUtils.equals(this.secretKey, serviceConfig.secretKey)
                && CollectionUtils.isEqualCollection(this.methods, serviceConfig.getMethods())
                && this.version == serviceConfig.version
                && this.token == serviceConfig.token) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("interface=");
        builder.append(interfaze);
        builder.append(", methods=");
        builder.append(methods);
        builder.append(", secretKey=");
        builder.append(secretKey);
        builder.append(", version=");
        builder.append(version);
        builder.append(", token=");
        builder.append(token);

        return builder.toString();
    }
}