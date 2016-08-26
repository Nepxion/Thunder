package com.nepxion.thunder.stock.client;

/**
 * <p>Title: Nepxion Stock</p>
 * <p>Description: Nepxion Stock For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nepxion.thunder.stock.scheduler.StockScheduler;
import com.nepxion.thunder.stock.service.StockService;

public class StockClient {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:stock-client-context.xml");
        StockService stockService = (StockService) context.getBean("stockService");

        StockScheduler stockScheduler = new StockScheduler();
        stockScheduler.execute(stockService);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}