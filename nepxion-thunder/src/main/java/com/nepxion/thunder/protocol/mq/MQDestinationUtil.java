package com.nepxion.thunder.protocol.mq;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import javax.jms.Destination;

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.DestinationEntity;
import com.nepxion.thunder.common.entity.DestinationType;
import com.nepxion.thunder.common.util.ClassUtil;

public class MQDestinationUtil {
    public static Destination createDestination(String destinationClass, DestinationType destinationType, String interfaze, ApplicationEntity applicationEntity) throws Exception {
        DestinationEntity destinationEntity = new DestinationEntity(destinationType, interfaze, applicationEntity);

        return ClassUtil.createInstance(destinationClass, destinationEntity.toString());
    }
}