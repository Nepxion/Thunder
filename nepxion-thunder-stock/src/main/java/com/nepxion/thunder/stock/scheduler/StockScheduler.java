package com.nepxion.thunder.stock.scheduler;

/**
 * <p>Title: Nepxion Stock</p>
 * <p>Description: Nepxion Stock For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.stock.entity.Stock;
import com.nepxion.thunder.stock.entity.StockType;
import com.nepxion.thunder.stock.service.StockService;

public class StockScheduler {
    private Timer timer = new Timer();

    public void execute(StockService stockService) {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();
        
        String stock = properties.getString("stock");
        String stockType = properties.getString("stockType");
        int interval = properties.getInteger("interval");
        String simple = properties.getString("simple");
                
        String[] codes = StringUtils.split(stock, ";");
        StockType type = StockType.fromString(stockType.trim());
        boolean isSimple = Boolean.valueOf(simple);
        
        execute(stockService, codes, type, interval, isSimple);
    }
    
    public void execute(final StockService stockService, final String[] codes, final StockType stockType, final int interval, final boolean simple) {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                List<Stock> stocks = null;
                try {
                    stocks = stockService.execute(codes, stockType, simple);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!CollectionUtils.isEmpty(stocks)) {
                    for (Stock stock : stocks) {
                        System.out.println(stock);
                    }
                    System.out.println("--------------------------------------------------------------------------------");
                } else {
                    System.out.println("返回为空，不支持该分析");
                }
            }
        }, 0, interval);
    }
}