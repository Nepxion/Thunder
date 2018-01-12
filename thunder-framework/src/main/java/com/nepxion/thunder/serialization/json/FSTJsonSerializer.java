package com.nepxion.thunder.serialization.json;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.nustaq.serialization.FSTConfiguration;

import com.nepxion.thunder.serialization.SerializerException;

public class FSTJsonSerializer {
    public static <T> String toJson(T object) {
        FSTConfiguration fst = FSTJsonSerializerFactory.getDefaultFST();

        return toJson(fst, object);
    }

    public static <T> String toJson(FSTConfiguration fst, T object) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        return fst.asJsonString(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        FSTConfiguration fst = FSTJsonSerializerFactory.getDefaultFST();

        return fromJson(fst, json, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(FSTConfiguration fst, String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        return (T) fst.asObject(json.getBytes());
    }
}