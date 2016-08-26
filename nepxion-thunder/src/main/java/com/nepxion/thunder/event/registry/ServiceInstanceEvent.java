package com.nepxion.thunder.event.registry;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;

import com.nepxion.thunder.common.entity.ApplicationEntity;

public class ServiceInstanceEvent extends InstanceEvent {
    public ServiceInstanceEvent(InstanceEventType eventType, String interfaze, ApplicationEntity applicationEntity, List<ApplicationEntity> applicationEntityList) {
        super(eventType, interfaze, applicationEntity, applicationEntityList);
    }
    
    @Override
    public String toString() {
        return getEventName();
    }
    
    public static String getEventName() {
        return ServiceInstanceEvent.class.getName(); 
    }
}