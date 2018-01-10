package com.nepxion.thunder.stock.service;

/**
 * <p>Title: Nepxion Stock</p>
 * <p>Description: Nepxion Stock For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.util.RandomUtil;
import com.nepxion.thunder.stock.entity.InterfaceType;
import com.nepxion.thunder.stock.entity.StockType;

public class SinaStockServiceImpl extends AbstractStockServiceImpl {
    private static final String URL = "http://hq.sinajs.cn/";

    @Override
    public String getUrl(String[] codes, StockType stockType) {
        String url = URL + "r=" + RandomUtil.uuidRandom() + "&list=";
        switch (stockType) {
            case PRICE:
                for (String code : codes) {
                    url += code + ",";
                }
                return url.substring(0, url.lastIndexOf(","));
            case ZHULI:
                return null;
            case PANKOU:
                return null;
        }

        return null;
    }

    @Override
    public AbstractStockCallback getCallback() {
        return new SinaStockCallback();
    }

    @Override
    public InterfaceType getInterfaceType() {
        return InterfaceType.SINA;
    }
}