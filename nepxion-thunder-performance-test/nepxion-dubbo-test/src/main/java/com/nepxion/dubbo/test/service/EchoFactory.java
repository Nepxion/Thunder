package com.nepxion.dubbo.test.service;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.util.IOUtil;

public class EchoFactory {
    public static String text = "abcdefghijklmnop";
    public static String bytes_10;
    public static String bytes_250;
    public static String bytes_2K;
    public static String bytes_16K;
    public static String bytes_60K;
    public static String bytes_260K;
    public static String bytes_520K;
    public static String bytes_input;

    static {
        bytes_10 = initialize(0);
        bytes_250 = initialize(4);
        bytes_2K = initialize(7);
        bytes_16K = initialize(10);
        bytes_60K = initialize(12);
        bytes_260K = initialize(14);
        bytes_520K = initialize(15);
        try {
            bytes_input = IOUtil.read("dubbo-input.txt", ThunderConstants.ENCODING_FORMAT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String initialize(int loop) {
        StringBuilder builder = new StringBuilder();

        builder.append(text);
        for (int i = 0; i < loop; i++) {
            builder.append(builder.toString());
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println("序列化对象长度：" + bytes_10.getBytes().length);
        System.out.println("序列化对象长度：" + bytes_250.getBytes().length);
        System.out.println("序列化对象长度：" + bytes_2K.getBytes().length);
        System.out.println("序列化对象长度：" + bytes_16K.getBytes().length);
        System.out.println("序列化对象长度：" + bytes_60K.getBytes().length);
        System.out.println("序列化对象长度：" + bytes_260K.getBytes().length);
        System.out.println("序列化对象长度：" + bytes_520K.getBytes().length);
        System.out.println("序列化对象长度：" + bytes_input.getBytes().length);
    }
}