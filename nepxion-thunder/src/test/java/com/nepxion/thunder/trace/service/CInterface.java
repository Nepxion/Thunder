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

public interface CInterface {    
    // 异步调用
    // C端收到B端消息，并发送到B端
    void asyncToC(String traceId, String path);
    
    // 同步调用
    // C端收到B端消息，并返回到B端
    String syncToC(String traceId, String path);
    
    // 链式调用：异步Callback调用
    // C端收到A端消息，并返回Callback给A端
    String[] async1ToC(String traceId, String path);
    
    // 链式调用：同步调用
    // C端收到A端消息，并返回给A端
    String[] sync1ToC(String[] result);
}