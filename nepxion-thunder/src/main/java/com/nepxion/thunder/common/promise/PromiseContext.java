package com.nepxion.thunder.common.promise;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class PromiseContext {
    private static final ThreadLocal<PromiseEntity<?>> PROMISE = new ThreadLocal<PromiseEntity<?>>();

    public static PromiseEntity<?> currentPromise() {
        PromiseEntity<?> promise = PROMISE.get();
        PROMISE.remove();

        return promise;
    }

    @SuppressWarnings("unchecked")
    public static <T> PromiseEntity<T> currentPromise(Class<T> genericType) {
        return (PromiseEntity<T>) currentPromise();
    }

    public static void setPromise(PromiseEntity<?> promise) {
        PROMISE.set(promise);
    }
}