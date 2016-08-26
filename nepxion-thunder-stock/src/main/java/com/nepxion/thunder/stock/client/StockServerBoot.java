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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@ImportResource({ "classpath*:stock-server-context.xml" })
public class StockServerBoot {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(StockServerBoot.class, args);
    }
}