package com.nepxion.thunder.test;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ImportResource;

// 该类的作用是改端口，预防端口冲突
@EnableAutoConfiguration
@ImportResource({ "classpath*:netty-client-context.xml" })
public class NettyClientBootEmbeddedRunnerTest extends NettyClientBootRunnerTest implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8081);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyClientBootEmbeddedRunnerTest.class, args);
    }
}