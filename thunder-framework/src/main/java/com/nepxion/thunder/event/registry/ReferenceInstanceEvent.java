package com.nepxion.thunder.event.registry;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;

import com.nepxion.thunder.common.entity.ApplicationEntity;

public class ReferenceInstanceEvent extends InstanceEvent {
    private static final long serialVersionUID = -4374144609470814666L;

    public ReferenceInstanceEvent(InstanceEventType eventType, String interfaze, ApplicationEntity applicationEntity, List<ApplicationEntity> applicationEntityList) {
        super(eventType, interfaze, applicationEntity, applicationEntityList);
    }

    @Override
    public String toString() {
        return getEventName();
    }

    public static String getEventName() {
        return ReferenceInstanceEvent.class.getName();
    }
}