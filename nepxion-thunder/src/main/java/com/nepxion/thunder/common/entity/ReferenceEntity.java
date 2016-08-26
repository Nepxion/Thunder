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
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ReferenceEntity implements Serializable {
    private static final long serialVersionUID = -862261059257935018L;
    
    private String interfaze;
    private String server;
    private Map<MethodKey, MethodEntity> methodMap;
    
    public String getInterface() {
        return interfaze;
    }
    
    public void setInterface(String interfaze) {
        this.interfaze = interfaze;
    }
    
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
    
    public MethodEntity getMethodEntity(MethodKey methodKey) {
        MethodEntity methodEntity = methodMap.get(methodKey);
        
        return methodEntity;
    }
    
    public boolean hasFeedback() {
        for (Map.Entry<MethodKey, MethodEntity> entry : methodMap.entrySet()) {
            MethodEntity methodEntity = entry.getValue();
            if (methodEntity.isAsync()) {
                if (methodEntity.isCallback()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        
        return false;
    }

    public Map<MethodKey, MethodEntity> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<MethodKey, MethodEntity> methodMap) {
        this.methodMap = methodMap;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        if (interfaze != null) {
            result = 37 * result + interfaze.hashCode();
        }
        
        if (server != null) {
            result = 37 * result + server.hashCode();
        }
        
        if (methodMap != null) {
            result = 37 * result + methodMap.hashCode();
        }
        
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReferenceEntity)) {
            return false;
        }
        
        ReferenceEntity referenceEntity = (ReferenceEntity) object;
        if (StringUtils.equals(this.interfaze, referenceEntity.interfaze)
                && StringUtils.equals(this.server, referenceEntity.server)
                && (this.methodMap == null ? referenceEntity.methodMap == null : this.methodMap.equals(referenceEntity.methodMap))) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("interfaze=");
        builder.append(interfaze);
        builder.append(", server=");
        builder.append(server);
        builder.append(", methodMap=");
        builder.append(methodMap);
        
        return builder.toString();
    }
}