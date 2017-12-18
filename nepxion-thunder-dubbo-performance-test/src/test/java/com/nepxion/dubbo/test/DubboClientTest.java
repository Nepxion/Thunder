package com.nepxion.dubbo.test;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.rpc.RpcContext;
import com.nepxion.dubbo.test.service.EchoFactory;
import com.nepxion.dubbo.test.service.EchoService;

public class DubboClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(DubboClientTest.class);
    
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:dubbo-client-context-pressure.xml");
        final EchoService echoService = (EchoService) applicationContext.getBean("echoService");

        final AtomicInteger atomicInteger = new AtomicInteger(0);
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    String value = echoService.echo(atomicInteger.getAndAdd(1) + " : " + EchoFactory.bytes_10);
                    LOG.info("value1={}", value);
                    
                    // 异步Future阻塞式调用
                    Future<String> future = RpcContext.getContext().getFuture();
                    LOG.info("future={}", future);
                    if (future != null) {
                        value = future.get();
                        LOG.info("value2={}", value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 100);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}