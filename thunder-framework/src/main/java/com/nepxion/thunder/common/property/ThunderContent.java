package com.nepxion.thunder.common.property;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.nepxion.thunder.common.util.IOUtil;

public class ThunderContent {
    private String content;

    public ThunderContent(String path, String encoding) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = IOUtil.getInputStream(path);
            this.content = IOUtils.toString(inputStream, encoding);
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    public ThunderContent(File file, String encoding) throws IOException {
        this.content = FileUtils.readFileToString(file, encoding);
    }

    public ThunderContent(StringBuilder stringBuilder) throws IOException {
        this.content = stringBuilder.toString();
    }

    public String getContent() {
        return content;
    }
}