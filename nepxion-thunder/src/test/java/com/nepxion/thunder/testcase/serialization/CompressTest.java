package com.nepxion.thunder.testcase.serialization;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.HashMap;

import org.junit.Test;

import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.serialization.SerializerExecutor;
import com.nepxion.thunder.serialization.SerializerFactory;
import com.nepxion.thunder.serialization.SerializerType;
import com.nepxion.thunder.serialization.binary.FSTSerializerFactory;
import com.nepxion.thunder.serialization.binary.KryoSerializerFactory;
import com.nepxion.thunder.serialization.compression.CompressorType;

public class CompressTest {
    @Test
    public void test() throws Exception {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < 10000; i++) {
            map.put(i, "abcdefghijklmnopqrstuvwxyz");
        }

        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        SerializerFactory.initialize(properties);
        
        FSTSerializerFactory.initialize();
        SerializerExecutor.serialize(map, SerializerType.FST_BINARY, CompressorType.QUICK_LZ_COMPRESSOR, true, true);
        
        KryoSerializerFactory.initialize();
        SerializerExecutor.serialize(map, SerializerType.KRYO_BINARY, CompressorType.QUICK_LZ_COMPRESSOR, true, true);
        
        SerializerExecutor.serialize(map, SerializerType.JDK_BINARY, CompressorType.QUICK_LZ_COMPRESSOR, true, true);
    }
}