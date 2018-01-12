package com.nepxion.thunder.monitor;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.entity.MonitorEntity;
import com.nepxion.thunder.common.entity.MonitorStat;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.protocol.apache.ApacheAsyncClientExecutor;
import com.nepxion.thunder.serialization.SerializerExecutor;

public class WebServiceMonitorExecutor extends AbstractMonitorExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(WebServiceMonitorExecutor.class);

    private CloseableHttpAsyncClient httpAsyncClient;

    @Override
    public void execute(MonitorStat monitorStat) throws Exception {
        MonitorEntity monitorEntity = cacheContainer.getMonitorEntity();
        List<String> addresses = monitorEntity.getAddresses();
        if (CollectionUtils.isEmpty(addresses)) {
            LOG.error("Webservice monitor addresses are null");

            return;
        }

        for (String address : addresses) {
            if (!address.startsWith("http://")) {
                address = "http://" + address;
            }

            String value = SerializerExecutor.toJson(monitorStat);

            HttpEntity entity = new StringEntity(value, ThunderConstant.ENCODING_UTF_8);

            HttpPost httpPost = new HttpPost(address);
            httpPost.addHeader("content-type", "application/json;charset=" + ThunderConstant.ENCODING_UTF_8);
            httpPost.setEntity(entity);

            HttpAsyncCallback httpAsyncCallback = new HttpAsyncCallback();
            httpAsyncCallback.setHttpPost(httpPost);

            httpAsyncClient.execute(httpPost, httpAsyncCallback);
        }
    }

    @Override
    public void setProperties(ThunderProperties properties) {
        super.setProperties(properties);

        try {
            ApacheAsyncClientExecutor clientExecutor = new ApacheAsyncClientExecutor();
            clientExecutor.initialize(properties);
            httpAsyncClient = clientExecutor.getClient();
        } catch (Exception e) {
            LOG.error("Get htty async client failed", e);
        }
    }

    public class HttpAsyncCallback implements FutureCallback<HttpResponse> {
        private HttpPost httpPost;

        public void setHttpPost(HttpPost httpPost) {
            this.httpPost = httpPost;
        }

        @Override
        public void completed(HttpResponse httpResponse) {
            httpPost.reset();
        }

        @Override
        public void failed(Exception e) {
            httpPost.reset();

            LOG.error("Monitor web service invoke failed, url={}", httpPost.getURI(), e);
        }

        @Override
        public void cancelled() {
            httpPost.reset();
        }
    }
}