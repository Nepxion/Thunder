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

import com.nepxion.thunder.stock.util.StockUtil;

public class StockPrice extends Stock {
    private static final long serialVersionUID = -222139011386929614L;
    
    private boolean simple = true;
    
    private float currentPrice = -1;
    private float rangePercent = -1;
    private float range = -1;
    private float turnoverRate = -1;
    private float highPrice = -1;
    private float lowPrice = -1;
    private float todayFirstPrice = -1;
    private float yesterdayLastPrice = -1;
    private float turnoverNumber = -1; // 手
    private float turnoverMoney = -1; // 万元
    
    private float pe = -1;
    private float pbv = -1;
    private float totalAssets = - 1;
    private float flowAssets = -1;

    public StockPrice() {

    }
    
    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public float getRangePercent() {
        return rangePercent;
    }

    public void setRangePercent(float rangePercent) {
        this.rangePercent = rangePercent;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getTurnoverRate() {
        return turnoverRate;
    }

    public void setTurnoverRate(float turnoverRate) {
        this.turnoverRate = turnoverRate;
    }

    public float getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(float highPrice) {
        this.highPrice = highPrice;
    }

    public float getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(float lowPrice) {
        this.lowPrice = lowPrice;
    }

    public float getTodayFirstPrice() {
        return todayFirstPrice;
    }

    public void setTodayFirstPrice(float todayFirstPrice) {
        this.todayFirstPrice = todayFirstPrice;
    }

    public float getYesterdayLastPrice() {
        return yesterdayLastPrice;
    }

    public void setYesterdayLastPrice(float yesterdayLastPrice) {
        this.yesterdayLastPrice = yesterdayLastPrice;
    }

    public float getTurnoverNumber() {
        return turnoverNumber;
    }

    public void setTurnoverNumber(float turnoverNumber) {
        this.turnoverNumber = turnoverNumber;
    }

    public float getTurnoverMoney() {
        return turnoverMoney;
    }

    public void setTurnoverMoney(float turnoverMoney) {
        this.turnoverMoney = turnoverMoney;
    }
    
    public float getPe() {
        return pe;
    }

    public void setPe(float pe) {
        this.pe = pe;
    }

    public float getPbv() {
        return pbv;
    }

    public void setPbv(float pbv) {
        this.pbv = pbv;
    }
    
    public float getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(float totalAssets) {
        this.totalAssets = totalAssets;
    }

    public float getFlowAssets() {
        return flowAssets;
    }

    public void setFlowAssets(float flowAssets) {
        this.flowAssets = flowAssets;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        
        if (currentPrice != -1) {
            buffer.append(": 现价=");
            buffer.append(currentPrice);
        }
        if (rangePercent != -1) {
            buffer.append(" 涨幅=");
            buffer.append(rangePercent + "%");
        }
        if (range != -1) {
            buffer.append(" 涨跌=");
            buffer.append(range);
        }
        if (turnoverRate != -1) {
            buffer.append(" 换手率=");
            buffer.append(turnoverRate + "%");
        }

        if (!simple) {
            if (highPrice != -1) {
                buffer.append(" 最高=");
                buffer.append(highPrice);
            }
            if (lowPrice != -1) {
                buffer.append(" 最低=");
                buffer.append(lowPrice);
            }
            if (todayFirstPrice != -1) {
                buffer.append(" 今开=");
                buffer.append(todayFirstPrice);
            }
            if (yesterdayLastPrice != -1) {
                buffer.append(" 昨收=");
                buffer.append(yesterdayLastPrice);
            }
            if (turnoverNumber != -1) {
                buffer.append(" 成交量=");
                buffer.append(StockUtil.scale(turnoverNumber / 10000, 4) + "万手");
            }
            if (turnoverMoney != -1) {
                buffer.append(" 成交额=");
                buffer.append(StockUtil.scale(turnoverMoney / 10000, 4) + "亿元");
            }
            
            if (pe != -1) {
                buffer.append(" 市盈率=");
                buffer.append(pe);
            }
            
            if (pbv != -1) {
                buffer.append(" 市净率=");
                buffer.append(pbv);
            }
            
            if (totalAssets != -1) {
                buffer.append(" 总市值=");
                buffer.append(totalAssets);
            }
            
            if (flowAssets != -1) {
                buffer.append(" 流通市值=");
                buffer.append(flowAssets);
            }
        }

        return buffer.toString();
    }
}