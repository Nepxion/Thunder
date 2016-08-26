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

public interface BInterface1 {
    // 异步调用
    // B端收到A端消息，并发送到C端
    void asyncToB(String traceId, String path);
    
    // 同步调用
    // B端收到A端消息，并发送到C端
    // B端接受C端的返回值，并返回给A端
    String syncToB(String traceId, String path);
    
    // 链式调用：异步Callback调用
    // B端收到A端消息，并返回Callback给A端
    // A端在Callback里调用C端
    // C端收到A端消息，并返回Callback给A端
    String[] async1ToB(String traceId, String path);
    
    // 先异步后同步调用
    // 先异步：B端收到A端消息，并发送到C端
    // 后同步：B端接受C端的返回值，并返回给A端
    void async2ToB(String traceId, String path);
    
    // 链式调用：同步调用
    // B端收到A端消息，并返回给A端
    // A端接受B端的返回值，调用C端
    // C端收到A端消息，并返回给A端
    String[] sync1ToB(String[] result);
}