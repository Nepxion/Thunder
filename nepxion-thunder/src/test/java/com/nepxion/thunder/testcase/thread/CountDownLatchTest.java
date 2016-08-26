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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class CountDownLatchTest {
    private byte[] lock = new byte[0];
    private CountDownLatch latch;

    @Test
    public void test() throws Exception {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    print(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    print(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(5000);
                    lock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        print(0);
        
        System.in.read();
    }

    private void print(int index) throws Exception {
        for (int i = 0; i < 100; i++) {
            await();
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("线程" + index + " ：正在工作 " + i);
        }
    }

    private void lock() throws Exception {
        synchronized (lock) {
            try {
                System.out.println("强势线程开始...");
                latch = new CountDownLatch(1);

                // 强势线程开始做事
                TimeUnit.MILLISECONDS.sleep(20000);

                latch.countDown();
                
                System.out.println("强势线程结束...");
            } finally {
                latch = null;
            }
        }
    }

    private void await() throws Exception {
        if (latch != null) {
            System.out.println("其他线程等待开始...");
            latch.await();
            System.out.println("其他线程等待结束...");
        }
    }
}