package com.nepxion.thunder.console.locale;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Locale;

import com.nepxion.util.locale.LocaleManager;

public class ConsoleLocale {
    public static final Class<?> BUNDLE_CLASS = ConsoleLocale.class;

    public static String getString(String key) {
        return LocaleManager.getString(BUNDLE_CLASS, key);
    }

    public static String getString(String key, Locale locale) {
        return LocaleManager.getString(BUNDLE_CLASS, key, locale);
    }
}