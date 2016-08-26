package com.nepxion.thunder.testcase.thread;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

public class CyclicBarrierTest {
    private final CyclicBarrier barrier = new CyclicBarrier(2);

    @Test
    public void test1() throws Exception {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Pass1");
                try {
                    // 这里的parties是一个计数器，例如，初始化时parties里的计数是2，于是拥有该CyclicBarrier对象的线程当parties的计数为2时就唤醒，
                    // 注：这里parties里的计数在运行时当调用CyclicBarrier:await()时，计数就加1，一直加到初始的值
                    // 阻塞主线程
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("Pass2");
            }
        });

        System.out.println("Pass3");
        
        // 在所有参与者都已经在此屏障上调用 await 方法之前将一直等待,或者超出了指定的等待时间
        try {
            barrier.await(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("Pass4");
    }
    
    @Test
    public void test2() throws Exception {        
        // 在所有参与者都已经在此屏障上调用 await 方法之前将一直等待,或者超出了指定的等待时间后结束线程
        try {
            barrier.await(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("Pass1");
    }
}