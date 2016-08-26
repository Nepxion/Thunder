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
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;

public class ServiceEntity implements Serializable {
    private static final long serialVersionUID = -2752198792180714196L;

    private String interfaze;
    private Object service;
    private String server;

    private AtomicLong defaultToken = new AtomicLong(0);
    private AtomicLong token = new AtomicLong(0);

    public String getInterface() {
        return interfaze;
    }

    public void setInterface(String interfaze) {
        this.interfaze = interfaze;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public AtomicLong getDefaultToken() {
        return defaultToken;
    }

    public void setDefaultToken(long defaultToken) {
        this.defaultToken.set(defaultToken);
    }

    public AtomicLong getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token.set(token);
    }

    public void resetToken() {
        this.token.set(defaultToken.get());
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (interfaze != null) {
            result = 37 * result + interfaze.hashCode();
        }

        if (service != null) {
            result = 37 * result + service.hashCode();
        }

        if (server != null) {
            result = 37 * result + server.hashCode();
        }

        result = 37 * result + defaultToken.hashCode();
        result = 37 * result + token.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ServiceEntity)) {
            return false;
        }

        ServiceEntity serviceEntity = (ServiceEntity) object;
        if (StringUtils.equals(this.interfaze, serviceEntity.interfaze)
                && (this.service == null ? serviceEntity.service == null : this.service.equals(serviceEntity.service))
                && StringUtils.equals(this.server, serviceEntity.server)
                && this.defaultToken.get() == serviceEntity.defaultToken.get()
                && this.token.get() == serviceEntity.token.get()) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("interface=");
        builder.append(interfaze);
        builder.append(", service=");
        builder.append(service);
        builder.append(", server=");
        builder.append(server);
        builder.append(", defaultToken=");
        builder.append(defaultToken.get());
        builder.append(", token=");
        builder.append(token.get());

        return builder.toString();
    }
}