package com.nepxion.thunder.stock.service;

/**
 * <p>Title: Nepxion Stock</p>
 * <p>Description: Nepxion Stock For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.thunder.stock.constant.StockConstants;
import com.nepxion.thunder.stock.entity.Stock;
import com.nepxion.thunder.stock.entity.StockPankou;
import com.nepxion.thunder.stock.entity.StockPrice;
import com.nepxion.thunder.stock.entity.StockZhuli;

public class TencentStockCallback extends AbstractStockCallback {

    @Override
    public List<Stock> convert(String result) {
        List<Stock> stockList = new ArrayList<Stock>();

        String[] contentList = StringUtils.split(result, "\n\r");
        for (int i = 0; i < contentList.length; i++) {
            String code = codes[i].trim();
            String content = contentList[i].trim();
            switch (stockType) {
                case PRICE:
                    stockList.add(createPrice(code, content));
                    break;
                case ZHULI:
                    stockList.add(createZhuli(code, content));
                    break;
                case PANKOU:
                    stockList.add(createPankou(code, content));
                    break;
            }
        }

        return stockList;
    }
    
    private StockPrice createPrice(String code, String content) {
        String[] parameterList = createParameterList(content);

        String time = parameterList[30];
        if (time.equals("0.00")) {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        } else {
            time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " " + 
                   time.substring(8, 10) + ":" + time.substring(10, 12) + ":" + time.substring(12, 14);
        }
        
        StockPrice price = new StockPrice();
        price.setInterfaceType(interfaceType);
        price.setStockType(stockType);
        price.setTime(time);
        price.setName(parameterList[1]);
        price.setCode(code);
        price.setSimple(simple);
        price.setCurrentPrice(Float.parseFloat(parameterList[3]));
        price.setRangePercent(Float.parseFloat(parameterList[32]));
        price.setRange(Float.parseFloat(parameterList[31]));
        if (!code.equals(StockConstants.SH) && !code.equals(StockConstants.SZ) && !code.equals(StockConstants.CY)) {
            price.setTurnoverRate(Float.parseFloat(parameterList[38]));
        }
        try {
            price.setHighPrice(Float.parseFloat(parameterList[33]));
        } catch (NumberFormatException e) {
        }
        try {
            price.setLowPrice(Float.parseFloat(parameterList[34]));
        } catch (NumberFormatException e) {
        }
        price.setTodayFirstPrice(Float.parseFloat(parameterList[5]));
        price.setYesterdayLastPrice(Float.parseFloat(parameterList[4]));
        price.setTurnoverNumber(Float.parseFloat(parameterList[36]));
        price.setTurnoverMoney(Float.parseFloat(parameterList[37]));
        
        return price;
    }
    
    private StockZhuli createZhuli(String code, String content) {
        String[] parameterList = createParameterList(content);

        String time = parameterList[13];
        if (time.equals("0.00")) {
            time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        } else {
            time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);
        }

        StockZhuli zhuli = new StockZhuli();
        zhuli.setInterfaceType(interfaceType);
        zhuli.setStockType(stockType);
        zhuli.setTime(time);
        zhuli.setName(parameterList[12]);
        zhuli.setCode(code);
        zhuli.setZhuliLiuru(Float.parseFloat(parameterList[1]));
        zhuli.setZhuliLiuchu(Float.parseFloat(parameterList[2]));
        zhuli.setZhuliJingliuru(Float.parseFloat(parameterList[3]));
        zhuli.setZhuliJingliuruAll(Float.parseFloat(parameterList[4]));
        zhuli.setSanhuLiuru(Float.parseFloat(parameterList[5]));
        zhuli.setSanhuLiuchu(Float.parseFloat(parameterList[6]));
        zhuli.setSanhuJingliuru(Float.parseFloat(parameterList[7]));
        zhuli.setSanhuJingliuruAll(Float.parseFloat(parameterList[8]));
        zhuli.setAll(Float.parseFloat(parameterList[9]));
        
        return zhuli;
    }
    
    private StockPankou createPankou(String code, String content) {
        String[] parameterList = createParameterList(content);
        
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        
        StockPankou pankou = new StockPankou();
        pankou.setInterfaceType(interfaceType);
        pankou.setStockType(stockType);
        pankou.setTime(time);
        pankou.setCode(code);
        pankou.setBuyBig(Float.parseFloat(parameterList[0]));
        pankou.setBuySmall(Float.parseFloat(parameterList[1]));
        pankou.setSellBig(Float.parseFloat(parameterList[2]));
        pankou.setSellSmall(Float.parseFloat(parameterList[3]));
        
        return pankou;
    }
    
    private String[] createParameterList(String content) {
        content = content.substring(content.indexOf("\"") + 1, content.lastIndexOf("\""));
        
        return StringUtils.split(content, "~");
    }
}