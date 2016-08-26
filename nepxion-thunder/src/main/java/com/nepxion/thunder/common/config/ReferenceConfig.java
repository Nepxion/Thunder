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

import org.apache.commons.lang3.StringUtils;

public class ReferenceConfig implements Serializable {
    private static final long serialVersionUID = -4278894097968838119L;

    private String interfaze;
    private String secretKey;
    private int version;

    private byte[] lock = new byte[0];

    public String getInterface() {
        return interfaze;
    }

    public void setInterface(String interfaze) {
        this.interfaze = interfaze;
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

    @Override
    public int hashCode() {
        int result = 17;

        if (interfaze != null) {
            result = 37 * result + interfaze.hashCode();
        }

        if (secretKey != null) {
            result = 37 * result + secretKey.hashCode();
        }

        result = 37 * result + version;

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReferenceConfig)) {
            return false;
        }

        ReferenceConfig referenceConfig = (ReferenceConfig) object;
        if (StringUtils.equals(this.interfaze, referenceConfig.interfaze)
                && StringUtils.equals(this.secretKey, referenceConfig.secretKey)
                && this.version == referenceConfig.version) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("interface=");
        builder.append(interfaze);
        builder.append(", secretKey=");
        builder.append(secretKey);
        builder.append(", version=");
        builder.append(version);

        return builder.toString();
    }
}