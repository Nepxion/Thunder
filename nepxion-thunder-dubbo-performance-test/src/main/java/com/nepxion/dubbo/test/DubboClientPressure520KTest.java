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

import com.nepxion.dubbo.test.service.EchoFactory;

public class DubboClientPressure520KTest extends DubboClientPressureTest {

    @Override
    protected String getText() {
        return EchoFactory.bytes_520K;
    }

    public static void main(String[] args) {
        new DubboClientPressure520KTest().runTest(null);
    }
}