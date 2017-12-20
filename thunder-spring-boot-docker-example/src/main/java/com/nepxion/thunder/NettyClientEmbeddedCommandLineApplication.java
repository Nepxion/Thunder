package com.nepxion.thunder;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
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
public class NettyClientEmbeddedCommandLineApplication extends NettyClientCommandLineApplication implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8082);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(NettyClientEmbeddedCommandLineApplication.class, args);
    }
}