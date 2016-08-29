package com.nepxion.thunder.test.service;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Neptune
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.springframework.context.ApplicationContext;

public abstract class EchoClientPressureTest extends AbstractJavaSamplerClient {
    private EchoService echoService;

    @Override
    public void setupTest(JavaSamplerContext context) {
        echoService = (EchoService) getApplicationContext().getBean("echoService");
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            echoService.echo(getText());
            result.setSuccessful(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccessful(false);
        }
        result.sampleEnd();

        return result;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {

    }

    protected abstract ApplicationContext getApplicationContext();

    protected abstract String getText();
}