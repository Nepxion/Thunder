package com.nepxion.thunder.cluster.loadbalance;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.ConnectionEntity;

public interface LoadBalanceExecutor extends ThunderDelegate {  
    // 负载均衡
    ConnectionEntity loadBalance(String interfaze) throws Exception;
}