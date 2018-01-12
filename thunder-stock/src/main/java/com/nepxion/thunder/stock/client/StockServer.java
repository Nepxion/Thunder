package com.nepxion.thunder.stock.client;

/**
 * <p>Title: Nepxion Stock</p>
 * <p>Description: Nepxion Stock For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StockServer {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath*:stock-server-context.xml");
    }
}