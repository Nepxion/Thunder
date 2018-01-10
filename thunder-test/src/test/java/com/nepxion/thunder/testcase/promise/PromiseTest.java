package com.nepxion.thunder.testcase.promise;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.jdeferred.AlwaysCallback;
import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.DonePipe;
import org.jdeferred.FailCallback;
import org.jdeferred.ProgressCallback;
import org.jdeferred.Promise;
import org.jdeferred.Promise.State;
import org.jdeferred.impl.DefaultDeferredManager;
import org.jdeferred.impl.DeferredObject;
import org.jdeferred.multiple.MultipleResults;
import org.jdeferred.multiple.OneReject;
import org.jdeferred.multiple.OneResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromiseTest {
    private static final Logger LOG = LoggerFactory.getLogger(PromiseTest.class);

    @Test
    public void test1() throws Exception {
        Deferred<String, String, Integer> deferred = new DeferredObject<String, String, Integer>();
        Promise<String, String, Integer> promise = deferred.promise();
        promise.done(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                LOG.info("Result : {}", result);
            }
        }).fail(new FailCallback<String>() {
            @Override
            public void onFail(String rejection) {
                System.out.println("Rejection : " + rejection);
            }
        }).progress(new ProgressCallback<Integer>() {
            @Override
            public void onProgress(Integer progress) {
                System.out.println("Progress : " + progress);
            }
        }).always(new AlwaysCallback<String, String>() {
            @Override
            public void onAlways(State state, String result, String rejection) {
                System.out.println("Always : " + result + " " + rejection);
            }
        });

        deferred.reject("oops");
        deferred.resolve("done");
        deferred.notify(99);

        System.in.read();
    }

    @Test
    public void test2() throws Exception {
        Deferred<String, String, Integer> deferred = new DeferredObject<String, String, Integer>();
        Promise<String, String, Integer> promise = deferred.promise();
        promise.then(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                LOG.info("Result : {}", result);
            }
        }).then(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                LOG.info("Result : {}", result);
            }
        }).then(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                LOG.info("Result : {}", result);
            }
        });

        deferred.resolve("a");

        System.in.read();
    }

    @Test
    public void test3() throws Exception {
        Deferred<String, String, Integer> deferred = new DeferredObject<String, String, Integer>();
        Promise<String, String, Integer> promise = deferred.promise();
        promise.then(new DonePipe<String, String, String, Integer>() {
            @Override
            public Promise<String, String, Integer> pipeDone(String result) {
                LOG.info("Result : {}", result);

                return new DeferredObject<String, String, Integer>().resolve("b");
            }
        }).then(new DonePipe<String, String, String, Integer>() {
            @Override
            public Promise<String, String, Integer> pipeDone(String result) {
                LOG.info("Result : {}", result);

                return new DeferredObject<String, String, Integer>().resolve("c");
            }
        }).then(new DonePipe<String, String, String, Integer>() {
            @Override
            public Promise<String, String, Integer> pipeDone(String result) {
                LOG.info("Result : {}", result);

                return new DeferredObject<String, String, Integer>();
            }
        });

        deferred.resolve("a");

        System.in.read();
    }

    @Test
    public void test4() throws Exception {
        DefaultDeferredManager deferredManager = new DefaultDeferredManager();
        deferredManager.when(new Callable<Integer>() {
            public Integer call() {
                LOG.info("并行调用1-1开始 -----");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                // Integer.parseInt("a");

                LOG.info("并行调用1-1结果 : {}", 100);

                return 100;
            }
        }, new Callable<String>() {
            public String call() {
                LOG.info("并行调用1-2开始 -----");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }

                // Integer.parseInt("b");

                LOG.info("并行调用1-2: {}", "Hello");

                return "Hello";
            }
        }).then(new DonePipe<MultipleResults, List<Object>, OneReject, Integer>() {
            @Override
            public Promise<List<Object>, OneReject, Integer> pipeDone(MultipleResults results) {
                List<Object> objects = new ArrayList<Object>();
                for (Iterator<OneResult> iterator = results.iterator(); iterator.hasNext();) {
                    objects.add(iterator.next().getResult());
                }
                LOG.info("汇总结果: {}", objects);

                return new DeferredObject<List<Object>, OneReject, Integer>().resolve(objects);
            }
        }).done(new DoneCallback<List<Object>>() {
            public void onDone(List<Object> result) {
                LOG.info("Result: {}", result);
            }
            /*}).done(new DoneCallback<MultipleResults>() {
            public void onDone(MultipleResults results) {
                for (Iterator<OneResult> iterator = results.iterator();iterator.hasNext();) {
                    LOG.info("汇总-1 : {}", iterator.next().getResult());
                }
            }*/
        }).fail(new FailCallback<OneReject>() {
            public void onFail(OneReject result) {
                LOG.info("Reject: {}", result.getReject());
            }
        }).waitSafely(4000);

        deferredManager.shutdown();
        while (!deferredManager.isTerminated()) {
            try {
                deferredManager.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
        }
    }
}