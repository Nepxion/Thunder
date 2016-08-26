package com.nepxion.thunder.trace.service;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class CException extends RuntimeException {
    private static final long serialVersionUID = -4564821783582928675L;

    public CException() {
        super();
    }

    public CException(String message) {
        super(message);
    }

    public CException(String message, Throwable cause) {
        super(message, cause);
    }

    public CException(Throwable cause) {
        super(cause);
    }
}