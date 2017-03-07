package com.nepxion.thunder.common.util;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtil {
    public static String toExceptionString(Exception exception) {
        if (exception == null) {
            return null;
        }

        return detailMessage(exception);
    }

    private static String detailMessage(Exception exception) {
        String result = null;
        ByteArrayOutputStream baos = null;
        PrintStream ps = null;
        try {
            baos = new ByteArrayOutputStream();
            ps = new PrintStream(baos);
            exception.printStackTrace(ps);
            result = baos.toString().trim();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /*private static String detailMessage(Exception exception) {
        String result = null;
        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            result = sw.toString().trim();
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sw != null) {
                try {
                    sw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }*/
}