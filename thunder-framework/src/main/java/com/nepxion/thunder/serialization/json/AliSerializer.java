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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.serialization.SerializerException;

public class AliSerializer {
    public static <T> String toJson(T object) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }

        return JSON.toJSONStringWithDateFormat(object, ThunderConstant.DATE_FORMAT, SerializerFeature.WriteClassName);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        return JSON.parseObject(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        return (T) JSON.parse(json);
    }

    public static JSONObject parseJson(String json) {
        if (StringUtils.isEmpty(json)) {
            throw new SerializerException("Json is null or empty");
        }

        return JSON.parseObject(json);
    }
}