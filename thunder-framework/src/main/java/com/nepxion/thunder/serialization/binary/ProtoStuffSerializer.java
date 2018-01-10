package com.nepxion.thunder.serialization.binary;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.concurrent.ConcurrentMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.google.common.collect.Maps;

public class ProtoStuffSerializer {
    private static final Objenesis OBJENESIS = new ObjenesisStd(true);
    private static final ConcurrentMap<Class<?>, Schema<?>> SCHEMA_MAP = Maps.newConcurrentMap();
    private static final ThreadLocal<LinkedBuffer> BUFFERS = new ThreadLocal<LinkedBuffer>() {
        @Override
        protected LinkedBuffer initialValue() {
            return LinkedBuffer.allocate();
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T object) {
        Schema<T> schema = getSchema((Class<T>) object.getClass());

        LinkedBuffer buffer = BUFFERS.get();
        try {
            return ProtostuffIOUtil.toByteArray(object, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T object = OBJENESIS.newInstance(clazz);

        ProtostuffIOUtil.mergeFrom(bytes, object, schema);

        return object;
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) SCHEMA_MAP.get(clazz);
        if (schema == null) {
            Schema<T> newSchema = RuntimeSchema.createFrom(clazz);
            schema = (Schema<T>) SCHEMA_MAP.putIfAbsent(clazz, newSchema);
            if (schema == null) {
                schema = newSchema;
            }
        }

        return schema;
    }
}