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

public class StockZhuli extends Stock {
    private static final long serialVersionUID = -7235653693876264950L;
    
    private float zhuliLiuru = -1;
    private float zhuliLiuchu = -1;
    private float zhuliJingliuru = -1;
    private float zhuliJingliuruAll = -1;

    private float sanhuLiuru = -1;
    private float sanhuLiuchu = -1;
    private float sanhuJingliuru = -1;
    private float sanhuJingliuruAll = -1;

    private float all = -1;

    public float getZhuliLiuru() {
        return zhuliLiuru;
    }

    public void setZhuliLiuru(float zhuliLiuru) {
        this.zhuliLiuru = zhuliLiuru;
    }

    public float getZhuliLiuchu() {
        return zhuliLiuchu;
    }

    public void setZhuliLiuchu(float zhuliLiuchu) {
        this.zhuliLiuchu = zhuliLiuchu;
    }

    public float getZhuliJingliuru() {
        return zhuliJingliuru;
    }

    public void setZhuliJingliuru(float zhuliJingliuru) {
        this.zhuliJingliuru = zhuliJingliuru;
    }

    public float getZhuliJingliuruAll() {
        return zhuliJingliuruAll;
    }

    public void setZhuliJingliuruAll(float zhuliJingliuruAll) {
        this.zhuliJingliuruAll = zhuliJingliuruAll;
    }

    public float getSanhuLiuru() {
        return sanhuLiuru;
    }

    public void setSanhuLiuru(float sanhuLiuru) {
        this.sanhuLiuru = sanhuLiuru;
    }

    public float getSanhuLiuchu() {
        return sanhuLiuchu;
    }

    public void setSanhuLiuchu(float sanhuLiuchu) {
        this.sanhuLiuchu = sanhuLiuchu;
    }

    public float getSanhuJingliuru() {
        return sanhuJingliuru;
    }

    public void setSanhuJingliuru(float sanhuJingliuru) {
        this.sanhuJingliuru = sanhuJingliuru;
    }

    public float getSanhuJingliuruAll() {
        return sanhuJingliuruAll;
    }

    public void setSanhuJingliuruAll(float sanhuJingliuruAll) {
        this.sanhuJingliuruAll = sanhuJingliuruAll;
    }

    public float getAll() {
        return all;
    }

    public void setAll(float all) {
        this.all = all;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        
        if (zhuliLiuru != -1) {
            buffer.append(": 主力流入=");
            buffer.append(zhuliLiuru + "万元");
        }
        if (zhuliLiuchu != -1) {
            buffer.append(" 主力流出=");
            buffer.append(zhuliLiuchu + "万元");
        }
        if (zhuliJingliuru != -1) {
            buffer.append(" 主力净流入=");
            buffer.append(zhuliJingliuru + "万元");
        }
        if (zhuliJingliuruAll != -1) {
            buffer.append(" 主力净流入/资金流入流出总和=");
            buffer.append(zhuliJingliuruAll + "万元");
        }
        
        if (sanhuLiuru != -1) {
            buffer.append(" 散户流入=");
            buffer.append(sanhuLiuru + "万元");
        }
        if (sanhuLiuchu != -1) {
            buffer.append(" 散户流出=");
            buffer.append(sanhuLiuchu + "万元");
        }
        if (sanhuJingliuru != -1) {
            buffer.append(" 散户净流入=");
            buffer.append(sanhuJingliuru + "万元");
        }
        if (sanhuJingliuruAll != -1) {
            buffer.append(" 散户净流入/资金流入流出总和=");
            buffer.append(sanhuJingliuruAll + "万元");
        }
        
        if (all != -1) {
            buffer.append(" 资金流入流出总和=");
            buffer.append(all + "万元");
        }

        return buffer.toString();
    }
}