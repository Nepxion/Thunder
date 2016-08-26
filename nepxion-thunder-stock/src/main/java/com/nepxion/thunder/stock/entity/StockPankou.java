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


public class StockPankou extends Stock {
    private static final long serialVersionUID = -5544055675566806124L;
    
    private float buyBig = -1;
    private float buySmall = -1;
    private float sellBig = -1;
    private float sellSmall = -1;

    public float getBuyBig() {
        return buyBig;
    }

    public void setBuyBig(float buyBig) {
        this.buyBig = buyBig;
    }

    public float getBuySmall() {
        return buySmall;
    }

    public void setBuySmall(float buySmall) {
        this.buySmall = buySmall;
    }

    public float getSellBig() {
        return sellBig;
    }

    public void setSellBig(float sellBig) {
        this.sellBig = sellBig;
    }

    public float getSellSmall() {
        return sellSmall;
    }

    public void setSellSmall(float sellSmall) {
        this.sellSmall = sellSmall;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        
        if (buyBig != -1) {
            buffer.append(": 买盘大单=");
            buffer.append(buyBig + "万手");
        }
        if (buySmall != -1) {
            buffer.append(" 买盘小单=");
            buffer.append(buySmall + "万手");
        }
        if (sellBig != -1) {
            buffer.append(" 卖盘大单=");
            buffer.append(sellBig + "万手");
        }
        if (sellSmall != -1) {
            buffer.append(" 卖盘小单=");
            buffer.append(sellSmall + "万手");
        }

        return  buffer.toString();
    }
}