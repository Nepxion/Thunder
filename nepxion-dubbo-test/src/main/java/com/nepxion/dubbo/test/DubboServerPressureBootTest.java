package com.nepxion.dubbo.test;

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
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@ImportResource({ "classpath*:dubbo-server-context-pressure.xml" })
public class DubboServerPressureBootTest {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(DubboServerPressureBootTest.class, args);
    }
}