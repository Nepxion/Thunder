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

public class DestinationEntity implements Serializable {
    private static final long serialVersionUID = 7659819277817309758L;
    
    private DestinationType destinationType;
    private String interfaze;
    private ApplicationEntity applicationEntity;
    
    public DestinationEntity(String interfaze, ApplicationEntity applicationEntity) {
        this(null, interfaze, applicationEntity);
    }
    
    public DestinationEntity(DestinationType destinationType, String interfaze, ApplicationEntity applicationEntity) {
        this.destinationType = destinationType;
        this.interfaze = interfaze;
        this.applicationEntity = applicationEntity;
    }
    
    @Override
    public int hashCode() {
        int result = 17;

        if (destinationType != null) {
            result = 37 * result + destinationType.hashCode();
        }

        if (interfaze != null) {
            result = 37 * result + interfaze.hashCode();
        }

        if (applicationEntity != null) {
            result = 37 * result + applicationEntity.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DestinationEntity)) {
            return false;
        }

        DestinationEntity destinationEntity = (DestinationEntity) object;
        if (this.destinationType == destinationEntity.destinationType
                && StringUtils.equals(this.interfaze, destinationEntity.interfaze)
                && this.applicationEntity == destinationEntity.applicationEntity) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String application = applicationEntity.getApplication();
        String group = applicationEntity.getGroup();
        
        StringBuilder builder = new StringBuilder();
        if (destinationType != null) {
            builder.append(destinationType);
            builder.append("-");
        }
        builder.append(group);
        builder.append("-");
        builder.append(application);
        builder.append("-");
        builder.append(interfaze);
        
        return builder.toString();
    }
}