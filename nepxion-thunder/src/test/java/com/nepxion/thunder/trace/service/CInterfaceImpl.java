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
import java.util.concurrent.atomic.AtomicLong;

public class CInterfaceImpl implements CInterface {
    private BInterface2 bInterface2;
    
    private AtomicLong atomicLong = new AtomicLong(1);
    
    public CInterfaceImpl() {

    }
    
    @Override
    public void asyncToC(String traceId, String path) {
        path += " -> C";
        
        if (Constants.PRINT) {
            System.out.println("异步：C端收到B端消息：" + path + "，并发送到B端");
        }
        
        if (atomicLong.getAndAdd(1) % 10 == 0) {
            throw new CException("C端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        bInterface2.asyncToB(traceId, path);
    }

    @Override
    public String syncToC(String traceId, String path) {
        path += " -> C";
        
        if (Constants.PRINT) {
            System.out.println("同步：C端收到B端消息：" + path + "，并返回到B端");
        }
        
        if (atomicLong.getAndAdd(1) % 10 == 0) {
            throw new CException("C端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return path;
    }
    
    @Override
    public String[] async1ToC(String traceId, String path) {
        path += " -> C";
        
        if (Constants.PRINT) {
            System.out.println("异步链式：C端收到A端消息：" + path + "，并Callback到A端");
        }
        
        if (atomicLong.getAndAdd(1) % 10 == 0) {
            throw new CException("C端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return new String[] {traceId, path};
    }
    
    @Override
    public String[] sync1ToC(String[] result) {
        String traceId = result[0];
        String path = result[1] + " -> C";
        
        if (Constants.PRINT) {
            System.out.println("同步链式：C端收到A端消息：" + path + "，并返回到A端");
        }
        
        if (atomicLong.getAndAdd(1) % 10 == 0) {
            throw new CException("C端出错了");
        }
        
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return new String[] {traceId, path};
    }
    
    public BInterface2 getBInterface2() {
        return bInterface2;
    }

    public void setBInterface2(BInterface2 bInterface2) {
        this.bInterface2 = bInterface2;
    }
}