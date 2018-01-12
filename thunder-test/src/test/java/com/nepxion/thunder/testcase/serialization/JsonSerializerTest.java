package com.nepxion.thunder.testcase.serialization;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.junit.Test;
import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.nepxion.thunder.common.object.ObjectPoolFactory;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.serialization.json.AliSerializer;
import com.nepxion.thunder.serialization.json.FSTJsonSerializer;
import com.nepxion.thunder.serialization.json.FSTJsonSerializerFactory;
import com.nepxion.thunder.serialization.json.JacksonSerializer;
import com.nepxion.thunder.test.core.CoreFactory;
import com.nepxion.thunder.test.core.User;

public class JsonSerializerTest {
    private static final Logger LOG = LoggerFactory.getLogger(JsonSerializerTest.class);

    @Test
    public void testJacksonFunction() throws Exception {
        User user1 = CoreFactory.createUser1();

        String json = JacksonSerializer.toJson(user1);
        LOG.info(json);

        User user2 = JacksonSerializer.fromJson(json, User.class);
        LOG.info(user2.toString());
    }

    @Test
    public void testAliFunction() throws Exception {
        User user1 = CoreFactory.createUser1();

        String json = AliSerializer.toJson(user1);
        LOG.info(json);

        User user2 = AliSerializer.fromJson(json, User.class);
        LOG.info(user2.toString());

        User user3 = AliSerializer.fromJson(json);
        LOG.info(user3.toString());

        JSONObject jsonObject = AliSerializer.parseJson(json);
        LOG.info(jsonObject.getString("name"));
    }

    @Test
    public void testFSTFunction() throws Exception {
        User user1 = CoreFactory.createUser1();

        FSTConfiguration fst = FSTJsonSerializerFactory.createFST();

        String json = FSTJsonSerializer.toJson(fst, user1);
        LOG.info(json);

        User user2 = FSTJsonSerializer.fromJson(fst, json, User.class);
        LOG.info(user2.toString());
    }

    @Test
    public void testPerformance() throws Exception {
        User user1 = CoreFactory.createUser1();

        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        ObjectPoolFactory.initialize(properties);
        FSTJsonSerializerFactory.initialize();

        long value1 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            String json = JacksonSerializer.toJson(user1);

            JacksonSerializer.fromJson(json, User.class);
        }
        LOG.info("JacksonSerializer time spent : {}", System.currentTimeMillis() - value1);

        long value2 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            String json = AliSerializer.toJson(user1);

            AliSerializer.fromJson(json, User.class);
        }
        LOG.info("AliSerializer time spent : {}", System.currentTimeMillis() - value2);

        FSTConfiguration fst = FSTJsonSerializerFactory.createFST();

        long value3 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            String json = FSTJsonSerializer.toJson(fst, user1);

            FSTJsonSerializer.fromJson(fst, json, User.class);
        }
        LOG.info("FSTJsonSerializer time spent : {}", System.currentTimeMillis() - value3);

        long value4 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            String json = FSTJsonSerializer.toJson(user1);

            FSTJsonSerializer.fromJson(json, User.class);
        }
        LOG.info("FSTJsonSerializer (pool) time spent : {}", System.currentTimeMillis() - value4);
    }
}