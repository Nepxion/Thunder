package com.nepxion.thunder.test.trace;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
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