package com.nepxion.thunder.stock.entity;

/**
 * <p>Title: Nepxion Stock</p>
 * <p>Description: Nepxion Stock For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Stock implements Serializable {
    private static final long serialVersionUID = 8604102228148287094L;
    
    protected InterfaceType interfaceType;
    protected StockType stockType;
    protected String time;
    protected String name;
    protected String code;

    public InterfaceType getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(InterfaceType interfaceType) {
        this.interfaceType = interfaceType;
    }
    
    public StockType getStockType() {
        return stockType;
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(interfaceType.getDescription());
        buffer.append(stockType.getDescription() + " - ");
        if (!StringUtils.isEmpty(time)) {
            buffer.append(time + " ");
        }
        if (!StringUtils.isEmpty(name)) {
            buffer.append(name + " ");
        }
        if (!StringUtils.isEmpty(code)) {
            buffer.append(code.substring(2, code.length()) + " ");
        }
        
        return buffer.toString();
    }
}