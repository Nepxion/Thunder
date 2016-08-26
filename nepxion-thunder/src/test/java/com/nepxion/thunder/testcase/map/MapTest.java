package com.nepxion.thunder.testcase.map;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.nepxion.thunder.common.entity.MethodKey;

public class MapTest {
    private static final Logger LOG = LoggerFactory.getLogger(MapTest.class);

    @Test
    public void test() throws Exception {
        LinkedHashMap<MethodKey, String> map = new LinkedHashMap<MethodKey, String>();
        
        MethodKey key1 = new MethodKey();
        key1.setMethod("1");
        key1.setParameterTypes("1");
        
        MethodKey key2 = new MethodKey();
        key2.setMethod("2");
        key2.setParameterTypes("1");
        
        map.put(key1, "a");
        key1.setMethod("2");
        // 不Put，不会Rehash
        map.put(key1, "a");
        
        LOG.info(map.containsKey(key2) + "");
    }
    
    @Test
    public void test1() throws Exception {
        Map<Integer, String> map = Maps.newConcurrentMap();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.put(4, "four");
        map.put(5, "five");
        map.put(6, "six");
        map.put(7, "seven");
        map.put(8, "eight");
        map.put(5, "five");
        map.put(9, "nine");
        map.put(10, "ten");

        Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            int key = entry.getKey();
            if (key % 2 == 1) {
                // map.put(key, "eleven"); //ConcurrentModificationException
                // map.remove(key); //ConcurrentModificationException
                iterator.remove(); // OK
            }
        }
        LOG.info(map + "");
    }
}