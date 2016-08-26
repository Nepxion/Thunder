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

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.junit.Test;

public class ThreadPoolTest {
    @Test
    public void test1() throws Exception {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                int index = 1;
                if (index == 1) {
                    throw new IllegalArgumentException("Failed"); // 异常成功抛出
                }
                System.out.println("Pass"); // 不打印
            }
        });
    }
    
    @Test
    public void test2() throws Exception {
        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                int index = 1;
                if (index == 1) {
                    throw new IllegalArgumentException("Failed"); // 异常被吃掉
                }
                System.out.println("Pass1"); // 不打印

                return null;
            }
        });
        System.out.println("Pass2"); // 打印
    }
    
    @Test
    public void test3() throws Exception {
        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                int index = 1;
                if (index == 1) {
                    throw new IllegalArgumentException("Failed"); // 异常抛出
                }
                System.out.println("Pass1"); // 不打印

                return null;
            }
        }).get();
        System.out.println("Pass2"); // 阻塞，直到线程池里面所有的线程都执行完，不打印
    }
    
    @Test
    public void test4() throws Exception {
        Executors.newSingleThreadExecutor().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                int index = 1;
                if (index == 1) {
                    throw new IllegalArgumentException("Failed"); // 异常抛出
                }
                System.out.println("Pass1"); // 不打印

                return null;
            }
        }).get();
        System.out.println("Pass2"); // 阻塞，不打印
    }
}