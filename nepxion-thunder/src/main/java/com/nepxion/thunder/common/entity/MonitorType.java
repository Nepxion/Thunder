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

public enum MonitorType {
    LOG_SERVICE("logService"),
    REDIS_SERVICE("redisService"),
    WEB_SERVICE("webService");

    private String value;

    private MonitorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static MonitorType fromString(String value) {
        for (MonitorType type : MonitorType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}