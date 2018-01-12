package com.nepxion.thunder.stock.service;

/**
 * <p>Title: Nepxion Stock</p>
 * <p>Description: Nepxion Stock For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import com.google.common.collect.Maps;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.common.util.RandomUtil;
import com.nepxion.thunder.protocol.apache.ApacheAsyncClientExecutor;
import com.nepxion.thunder.stock.entity.InterfaceType;
import com.nepxion.thunder.stock.entity.Stock;
import com.nepxion.thunder.stock.entity.StockType;

public abstract class AbstractStockServiceImpl implements StockService {
    protected ApacheAsyncClientExecutor clientExecutor;
    protected Map<String, List<Stock>> cacheMap = Maps.newConcurrentMap();
    protected CyclicBarrier barrier = new CyclicBarrier(2);

    public AbstractStockServiceImpl() {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();

        clientExecutor = new ApacheAsyncClientExecutor();
        try {
            clientExecutor.initialize(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Stock> execute(String[] codes, StockType stockType, boolean simple) {
        barrier.reset();

        InterfaceType interfaceType = getInterfaceType();
        if (interfaceType == InterfaceType.SINA) {
            if (stockType == StockType.ZHULI || stockType == StockType.PANKOU) {
                return null;
            }
        }

        String url = getUrl(codes, stockType);
        if (url == null) {
            return null;
        }

        String id = RandomUtil.uuidRandom();

        AbstractStockCallback callback = getCallback();
        callback.setCodes(codes);
        callback.setStockType(stockType);
        callback.setInterfaceType(interfaceType);
        callback.setSimple(simple);
        callback.setId(id);
        callback.setCacheMap(cacheMap);
        callback.setBarrier(barrier);

        HttpGet httpGet = new HttpGet(url);

        CloseableHttpAsyncClient httpAsyncClient = clientExecutor.getClient();
        httpAsyncClient.execute(httpGet, callback);
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            httpGet.reset();
        }

        List<Stock> stock = cacheMap.get(id);
        cacheMap.remove(id);

        return stock;
    }

    public abstract String getUrl(String[] codes, StockType stockType);

    public abstract AbstractStockCallback getCallback();

    public abstract InterfaceType getInterfaceType();
}