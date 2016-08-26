package com.nepxion.thunder.testcase.splunk;

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
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.protocol.apache.ApacheSyncClientExecutor;

public class SplunkTest {
    private static final Logger LOG = LoggerFactory.getLogger(SplunkTest.class);

    private CloseableHttpClient httpSyncClient;
    private String loginUrl = "https://192.168.63.112:8089/services/auth/login";
    private String dataUrl = "https://192.168.63.112:8089/servicesNS/admin/search/search/jobs/export/";

    @Test
    public void test() throws Exception {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();

        ApacheSyncClientExecutor clientExecutor = new ApacheSyncClientExecutor();
        clientExecutor.initialize(properties, true);
        httpSyncClient = clientExecutor.getClient();

        String sessionKey = login();
        get(sessionKey);
    }

    public String login() throws Exception {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("username", "admin"));
        parameters.add(new BasicNameValuePair("password", "Aa123456"));

        HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");

        HttpPost httpPost = new HttpPost(loginUrl);
        httpPost.setEntity(entity);

        HttpResponse httpResponse = httpSyncClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = null;

        try {
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sessionKey = result.substring(result.indexOf("<sessionKey>") + "<sessionKey>".length(), result.lastIndexOf("</sessionKey>"));
        LOG.info("sessionKey is {}", sessionKey);

        return sessionKey;
    }

    public void get(String sessionKey) throws Exception {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("search", "search 'source=\"thunder.log\" sourcetype=\"thunder\" traceId=\"A1(1)\"'"));
        // parameters.add(new BasicNameValuePair("output_mode", "json"));

        HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");

        HttpPost httpPost = new HttpPost(dataUrl);
        httpPost.addHeader("Authorization", "Splunk " + sessionKey);
        httpPost.setEntity(entity);

        HttpResponse httpResponse = httpSyncClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = null;

        try {
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info(result);
    }
}