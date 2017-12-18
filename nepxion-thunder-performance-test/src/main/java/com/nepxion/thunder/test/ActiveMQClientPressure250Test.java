package com.nepxion.thunder.test;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.test.service.EchoFactory;

public class ActiveMQClientPressure250Test extends ActiveMQClientPressureTest {

    @Override
    protected String getText() {
        return EchoFactory.bytes_250;
    }

    public static void main(String[] args) {
        new ActiveMQClientPressure250Test().runTest(null);
    }
}