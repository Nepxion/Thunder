package com.nepxion.thunder.serialization.binary;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.nustaq.serialization.FSTConfiguration;

import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.serialization.SerializerException;

public class FSTSerializerFactory {
    // 官方文档说，FSTConfiguration太重了，需要通过ThreadLocal或者静态对象去维护它
    // 利用线程池的做法，先屏蔽掉
    private static FSTConfiguration fst;
    private static ObjectPool<FSTConfiguration> pool;
    private static boolean usePool = false;
    
    public static void initialize() {
        if (usePool) {
            pool = createPool();
        } else {
            fst = createFST();
        }
    }

    public static FSTConfiguration createFST() {
        FSTConfiguration fst = FSTConfiguration.createDefaultConfiguration();

        return fst;
    }

    public static ObjectPool<FSTConfiguration> createPool() {
        GenericObjectPoolConfig config = ObjectPoolFactory.createFSTObjectPoolConfig();

        return new GenericObjectPool<FSTConfiguration>(
                new PooledObjectFactory<FSTConfiguration>() {
                    public PooledObject<FSTConfiguration> makeObject() throws Exception {
                        return new DefaultPooledObject<FSTConfiguration>(createFST());
                    }

                    public void destroyObject(PooledObject<FSTConfiguration> p) throws Exception {

                    }

                    public boolean validateObject(PooledObject<FSTConfiguration> p) {
                        return false;
                    }

                    public void activateObject(PooledObject<FSTConfiguration> p) throws Exception {

                    }

                    public void passivateObject(PooledObject<FSTConfiguration> p) throws Exception {

                    }
                }, config);
    }

    public static FSTConfiguration getFST(ObjectPool<FSTConfiguration> pool) {
        FSTConfiguration fst = null;
        try {
            fst = pool.borrowObject();
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            try {
                if (fst != null) {
                    pool.returnObject(fst);
                }
            } catch (Exception e) {

            }
        }

        return fst;
    }

    public static ObjectPool<FSTConfiguration> getDefaultPool() {
        return pool;
    }

    public static FSTConfiguration getDefaultFST() {
        if (usePool) {
            return getFST(pool);
        } else {
            return fst;
        }
    }
}