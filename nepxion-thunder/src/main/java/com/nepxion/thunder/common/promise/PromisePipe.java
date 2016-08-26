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

import org.jdeferred.DonePipe;
import org.jdeferred.Promise;

import com.nepxion.thunder.common.entity.CallbackType;

// T为入参，即onResult方法的入参，可以来自两种方式：
//   1. 由promiseExecutor.currentPromise().resolve(xxx)方式传入
//   2. 上一个链式调用方法的回调值方式从传入
// R为出参，即onResult方法的实现体里的异步方法的所带来的回调值，它将是下个链式调用onResult方法的入参
public abstract class PromisePipe<T, R> implements DonePipe<T, R, Exception, Void> {
    
    @SuppressWarnings("unchecked")
    @Override
    public Promise<R, Exception, Void> pipeDone(T result) {        
        try {
            onResult(result);
            
            Promise<R, Exception, Void> promise = (Promise<R, Exception, Void>) PromiseContext.currentPromise();
            if (promise == null) {
                throw new IllegalArgumentException("Get next promise failed, do you set callback=\"" + CallbackType.PROMISE + "\" correctly?");
            }
            
            return promise;
        } catch (Exception e) {
            return reject(e);
        }
    }

    public Promise<R, Exception, Void> resolve(R result) {
        // 无异常，继续执行下一个调用，通过resolve方法唤醒
        PromiseEntity<R> promise = new PromiseEntity<R>();

        return promise.resolve(result);
    }

    public Promise<R, Exception, Void> reject(Exception exception) {
        // 有异常，终止下一个调用，通过reject方法终止
        PromiseEntity<R> promise = new PromiseEntity<R>();

        return promise.reject(exception);
    }
    
    public abstract void onResult(T result);
}