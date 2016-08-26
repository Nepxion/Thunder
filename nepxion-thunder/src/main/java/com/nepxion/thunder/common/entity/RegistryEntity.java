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

public class RegistryEntity implements Serializable {
    private static final long serialVersionUID = -7164716570644944474L;
    
    private RegistryType type;
    private String address;
    private PropertyType propertyType;
    
    public RegistryType getType() {
        return type;
    }

    public void setType(RegistryType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    @Override
    public int hashCode() {
        int result = 17;

        if (type != null) {
            result = 37 * result + type.hashCode();
        }

        if (address != null) {
            result = 37 * result + address.hashCode();
        }
        
        if (propertyType != null) {
            result = 37 * result + propertyType.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RegistryEntity)) {
            return false;
        }

        RegistryEntity registryEntity = (RegistryEntity) object;
        if (this.type == registryEntity.type
                && StringUtils.equals(this.address, registryEntity.address)
                && this.propertyType == registryEntity.propertyType) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("type=");
        builder.append(type);
        builder.append(", address=");
        builder.append(address);
        builder.append(", propertyType=");
        builder.append(propertyType);

        return builder.toString();
    }
}