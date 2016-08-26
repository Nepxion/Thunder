package com.nepxion.thunder.console.context;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;

import com.nepxion.swing.exception.ExceptionTracerContext;
import com.nepxion.util.encoder.EncoderContext;
import com.nepxion.util.locale.LocaleContext;

public class DataContext {
    private static final String CHARSET = "charset";
    private static final String LOCALE = "locale";

    public static void initialize() {
        initializeEncoder();
        initializeLocale();
        initializeTracer();
    }

    private static void initializeEncoder() {
        String charset = null;
        try {
            charset = PropertiesContext.getProperties().getString(CHARSET);
        } catch (Exception e) {

        }

        if (StringUtils.isNotEmpty(charset)) {
            EncoderContext.registerIOCharset(charset);
        }
    }

    private static void initializeLocale() {
        String locale = null;
        try {
            locale = PropertiesContext.getProperties().getString(LOCALE);
        } catch (Exception e) {

        }

        if (StringUtils.isNotEmpty(locale)) {
            LocaleContext.registerLocale(locale);
        }
    }

    private static void initializeTracer() {
        ExceptionTracerContext.register(true);
    }
}