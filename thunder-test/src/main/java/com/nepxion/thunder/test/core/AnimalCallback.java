package com.nepxion.thunder.test.core;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.callback.ThunderCallback;

public class AnimalCallback extends ThunderCallback<Animal> {
    private static final Logger LOG = LoggerFactory.getLogger(AnimalCallback.class);

    @Override
    public void onResult(Animal result) {
        LOG.info("客户端-异步回调结果：返回值={}", result);
    }

    @Override
    public void onError(Exception exception) {
        LOG.info("客户端-异步回调结果：异常=", exception);
    }
}