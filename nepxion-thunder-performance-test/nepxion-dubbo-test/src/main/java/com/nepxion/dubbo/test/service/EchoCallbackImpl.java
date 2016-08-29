package com.nepxion.dubbo.test.service;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoCallbackImpl implements EchoCallback {
    private static final Logger LOG = LoggerFactory.getLogger(EchoCallbackImpl.class);

    @Override
    public void onResult(String result) {
        LOG.info("客户端-异步回调结果：返回值={}", result);
    }

    @Override
    public void onError(Exception exception) {
        LOG.info("客户端-异步回调结果：异常=", exception);
    }
}