package com.nepxion.thunder.serialization.compression;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;

import org.xerial.snappy.Snappy;

import com.nepxion.thunder.serialization.compression.quicklz.QuickLz;

public class CompressorExecutor {
    public static byte[] compress(byte[] bytes) {
        return compress(bytes, CompressorFactory.getCompressorType());
    }

    public static byte[] decompress(byte[] bytes) {
        return decompress(bytes, CompressorFactory.getCompressorType());
    }

    public static byte[] compress(byte[] bytes, CompressorType type) {
        if (type == CompressorType.QUICK_LZ_COMPRESSOR) {
            return QuickLz.compress(bytes, 1);
        } else if (type == CompressorType.SNAPPY_COMPRESSOR) {
            try {
                return Snappy.compress(bytes);
            } catch (IOException e) {
                throw new CompressorException(e);
            }
        } else {
            throw new CompressorException("Invalid compressor type : " + type);
        }
    }

    public static byte[] decompress(byte[] bytes, CompressorType type) {
        if (type == CompressorType.QUICK_LZ_COMPRESSOR) {
            return QuickLz.decompress(bytes);
        } else if (type == CompressorType.SNAPPY_COMPRESSOR) {
            try {
                return Snappy.uncompress(bytes);
            } catch (IOException e) {
                throw new CompressorException(e);
            }
        } else {
            throw new CompressorException("Invalid compressor type : " + type);
        }
    }
}