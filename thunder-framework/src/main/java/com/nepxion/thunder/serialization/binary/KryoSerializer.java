package com.nepxion.thunder.serialization.binary;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.lang3.ArrayUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.nepxion.thunder.serialization.SerializerException;

public class KryoSerializer {
    public static <T> byte[] serialize(T object) {
        return serialize(object, KryoConstants.BUFFER_SIZE, KryoConstants.MAX_BUFFER_SIZE);
    }

    public static <T> byte[] serialize(T object, int bufferSize, int maxBufferSize) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        KryoPool pool = KryoSerializerFactory.getDefaultPool();

        Kryo kryo = null;
        Output output = null;
        byte[] bytes = null;
        try {
            kryo = pool.borrow();
            output = new Output(bufferSize, maxBufferSize);
            kryo.writeClassAndObject(output, object);
            bytes = output.toBytes();
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            try {
                if (kryo != null) {
                    pool.release(kryo);
                }
            } catch (Exception e) {

            }

            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {

            } finally {
                output = null;
            }
        }

        return bytes;
    }

    public static <T> byte[] serialize(Kryo kryo, T object) {
        return serialize(kryo, object, KryoConstants.BUFFER_SIZE, KryoConstants.MAX_BUFFER_SIZE);
    }

    public static <T> byte[] serialize(Kryo kryo, T object, int bufferSize, int maxBufferSize) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        Output output = null;
        byte[] bytes = null;
        try {
            output = new Output(bufferSize, maxBufferSize);
            kryo.writeClassAndObject(output, object);
            bytes = output.toBytes();
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {

            } finally {
                output = null;
            }
        }

        return bytes;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            throw new SerializerException("Bytes is null or empty");
        }

        KryoPool pool = KryoSerializerFactory.getDefaultPool();

        Kryo kryo = null;
        Input input = null;
        Object object = null;
        try {
            kryo = pool.borrow();
            input = new Input(bytes);
            object = kryo.readClassAndObject(input);
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            try {
                if (kryo != null) {
                    pool.release(kryo);
                }
            } catch (Exception e) {

            }

            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {

            } finally {
                input = null;
            }
        }

        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(Kryo kryo, byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            throw new SerializerException("Bytes is null or empty");
        }

        Input input = null;
        Object object = null;
        try {
            input = new Input(bytes);
            object = kryo.readClassAndObject(input);
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {

            } finally {
                input = null;
            }
        }

        return (T) object;
    }
}