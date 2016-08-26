package com.nepxion.thunder.serialization.binary;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.lang3.ArrayUtils;
import org.nustaq.serialization.FSTConfiguration;

import com.nepxion.thunder.serialization.SerializerException;

public class FSTSerializer {
    public static <T> byte[] serialize(T object) {
        FSTConfiguration fst = FSTSerializerFactory.getDefaultFST();

        return serialize(fst, object);
    }
    
    public static <T> byte[] serialize(FSTConfiguration fst, T object) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        return fst.asByteArray(object);
    }

    public static <T> T deserialize(byte[] bytes) {
        FSTConfiguration fst = FSTSerializerFactory.getDefaultFST();

        return deserialize(fst, bytes);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(FSTConfiguration fst, byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            throw new SerializerException("Bytes is null or empty");
        }

        return (T) fst.asObject(bytes);
    }
}