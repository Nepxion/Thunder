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

import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.stock.entity.InterfaceType;
import com.nepxion.thunder.stock.scheduler.StockScheduler;
import com.nepxion.thunder.stock.service.SinaStockServiceImpl;
import com.nepxion.thunder.stock.service.StockService;
import com.nepxion.thunder.stock.service.TencentStockServiceImpl;

public class StockClientStandalone {
    public static void main(String[] args) {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        String interfaceType = properties.getString("interfaceType");
        StockService stockService = null;
        if (interfaceType.equalsIgnoreCase(InterfaceType.SINA.toString())) {
            stockService = new SinaStockServiceImpl();
        } else if (interfaceType.equalsIgnoreCase(InterfaceType.TENCENT.toString())) {
            stockService = new TencentStockServiceImpl();
        }

        StockScheduler stockScheduler = new StockScheduler();
        stockScheduler.execute(stockService);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}