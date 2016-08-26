package com.nepxion.thunder.testcase.retry;

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

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;

public class RetryTest {
    private static ThunderProperties properties = ThunderPropertiesManager.getProperties();
    private int index = 0;

    @Test
    public void test() throws Exception {
        System.out.println(invoke1(properties.getInteger(ThunderConstants.LOAD_BALANCE_RETRY_TIMES_ATTRIBUTE_NAME)));
        //invoke2(properties.getInteger(ThunderConstants.RETRY_TIMES_ATTRIBUTE_NAME));
    }

    protected String invoke1(int invokeRetryTimes) {
        System.out.println("Touch Server...");
        invokeRetryTimes--;

        if (invokeRetryTimes > -1) {
            System.out.println("Retry times = " + (properties.getInteger(ThunderConstants.LOAD_BALANCE_RETRY_TIMES_ATTRIBUTE_NAME) - invokeRetryTimes));

            return invoke1(invokeRetryTimes);
        }

        return "result";
    }

    protected void invoke2(int invokeRetryTimes) {
        System.out.println("Touch Server...");
        invokeRetryTimes--;

        if (loop() < 7) {
            if (invokeRetryTimes > -1) {
                System.out.println("Retry times = " + (properties.getInteger(ThunderConstants.LOAD_BALANCE_RETRY_TIMES_ATTRIBUTE_NAME) - invokeRetryTimes));

                invoke2(invokeRetryTimes);
            }
        } else {
            System.out.println("result"); 
        }
    }

    private int loop() {
        return index++;
    }
}