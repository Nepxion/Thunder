package com.nepxion.thunder.testcase.eventbus;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.junit.Test;

import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;
import com.nepxion.thunder.event.eventbus.EventControllerFactory;
import com.nepxion.thunder.event.eventbus.EventControllerType;

public class MySubscriberTest {

    @Test
    public void testAsync() throws Exception {        
        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        ThreadPoolFactory.initialize(properties);
        
        new MySyncSubscriber1();
        new MySyncSubscriber2();
        new MyAsyncSubscriber1();
        new MyAsyncSubscriber2();
        
        new Runnable() {
            @Override
            public void run() {
                EventControllerFactory.getSingletonController(EventControllerType.SYNC).post(new MyEvent("A"));
            } 
        }.run();
        
        new Runnable() {
            @Override
            public void run() {
                EventControllerFactory.getSingletonController(EventControllerType.ASYNC).post(new MyEvent("B"));
            } 
        }.run();

        System.in.read();
    }
}