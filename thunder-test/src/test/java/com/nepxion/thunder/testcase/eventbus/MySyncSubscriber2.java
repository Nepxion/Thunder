package com.nepxion.thunder.testcase.eventbus;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.nepxion.thunder.event.eventbus.EventControllerFactory;
import com.nepxion.thunder.testcase.http.HttpTest;

public class MySyncSubscriber2 {
    private static final Logger LOG = LoggerFactory.getLogger(HttpTest.class);

    public MySyncSubscriber2() {
        EventControllerFactory.getSyncController().register(this);
    }

    @Subscribe
    public void listen(MyEvent event) {
        LOG.info("Listen:{}", event.getSource());
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}