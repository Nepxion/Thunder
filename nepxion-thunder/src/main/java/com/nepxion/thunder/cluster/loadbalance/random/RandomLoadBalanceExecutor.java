package com.nepxion.thunder.cluster.loadbalance.random;

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

import com.nepxion.thunder.cluster.loadbalance.AbstractLoadBalanceExecutor;
import com.nepxion.thunder.common.entity.ConnectionEntity;
import com.nepxion.thunder.common.util.RandomUtil;

public class RandomLoadBalanceExecutor extends AbstractLoadBalanceExecutor {

    @Override
    protected ConnectionEntity loadBalance(String interfaze, List<ConnectionEntity> connectionEntityList) throws Exception {
        int random = RandomUtil.threadLocalRandom(connectionEntityList.size());
        // int random = RandomUtil.random(0, connectionEntityList.size() - 1);

        return connectionEntityList.get(random);
    }
}