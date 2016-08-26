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

import java.util.concurrent.TimeUnit;

public class AInterfaceImpl implements AInterface {
    public AInterfaceImpl() {

    }
    
    @Override
    public void asyncToA(String traceId, String path) {
        path += " -> A";
        
        if (Constants.PRINT) {
            System.out.println("异步：A端收到B端消息：" + path);
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}