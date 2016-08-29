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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.stock.entity.InterfaceType;
import com.nepxion.thunder.stock.entity.Stock;
import com.nepxion.thunder.stock.entity.StockType;

public abstract class AbstractStockCallback implements FutureCallback<HttpResponse> {
    protected String[] codes;
    protected StockType stockType;
    protected InterfaceType interfaceType;
    protected boolean simple;
    protected String id;
    protected Map<String, List<Stock>> cacheMap;
    protected CyclicBarrier barrier;
    
    @Override
    public void completed(HttpResponse httpResponse) {
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = null;

        try {
            result = EntityUtils.toString(httpEntity, ThunderConstants.ENCODING_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Stock> stocks = convert(result);
        cacheMap.put(id, stocks);
        
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelled() {

    }

    @Override
    public void failed(Exception httpResponse) {

    }

    public String[] getCodes() {
        return codes;
    }

    public void setCodes(String[] codes) {
        this.codes = codes;
    }

    public StockType getStockType() {
        return stockType;
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
    }

    public InterfaceType getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(InterfaceType interfaceType) {
        this.interfaceType = interfaceType;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, List<Stock>> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(Map<String, List<Stock>> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }
    
    public abstract List<Stock> convert(String result);
}