package com.nepxion.thunder.protocol.kafka;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.DestinationEntity;
import com.nepxion.thunder.common.entity.DestinationType;

public class KafkaMQDestinationUtil {
    public static DestinationEntity createDestinationEntity(DestinationType destinationType, String interfaze, ApplicationEntity applicationEntity) throws Exception {
        DestinationEntity destinationEntity = new DestinationEntity(destinationType, interfaze, applicationEntity);

        return destinationEntity;
    }
    
    public static DestinationEntity createDestinationEntity(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        DestinationEntity destinationEntity = new DestinationEntity(interfaze, applicationEntity);

        return destinationEntity;
    }
}