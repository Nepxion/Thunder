package com.nepxion.thunder.serialization.compression;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.property.ThunderProperties;

public class CompressorFactory {
    private static final Logger LOG = LoggerFactory.getLogger(CompressorFactory.class);

    private static CompressorType compressorType = CompressorType.QUICK_LZ_COMPRESSOR;
    private static boolean compress;

    public static void initialize(ThunderProperties properties) {
        String compressor = properties.getString(ThunderConstant.COMPRESSOR_ATTRIBUTE_NAME);
        try {
            compressorType = CompressorType.fromString(compressor);
        } catch (Exception e) {
            LOG.warn("Invalid compressor={}, use default={}", compressor, compressorType);
        }
        LOG.info("Compressor is {}", compressorType);

        compress = properties.getBoolean(ThunderConstant.COMPRESS_ATTRIBUTE_NAME);
        LOG.info("Compress is {}", compress);
    }

    public static CompressorType getCompressorType() {
        return compressorType;
    }

    public static void setCompressorType(CompressorType compressorType) {
        CompressorFactory.compressorType = compressorType;
    }

    public static boolean isCompress() {
        return compress;
    }

    public static void setCompress(boolean compress) {
        CompressorFactory.compress = compress;
    }
}