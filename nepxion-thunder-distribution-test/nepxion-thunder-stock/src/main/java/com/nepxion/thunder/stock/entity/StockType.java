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

public enum StockType {
    PRICE("price", "股价"),
    ZHULI("zhuli", "主力"),
    PANKOU("pankou", "盘口");

    private String value;
    private String description;

    private StockType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }

    public static StockType fromString(String value) {
        for (StockType type : StockType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("No matched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}