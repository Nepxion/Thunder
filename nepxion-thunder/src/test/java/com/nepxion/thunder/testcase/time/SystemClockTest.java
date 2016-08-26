package com.nepxion.thunder.testcase.time;

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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.time.SystemClock;

public class SystemClockTest {
    private static final Logger LOG = LoggerFactory.getLogger(SystemClockTest.class);
    
    @Test
    public void test() throws Exception {
        long precision = 10L;
        SystemClock clock = new SystemClock(precision);

        TimeUnit.MILLISECONDS.sleep(precision * 2);

        long nowFromSystem = System.currentTimeMillis();
        long nowFromClock = clock.now();
        
        LOG.info("{}", nowFromClock - nowFromSystem);
    }
}