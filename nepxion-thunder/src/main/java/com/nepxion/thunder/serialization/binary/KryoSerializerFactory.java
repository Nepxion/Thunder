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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

public class KryoSerializerFactory {
    private static KryoPool pool;

    public static void initialize() {
        pool = createPool();
    }

    public static void initialize(int maxDepth) {
        pool = createPool(maxDepth);
    }

    public static KryoPool createPool() {
        return createPool(KryoConstants.MAX_DEPTH);
    }

    public static KryoPool createPool(final int maxDepth) {
        return new KryoPool.Builder(new KryoFactory() {
            @Override
            public Kryo create() {
                return createKryo(maxDepth);
            }
        }).softReferences().build();
    }

    public static Kryo createKryo() {
        return createKryo(KryoConstants.MAX_DEPTH);
    }

    public static Kryo createKryo(int maxDepth) {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.setAsmEnabled(true);
        kryo.setMaxDepth(maxDepth);
        kryo.setAutoReset(true);

        return kryo;
    }

    public static KryoPool getDefaultPool() {
        return pool;
    }
}