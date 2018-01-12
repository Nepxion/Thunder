package com.nepxion.thunder.event.eventbus;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.Serializable;

public class Event implements Serializable {
    private static final long serialVersionUID = 7090050666419528496L;

    protected Object source;

    public Event() {
        this(null);
    }

    public Event(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}