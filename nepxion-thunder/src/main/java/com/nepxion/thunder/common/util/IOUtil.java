package com.nepxion.thunder.common.util;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class IOUtil {
    public static String read(String path, String encoding) throws Exception {
        String content = null;
        
        InputStream inputStream = null;
        try {
            // 从Resource路径获取
            inputStream = IOUtil.class.getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                // 从文件路径获取
                inputStream = new FileInputStream(path);
            }
            content = IOUtils.toString(inputStream, encoding);
            
            /*List<String> lines = IOUtils.readLines(inputStream, encoding);
            StringBuilder builder = new StringBuilder();
            for (String line : lines) {
                builder.append(line + "\n");
            }

            content = builder.toString();*/
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
        
        return content;
    }
}