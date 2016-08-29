package com.nepxion.thunder.stock.util;

/**
 * <p>Title: Nepxion Stock</p>
 * <p>Description: Nepxion Stock For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.math.BigDecimal;

public class StockUtil {
    
    public static float scale(float value, int scale) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static double scale(double value, int scale) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}