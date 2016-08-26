package com.nepxion.thunder.protocol;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.aopalliance.intercept.MethodInterceptor;

import com.nepxion.thunder.common.delegate.ThunderDelegate;

public interface ClientInterceptor extends ThunderDelegate, MethodInterceptor {
    // 设置要调用的接口
    void setInterface(String interfaze);
    
    // 异步调用
    void invokeAsync(ProtocolRequest request) throws Exception;

    // 同步调用
    Object invokeSync(ProtocolRequest request) throws Exception;

    // 广播调用
    void invokeBroadcast(ProtocolRequest request) throws Exception;
}