package com.nepxion.thunder.test;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nepxion.thunder.test.service.EchoFactory;
import com.nepxion.thunder.test.service.EchoService;

public class ThunderClientTest {
    
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:netty-client-context-pressure.xml");
        final EchoService echoService = (EchoService) applicationContext.getBean("echoService");

        final AtomicInteger atomicInteger = new AtomicInteger(0);
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    System.out.println(echoService.echo(atomicInteger.getAndAdd(1) + " : " + EchoFactory.bytes_10));
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