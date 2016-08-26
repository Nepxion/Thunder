package com.nepxion.thunder.external;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExternalTest {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:netty-external-context.xml");

        InjectionService injectionService = (InjectionService) applicationContext.getBean("injectionService");
        System.out.println("injectionService : " + injectionService.getUser("Zhangsan"));

        AutowireService autowireService = (AutowireService) applicationContext.getBean("autowireService");
        System.out.println("autowireService : " + autowireService.getUser("Lisi"));
    }
}