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

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

import com.google.common.collect.Maps;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.thread.ThreadPoolFactory;

public final class EventControllerFactory {
    private static final String SHARED_CONTROLLER = "SharedController";
    private static final ConcurrentMap<Object, EventController> SYNC_CONTROLLER_MAP = Maps.newConcurrentMap();
    private static final ConcurrentMap<Object, EventController> ASYNC_CONTROLLER_MAP = Maps.newConcurrentMap();

    private EventControllerFactory() {

    }

    public static EventController getAsyncController() {
        return getAsyncController(SHARED_CONTROLLER);
    }

    public static EventController getAsyncController(String identifier) {
        return getController(identifier, true);
    }

    public static EventController getSyncController() {
        return getSyncController(SHARED_CONTROLLER);
    }

    public static EventController getSyncController(String identifier) {
        return getController(identifier, false);
    }

    public static EventController getController(String identifier, boolean async) {
        return getController(identifier, async ? EventType.ASYNC : EventType.SYNC);
    }

    public static EventController getController(String identifier, EventType type) {
        switch (type) {
            case SYNC:
                EventController syncEventController = SYNC_CONTROLLER_MAP.get(identifier);
                if (syncEventController == null) {
                    EventController newEventController = createSyncController(identifier);
                    syncEventController = SYNC_CONTROLLER_MAP.putIfAbsent(identifier, newEventController);
                    if (syncEventController == null) {
                        syncEventController = newEventController;
                    }
                }

                return syncEventController;
            case ASYNC:
                EventController asyncEventController = ASYNC_CONTROLLER_MAP.get(identifier);
                if (asyncEventController == null) {
                    EventController newEventController = createAsyncController(ThreadPoolFactory.createThreadPoolDefaultExecutor(null, ThunderConstants.EVENT_BUS));
                    asyncEventController = ASYNC_CONTROLLER_MAP.putIfAbsent(identifier, newEventController);
                    if (asyncEventController == null) {
                        asyncEventController = newEventController;
                    }
                }

                return asyncEventController;
        }

        return null;
    }

    public static EventController createSyncController() {
        return new EventControllerImpl(new EventBus());
    }

    public static EventController createSyncController(String identifier) {
        return new EventControllerImpl(new EventBus(identifier));
    }

    public static EventController createSyncController(SubscriberExceptionHandler subscriberExceptionHandler) {
        return new EventControllerImpl(new EventBus(subscriberExceptionHandler));
    }

    public static EventController createAsyncController(String identifier, Executor executor) {
        return new EventControllerImpl(new AsyncEventBus(identifier, executor));
    }

    public static EventController createAsyncController(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
        return new EventControllerImpl(new AsyncEventBus(executor, subscriberExceptionHandler));
    }

    public static EventController createAsyncController(Executor executor) {
        return new EventControllerImpl(new AsyncEventBus(executor));
    }
}