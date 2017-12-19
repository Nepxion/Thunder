package com.nepxion.thunder.test.trace;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class BInterface2Impl implements BInterface2 {
    private AInterface aInterface;

    private AtomicLong atomicLong = new AtomicLong(1);

    public BInterface2Impl() {

    }

    @Override
    public void asyncToB(String traceId, String path) {
        path += " -> B";

        if (TraceConstants.PRINT) {
            System.out.println("异步：B端收到C端消息：" + path + "，并发送到A端");
        }

        if (atomicLong.getAndAdd(1) % 7 == 0) {
            throw new BException("B端出错了");
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        aInterface.asyncToA(traceId, path);
    }

    public AInterface getAInterface() {
        return aInterface;
    }

    public void setAInterface(AInterface aInterface) {
        this.aInterface = aInterface;
    }
}