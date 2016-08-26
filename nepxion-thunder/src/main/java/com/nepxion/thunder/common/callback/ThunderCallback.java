package com.nepxion.thunder.common.callback;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public abstract class ThunderCallback<T> {
    public void call(T result, Exception exception) {
        if (exception == null) {
            onResult(result);
        } else {
            onError(exception);
        }
    }

    public abstract void onResult(T result);
    
    public abstract void onError(Exception exception);
}