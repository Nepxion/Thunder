package com.nepxion.thunder.common.promise;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromiseEntity<T> extends DeferredObject<T, Exception, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(PromiseEntity.class);
    
    @Override
    public Deferred<T, Exception, Void> reject(Exception exception) {
        LOG.error("Promise chain is terminated", exception);
        
        return super.reject(exception);
    }
}