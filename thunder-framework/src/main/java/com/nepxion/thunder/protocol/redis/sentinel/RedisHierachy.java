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

public class RedisHierachy {
    public String createChannel(String interfaze, ApplicationEntity applicationEntity) {
        return RedisDestinationUtil.createDestinationEntity(interfaze, applicationEntity).toString();
    }
}