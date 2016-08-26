package com.nepxion.thunder.testcase.http;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.protocol.apache.ApacheAsyncClientExecutor;
import com.nepxion.thunder.protocol.apache.ApacheSyncClientExecutor;

public class HttpTest {
    private static final Logger LOG = LoggerFactory.getLogger(HttpTest.class);

    @Test
    public void testAsync() throws Exception {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();

        ApacheAsyncClientExecutor clientExecutor = new ApacheAsyncClientExecutor();
        try {
            clientExecutor.initialize(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CloseableHttpAsyncClient httpAsyncClient = clientExecutor.getClient();
        final HttpGet httpGet = new HttpGet("http://www.baidu.com");
        final HttpPost httpPost = new HttpPost("http://www.baidu.com");
        httpAsyncClient.execute(httpPost, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                try {
                    LOG.info("异步调用:{}", EntityUtils.toString(result.getEntity()));
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                httpGet.reset();
                httpPost.reset();
                try {
                    httpAsyncClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception e) {
                LOG.error("Failed", e);
                httpGet.reset();
                httpPost.reset();
                try {
                    httpAsyncClient.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void cancelled() {
                LOG.info("Cancelled");
                httpGet.reset();
                httpPost.reset();
                try {
                    httpAsyncClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        System.in.read();
    }

    @Test
    public void testSync() throws Exception {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();

        ApacheSyncClientExecutor clientExecutor = new ApacheSyncClientExecutor();
        try {
            clientExecutor.initialize(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CloseableHttpClient httpSyncClient = clientExecutor.getClient();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        HttpPost httpPost = new HttpPost("http://www.baidu.com");
        CloseableHttpResponse response = httpSyncClient.execute(httpPost);
        try {
            LOG.info("同步调用:{}", EntityUtils.toString(response.getEntity()));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        httpGet.reset();
        httpPost.reset();
        try {
            httpSyncClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.in.read();
    }
}