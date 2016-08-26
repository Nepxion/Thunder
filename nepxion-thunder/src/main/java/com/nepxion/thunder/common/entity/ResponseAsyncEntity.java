package com.nepxion.thunder.common.entity;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.Serializable;

import com.nepxion.thunder.common.promise.PromiseEntity;
import com.nepxion.thunder.protocol.ProtocolRequest;

public class ResponseAsyncEntity implements Serializable {    
    private static final long serialVersionUID = -253171631908650621L;
    
    private ProtocolRequest request;
    private PromiseEntity<?> promise;

    public ResponseAsyncEntity() {
        
    }

    public ProtocolRequest getRequest() {
        return request;
    }

    public void setRequest(ProtocolRequest request) {
        this.request = request;
    }
    
    public PromiseEntity<?> getPromise() {
        return promise;
    }

    public void setPromise(PromiseEntity<?> promise) {
        this.promise = promise;
    }
}