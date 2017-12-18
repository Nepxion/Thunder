package com.nepxion.thunder.event.eventbus;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public class Event {
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