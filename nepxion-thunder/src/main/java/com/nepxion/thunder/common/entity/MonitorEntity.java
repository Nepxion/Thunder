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
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class MonitorEntity implements Serializable {
    private static final long serialVersionUID = 631345745107842496L;
    
    private byte[] lock = new byte[0];
    
    private List<MonitorType> types;
    private List<String> addresses;

    public List<MonitorType> getTypes() {
        return types;
    }

    public void setTypes(List<MonitorType> types) {
        this.types = types;
    }
    
    public List<String> getAddresses() {
        synchronized (lock) {
            return addresses;
        }
    }
    
    public void setAddresses(List<String> addresses) {
        synchronized (lock) {
            this.addresses = addresses;
        }
    }

    public void addAddress(String address) {
        synchronized (lock) {
            if (!addresses.contains(address)) {
                addresses.add(address);
            }
        }
    }
    
    public void removeAddress(String address) {
        synchronized (lock) {
            if (addresses.contains(address)) {
                addresses.remove(address);
            }
        }
    }
    
    public boolean hasWebService() {
        for (MonitorType type : types) {
            if (type == MonitorType.WEB_SERVICE) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (types != null) {
            result = 37 * result + types.hashCode();
        }

        if (addresses != null) {
            result = 37 * result + addresses.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MonitorEntity)) {
            return false;
        }

        MonitorEntity monitorEntity = (MonitorEntity) object;
        if (CollectionUtils.isEqualCollection(this.types, monitorEntity.types)
                && CollectionUtils.isEqualCollection(this.addresses, monitorEntity.addresses)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("types=");
        builder.append(types);
        builder.append(", addresses=");
        builder.append(addresses);

        return builder.toString();
    }
}