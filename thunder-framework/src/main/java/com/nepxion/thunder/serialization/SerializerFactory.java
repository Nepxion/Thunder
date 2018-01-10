package com.nepxion.thunder.serialization;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.serialization.binary.FSTSerializerFactory;
import com.nepxion.thunder.serialization.binary.KryoSerializerFactory;
import com.nepxion.thunder.serialization.json.FSTJsonSerializerFactory;

public class SerializerFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SerializerFactory.class);

    private static SerializerType binarySerializerType = SerializerType.FST_BINARY;
    private static SerializerType jsonSerializerType = SerializerType.JACKSON_JSON;
    private static boolean serializerLogPrint;

    public static void initialize(ThunderProperties properties) {
        String binarySerializer = properties.getString(ThunderConstant.BINARY_SERIALIZER_ATTRIBUTE_NAME);
        try {
            binarySerializerType = SerializerType.fromString(binarySerializer);
        } catch (Exception e) {
            LOG.warn("Invalid binary serializer={}, use default={}", binarySerializer, binarySerializerType);
        }
        LOG.info("Binary serializer is {}", binarySerializerType);
        if (binarySerializerType == SerializerType.FST_BINARY) {
            FSTSerializerFactory.initialize();
        } else if (binarySerializerType == SerializerType.KRYO_BINARY) {
            KryoSerializerFactory.initialize();
        }

        String jsonSerializer = properties.getString(ThunderConstant.JSON_SERIALIZER_ATTRIBUTE_NAME);
        try {
            jsonSerializerType = SerializerType.fromString(jsonSerializer);
        } catch (Exception e) {
            LOG.warn("Invalid json serializer={}, use default={}", jsonSerializer, jsonSerializerType);
        }
        LOG.info("Json serializer is {}", jsonSerializerType);
        if (jsonSerializerType == SerializerType.FST_JSON) {
            FSTJsonSerializerFactory.initialize();
        }

        serializerLogPrint = properties.getBoolean(ThunderConstant.SERIALIZER_LOG_PRINT_ATTRIBUTE_NAME);
    }

    public static SerializerType getBinarySerializerType() {
        return binarySerializerType;
    }

    public static void setBinarySerializerType(SerializerType binarySerializerType) {
        SerializerFactory.binarySerializerType = binarySerializerType;
    }

    public static SerializerType getJsonSerializerType() {
        return jsonSerializerType;
    }

    public static void setJsonSerializerType(SerializerType jsonSerializerType) {
        SerializerFactory.jsonSerializerType = jsonSerializerType;
    }

    public static boolean isSerializerLogPrint() {
        return serializerLogPrint;
    }
}