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

public class BException extends RuntimeException {
    private static final long serialVersionUID = 341354494033218328L;

    public BException() {
        super();
    }

    public BException(String message) {
        super(message);
    }

    public BException(String message, Throwable cause) {
        super(message, cause);
    }

    public BException(Throwable cause) {
        super(cause);
    }
}