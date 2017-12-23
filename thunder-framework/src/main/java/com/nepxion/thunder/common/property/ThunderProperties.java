package com.nepxion.thunder.common.property;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.nepxion.thunder.common.util.MathsUtil;

public class ThunderProperties implements Serializable {
    private static final long serialVersionUID = 1722927234615067236L;

    public static final String DOT = ".";
    public static final String LINE = "-";

    private final Map<String, String> map = Maps.newLinkedHashMap();

    private String content;

    public ThunderProperties() {

    }

    // 配置文件含中文，stringEncoding必须为GBK，readerEncoding必须为UTF-8，文本文件编码必须为ANSI
    public ThunderProperties(String path, String stringEncoding, String readerEncoding) throws IOException {
        this(new ThunderContent(path, stringEncoding).getContent(), readerEncoding);
    }

    // 配置文件含中文，stringEncoding必须为UTF-8，readerEncoding必须为UTF-8
    public ThunderProperties(byte[] bytes, String stringEncoding, String readerEncoding) throws IOException {
        this(new String(bytes, stringEncoding), readerEncoding);
    }

    // 配置文件含中文，encoding必须为UTF-8
    public ThunderProperties(String content, String encoding) throws IOException {
        this.content = content;

        InputStream inputStream = null;
        Reader reader = null;
        try {
            inputStream = IOUtils.toInputStream(content, encoding);
            reader = new InputStreamReader(inputStream, encoding);

            Properties properties = new Properties();
            properties.load(reader);
            for (Iterator<Object> iterator = properties.keySet().iterator(); iterator.hasNext();) {
                String key = iterator.next().toString();
                String value = properties.getProperty(key);
                put(key, value);
            }
        } finally {
            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }

            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    public String getContent() {
        return content;
    }

    public void put(String key, String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value is null for key [" + key + "]");
        }

        Long result = MathsUtil.calculate(value.toString());
        if (result != null) {
            map.put(key, String.valueOf(result));
        } else {
            map.put(key, value);
        }
    }

    public Map<String, String> getMap() {
        return map;
    }

    public String getString(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Key [" + key + "] isn't found in properties");
        }

        return map.get(key);
    }

    public String getString(String key, String defaultValue) {
        String value = getString(key);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key) {
        String value = getString(key);
        if (value != null) {
            try {
                return Boolean.parseBoolean(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to boolean", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return Boolean.parseBoolean(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to boolean", e);
            }
        } else {
            return defaultValue;
        }
    }

    public int getInteger(String key) {
        String value = getString(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to int", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public int getInteger(String key, int defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to int", e);
            }
        } else {
            return defaultValue;
        }
    }

    public long getLong(String key) {
        String value = getString(key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to long", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public long getLong(String key, long defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to long", e);
            }
        } else {
            return defaultValue;
        }
    }

    public short getShort(String key, short defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return Short.parseShort(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to short", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public Short getShort(String key, Short defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return Short.parseShort(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to short", e);
            }
        } else {
            return defaultValue;
        }
    }

    public byte getByte(String key) {
        String value = getString(key);
        if (value != null) {
            try {
                return Byte.parseByte(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to byte", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public byte getByte(String key, byte defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return Byte.parseByte(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to byte", e);
            }
        } else {
            return defaultValue;
        }
    }

    public double getDouble(String key) {
        String value = getString(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to double", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public double getDouble(String key, double defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to double", e);
            }
        } else {
            return defaultValue;
        }
    }

    public float getFloat(String key) {
        String value = getString(key);
        if (value != null) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to float", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public float getFloat(String key, float defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to float", e);
            }
        } else {
            return defaultValue;
        }
    }

    public BigInteger getBigInteger(String key) {
        String value = getString(key);
        if (value != null) {
            try {
                return BigInteger.valueOf(Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to BigInteger", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return BigInteger.valueOf(Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to BigInteger", e);
            }
        } else {
            return defaultValue;
        }
    }

    public BigDecimal getBigDecimal(String key) {
        String value = getString(key);
        if (value != null) {
            try {
                return BigDecimal.valueOf(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to BigDecimal", e);
            }
        } else {
            throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] is null");
        }
    }

    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        String value = getString(key);
        if (value != null) {
            try {
                return BigDecimal.valueOf(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value [" + value + "] for key [" + key + "] can't be parsed to BigDecimal", e);
            }
        } else {
            return defaultValue;
        }
    }

    public void mergeProperties(ThunderProperties properties) {
        map.putAll(properties.getMap());
    }

    // 抽取全局配置中的配置分组，key为组名，例如kafka
    public Map<String, ThunderProperties> extractProperties(String group) {
        Map<String, ThunderProperties> propertiesMap = Maps.newConcurrentMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.toLowerCase().startsWith(group.toLowerCase() + LINE)) {
                int index = key.indexOf(DOT);
                if (index < 0) {
                    throw new IllegalArgumentException("Key [" + key + "] is an invalid format");
                }

                String tag = key.substring(0, index);
                String name = key.substring(index + 1);
                ThunderProperties subProperties = propertiesMap.get(tag);
                if (subProperties == null) {
                    subProperties = new ThunderProperties();
                    propertiesMap.put(tag, subProperties);
                }
                subProperties.put(name, value);
            }
        }

        return propertiesMap;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}