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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.thunder.stock.constant.StockConstants;
import com.nepxion.thunder.stock.entity.Stock;
import com.nepxion.thunder.stock.entity.StockPrice;
import com.nepxion.thunder.stock.util.StockUtil;

public class SinaStockCallback extends AbstractStockCallback {

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
                case PANKOU:
                    break;
                case ZHULI:
                    break;
            }
        }

        return stockList;
    }

    private StockPrice createPrice(String code, String content) {
        String[] parameterList = createParameterList(content);

        float range = Float.parseFloat(parameterList[3]) - Float.parseFloat(parameterList[2]);
        float rangePercent = (Float.parseFloat(parameterList[3]) - Float.parseFloat(parameterList[2])) / Float.parseFloat(parameterList[2]) * 100;

        StockPrice price = new StockPrice();
        price.setInterfaceType(interfaceType);
        price.setStockType(stockType);
        price.setTime(parameterList[30] + " " + parameterList[31]);
        price.setName(parameterList[0]);
        price.setCode(code);
        price.setSimple(simple);
        price.setCurrentPrice(StockUtil.scale(Float.parseFloat(parameterList[3]), 2));
        price.setRangePercent(StockUtil.scale(rangePercent, 2));
        price.setRange(StockUtil.scale(range, 2));
        price.setHighPrice(StockUtil.scale(Float.parseFloat(parameterList[4]), 2));
        price.setLowPrice(StockUtil.scale(Float.parseFloat(parameterList[5]), 2));
        price.setTodayFirstPrice(StockUtil.scale(Float.parseFloat(parameterList[1]), 2));
        price.setYesterdayLastPrice(StockUtil.scale(Float.parseFloat(parameterList[2]), 2));
        price.setTurnoverNumber(code.equals(StockConstants.SH) ? Float.parseFloat(parameterList[8]) : Float.parseFloat(parameterList[8]) / 100);
        price.setTurnoverMoney(Float.parseFloat(parameterList[9]) / 10000);

        return price;
    }
    
    private String[] createParameterList(String content) {
        content = content.substring(content.indexOf("\"") + 1, content.lastIndexOf("\""));
        
        return StringUtils.split(content, ",");
    }
}