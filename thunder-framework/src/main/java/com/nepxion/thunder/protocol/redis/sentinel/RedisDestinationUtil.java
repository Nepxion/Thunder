package com.nepxion.thunder.protocol.redis.sentinel;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.DestinationEntity;

public class RedisDestinationUtil {
    public static DestinationEntity createDestinationEntity(String interfaze, ApplicationEntity applicationEntity) {
        DestinationEntity destinationEntity = new DestinationEntity(interfaze, applicationEntity);

        return destinationEntity;
    }
}