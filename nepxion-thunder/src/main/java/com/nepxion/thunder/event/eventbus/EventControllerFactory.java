package com.nepxion.thunder.event.eventbus;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
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
import com.nepxion.thunder.common.thread.ThreadPoolFactory;

public final class EventControllerFactory {
    private static final String SINGLETON = "Singleton";
    private static ConcurrentMap<Object, EventController> syncControllerMap = Maps.newConcurrentMap();
    private static ConcurrentMap<Object, EventController> asyncControllerMap = Maps.newConcurrentMap();
    
    private EventControllerFactory() {

    }

    public static EventController getSingletonController(EventControllerType type) {
        return getController(SINGLETON, type);
    }
    
    public static EventController getController(Object id, EventControllerType type) {
        switch (type) {
            case SYNC :
                return syncControllerMap.putIfAbsent(id, createSyncController());
            case ASYNC :
                return asyncControllerMap.putIfAbsent(id, createAsyncController(ThreadPoolFactory.createThreadPoolDefaultExecutor(null, EventControllerFactory.class.getName())));
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