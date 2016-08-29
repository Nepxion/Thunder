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

import com.nepxion.thunder.common.util.RandomUtil;
import com.nepxion.thunder.stock.entity.InterfaceType;
import com.nepxion.thunder.stock.entity.StockType;

public class TencentStockServiceImpl extends AbstractStockServiceImpl {
    private static final String URL = "http://qt.gtimg.cn/";

    @Override
    public String getUrl(String[] codes, StockType stockType) {
        String url = URL + "r=" + RandomUtil.uuidRandom() + "&q=";
        switch (stockType) {
            case PRICE:
                for (String code : codes) {
                    url += code + ",";
                }
                return url.substring(0, url.lastIndexOf(","));
            case ZHULI:
                for (String code : codes) {
                    url += "ff_" + code + ",";
                }
                return url.substring(0, url.lastIndexOf(","));
            case PANKOU:
                for (String code : codes) {
                    url += "s_pk" + code + ",";
                }
                return url.substring(0, url.lastIndexOf(","));
        }

        return null;
    }

    @Override
    public AbstractStockCallback getCallback() {
        return new TencentStockCallback();
    }

    @Override
    public InterfaceType getInterfaceType() {
        return InterfaceType.TENCENT;
    }
}