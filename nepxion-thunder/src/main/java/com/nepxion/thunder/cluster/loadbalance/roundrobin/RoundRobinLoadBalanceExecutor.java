package com.nepxion.thunder.cluster.loadbalance.roundrobin;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Maps;
import com.nepxion.thunder.cluster.loadbalance.AbstractLoadBalanceExecutor;
import com.nepxion.thunder.common.entity.ConnectionEntity;

public class RoundRobinLoadBalanceExecutor extends AbstractLoadBalanceExecutor {
    private Map<String, AtomicLong> indexMap = Maps.newConcurrentMap();

    @Override
    protected ConnectionEntity loadBalance(String interfaze, List<ConnectionEntity> connectionEntityList) throws Exception {        
        AtomicLong atomicLong = indexMap.get(interfaze);
        if (atomicLong == null) {
            atomicLong = new AtomicLong(0);
            indexMap.put(interfaze, atomicLong);
        }

        int index = (int) (atomicLong.getAndAdd(1) % connectionEntityList.size());

        return connectionEntityList.get(Math.abs(index));
    }
}