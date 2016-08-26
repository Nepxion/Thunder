package com.nepxion.thunder.serialization;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

public enum SerializerType {
    FST_BINARY("fstBinarySerializer"),
    KRYO_BINARY("kryoBinarySerializer"),
    JDK_BINARY("jdkBinarySerializer"),   
    JACKSON_JSON("jacksonJsonSerializer"),
    ALI_JSON("aliJsonSerializer"),
    FST_JSON("fstJsonSerializer");

    private String value;

    private SerializerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static SerializerType fromString(String value) {
        for (SerializerType type : SerializerType.values()) {
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