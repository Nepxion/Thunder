package com.nepxion.thunder.serialization.compression;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public enum CompressorType {
    QUICK_LZ_COMPRESSOR("quickLzCompressor"),
    SNAPPY_COMPRESSOR("snappyCompressor");

    private String value;

    private CompressorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CompressorType fromString(String value) {
        for (CompressorType type : CompressorType.values()) {
            if (type.getValue().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}