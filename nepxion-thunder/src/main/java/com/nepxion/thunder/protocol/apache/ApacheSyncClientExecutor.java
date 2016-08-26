package com.nepxion.thunder.protocol.apache;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.property.ThunderProperties;

public class ApacheSyncClientExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ApacheSyncClientExecutor.class);

    private CloseableHttpClient httpSyncClient;
    
    public void initialize(ThunderProperties properties) throws Exception {
        initialize(properties, false);
    }

    public void initialize(ThunderProperties properties, boolean https) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(properties.getInteger(ThunderConstants.APACHE_CONNECT_TIMEOUT_ATTRIBUTE_NAME))
                .setConnectionRequestTimeout(properties.getInteger(ThunderConstants.APACHE_CONNECT_TIMEOUT_ATTRIBUTE_NAME))
                .setSocketTimeout(properties.getInteger(ThunderConstants.APACHE_SO_TIMEOUT_ATTRIBUTE_NAME))
                .build();

        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setDefaultRequestConfig(requestConfig);

        if (https) {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }

            }).build();
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

            clientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
        }

        httpSyncClient = clientBuilder.build();

        LOG.info("Create apache sync client with {} successfully", https ? "https mode" : "http mode");
    }

    public CloseableHttpClient getClient() {
        return httpSyncClient;
    }
}