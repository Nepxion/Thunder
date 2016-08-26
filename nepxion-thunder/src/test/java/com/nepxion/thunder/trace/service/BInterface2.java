package com.nepxion.thunder.trace.service;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public interface BInterface2 {    
    // 异步调用
    // B端收到C端消息，并发送到A端
    void asyncToB(String traceId, String path);
}