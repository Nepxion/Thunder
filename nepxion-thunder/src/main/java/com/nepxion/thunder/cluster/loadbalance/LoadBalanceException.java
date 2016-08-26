package com.nepxion.thunder.cluster.loadbalance;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class LoadBalanceException extends RuntimeException {
    private static final long serialVersionUID = 851864048447611118L;

    public LoadBalanceException() {
        super();
    }

    public LoadBalanceException(String message) {
        super(message);
    }

    public LoadBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadBalanceException(Throwable cause) {
        super(cause);
    }
}