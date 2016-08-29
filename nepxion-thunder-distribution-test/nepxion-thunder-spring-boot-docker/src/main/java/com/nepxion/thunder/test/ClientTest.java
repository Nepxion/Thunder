package com.nepxion.thunder.test;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.test.service.AnimalService;
import com.nepxion.thunder.test.service.User;
import com.nepxion.thunder.test.service.UserService;

public class ClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(ClientTest.class);

    public static void test1(UserService userService, AnimalService animalService) throws Exception {
        test2(userService, animalService);
        // test3(userService);
    }
    
    public static void test2(final UserService userService, final AnimalService animalService) throws Exception {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    User user1 = userService.getUser("Zhangsan", 30);
                    if (user1 != null) {
                        LOG.info("客户端-同步调用结果：返回值=User1 name={}, address={}, phone={}", user1.getName(), user1.getAddress(), user1.getPhone());
                    }
                } catch (Exception e) {
                    LOG.error("::::userService.getUser():::", e);
                }

                try {
                    User user2 = userService.getUser("Lisi");
                    if (user2 != null) {
                        LOG.info("客户端-同步调用结果：返回值=User2 name={}, address={}, phone={}", user2.getName(), user2.getAddress(), user2.getPhone());
                    }
                } catch (Exception e) {
                    LOG.error("::::userService.getUser():::", e);
                }

                try {
                    userService.getUsers();

                    for (int i = 0; i < 5; i++) {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        LOG.info("客户端-异步回调主线程打印...");
                    }
                } catch (Exception e) {
                    LOG.error("::::userService.getUsers():::", e);
                }

                try {
                    LOG.info("客户端-发起异步调用，无回调接口");
                    userService.refreshUsers();
                } catch (Exception e) {
                    LOG.error("::::userService.refreshUsers():::", e);
                }

                try {
                    String name = "Tom";
                    LOG.info("客户端-发起异步广播：name={}", name);
                    animalService.getAnimal(name);
                } catch (Exception e) {
                    LOG.error("::::animalService.getAnimal():::", e);
                }
            }
        }, 0, 15000);

        System.in.read();
    }
    
    private static int index = 0;
    
    public static void test3(final UserService userService) throws Exception {
        LOG.info("下面程序测试是否丢消息");
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    User user = userService.getUser("Zhangsan", 30);
                    if (user != null) {
                        LOG.info("执行 {} 客户端-同步调用结果：返回值=User1 name={}, address={}, phone={}", index, user.getName(), user.getAddress(), user.getPhone());
                    }
                    userService.getUsers();
                } catch (Exception e) {
                    LOG.error("::::userService.getUser():::", e);
                }
                
                index++;
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);

        System.in.read();
    }

    public static void test4(final UserService userService) throws Exception {
        int number = 50;
        final CyclicBarrier barrier = new CyclicBarrier(number + 1);
        ExecutorService executor = Executors.newFixedThreadPool(number + 1);
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < number; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        try {
                            userService.getUsers();
                            // User user1 = userService.getUser("Zhangsan", 30);
                            // LOG.info("客户端-同步调用结果：返回值=User1 name={}, address={}, phone={}", user1.getName(), user1.getAddress(), user1.getPhone());
                        } catch (Exception e) {
                            LOG.error("::::userService.getUsers():::", e);
                        }
                    }
                    LOG.info("客户端-计数器:" + barrier.getNumberWaiting());
                    
                    try {
                        barrier.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        long t2 = System.currentTimeMillis();
        LOG.info("耗费时间：{}", t2-t1);
        
        System.in.read();
    }
}