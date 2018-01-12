package com.nepxion.thunder.event.protocol;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.google.common.eventbus.Subscribe;
import com.nepxion.thunder.event.eventbus.EventControllerFactory;

public abstract class ProtocolEventInterceptor {
    public ProtocolEventInterceptor() {
        EventControllerFactory.getAsyncController().register(this);
    }

    @Subscribe
    public void listen(ProtocolEvent event) {
        onEvent(event);
    }

    protected abstract void onEvent(ProtocolEvent event);
}