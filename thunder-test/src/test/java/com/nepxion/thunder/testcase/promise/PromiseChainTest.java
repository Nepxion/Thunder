package com.nepxion.thunder.testcase.promise;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.promise.PromiseContext;
import com.nepxion.thunder.common.promise.PromiseDone;
import com.nepxion.thunder.common.promise.PromiseEntity;
import com.nepxion.thunder.common.promise.PromiseExecutor;
import com.nepxion.thunder.common.promise.PromiseFail;
import com.nepxion.thunder.common.promise.PromisePipe;
import com.nepxion.thunder.common.util.RandomUtil;

public class PromiseChainTest {
    private static final Logger LOG = LoggerFactory.getLogger(PromiseChainTest.class);
    private static final Map<String, PromiseEntity<?>> PROMISE_MAP = Maps.newConcurrentMap();
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    // 这样的设定，确保调用不会岔掉，调用结果必定是：
    // 回调完成 [0] : String=1970-01-01 08:01:00.000
    // 回调完成 [1] : String=1970-01-02 08:02:00.000
    // 回调完成 [2] : String=1970-01-03 08:03:00.000
    // 回调完成 [3] : String=1970-01-04 08:04:00.000
    // 回调完成 [4] : String=1970-01-05 08:05:00.000
    private static final Long[] ORIGIN_TIMES = new Long[] { 0L, 60000L * 60 * 24, 60000L * 60 * 24 * 2, 60000L * 60 * 24 * 3, 60000L * 60 * 24 * 4 };
    private static final Long[] ADDED_TIMES = new Long[] { 60000L, 60000L * 2, 60000L * 3, 60000L * 4, 60000L * 5 };

    @Test
    public void test() throws Exception {
        System.out.println(new Date(0L));
        for (int i = 0; i < 5; i++) {
            final int index = i;
            final long originTime = ORIGIN_TIMES[index];
            final long addedTime = ADDED_TIMES[index];
            EXECUTOR.execute(new Runnable() {
                public void run() {
                    PromiseExecutor promiseExecutor = new PromiseExecutor();
                    promiseExecutor.then(new PromisePipe<Void, Long>() {
                        @Override
                        public void onResult(Void result) {
                            LOG.info("调用开始 [{}]", index);
                            // throw new IllegalArgumentException("开始就出现异常");

                            step0(originTime);
                        }
                    }).then(new PromisePipe<Long, Date>() {
                        @Override
                        public void onResult(Long result) {
                            LOG.info("调用第一步 [{}] : Long={}", index, result);
                            // throw new IllegalArgumentException("调用第一步异常");
                            step1(result + addedTime);
                        }
                    }).then(new PromisePipe<Date, String>() {
                        @Override
                        public void onResult(Date result) {
                            LOG.info("调用第二步 [{}] : Date={}", index, result);
                            // throw new IllegalArgumentException("调用第二步异常");
                            step2(result);
                        }
                    }).done(new PromiseDone<String>() {
                        @Override
                        public void onDone(String result) {
                            LOG.info("回调完成 [{}] : String={}", index, result);
                            // throw new IllegalArgumentException("开始就出现异常");
                        }
                    }).fail(new PromiseFail() {
                        @Override
                        public void onFail(Exception exception) {
                            LOG.error("异常", exception);
                        }
                    });
                    promiseExecutor.execute();

                    LOG.info("Promise looping {} end", index);
                }
            });
        }

        LOG.info("Promise chain invoking starts");

        System.in.read();
    }

    private String store() {
        PromiseEntity<Object> promise = new PromiseEntity<Object>();

        PromiseContext.setPromise(promise);

        String messageId = RandomUtil.uuidRandom();
        PROMISE_MAP.put(messageId, promise);

        return messageId;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void execute(String messageId, Object result, Exception exception) {
        PromiseEntity promise = PROMISE_MAP.get(messageId);
        if (exception == null) {
            promise.resolve(result);
        } else {
            promise.reject(exception);
        }
        PROMISE_MAP.remove(messageId);
    }

    private void step0(final Long result) {
        final String messageId = store();

        new Thread(new Runnable() {
            @Override
            public void run() {
                execute(messageId, result, null);
                // execute(messageId, new Date(result), new IllegalArgumentException("Step0 Exception"));
            }
        }).run();
    }

    private void step1(final Long result) {
        final String messageId = store();

        new Thread(new Runnable() {
            @Override
            public void run() {
                execute(messageId, new Date(result), null);
                // execute(messageId, new Date(result), new IllegalArgumentException("Step1 Exception"));
            }
        }).run();
    }

    private void step2(final Date result) {
        final String messageId = store();

        new Thread(new Runnable() {
            @Override
            public void run() {
                execute(messageId, new SimpleDateFormat(ThunderConstant.DATE_FORMAT).format(result), null);
                // execute(messageId, new SimpleDateFormat(ThunderConstants.DATE_FORMAT).format(result), new IllegalArgumentException("Step2 Exception"));
            }
        }).run();
    }
}