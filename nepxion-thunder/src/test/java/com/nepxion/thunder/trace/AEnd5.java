package com.nepxion.thunder.trace;

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
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.trace.service.BInterface1;
import com.nepxion.thunder.trace.service.Constants;

public class AEnd5 {    
    // 跨服务器先异步后同步调用
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        System.setProperty(ThunderConstants.PORT_PARAMETER_NAME, "1004");
        
        // ApplicationContext applicationContext = new FileSystemXmlApplicationContext("file://192.168.15.82\\Thunder\\Trace\\trace-a-context.xml"); 
        // ApplicationContext applicationContext = new ClassPathXmlApplicationContext("http://www.nepxion.com/Thunder/Trace/trace-a-context.xml");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:trace-a-context.xml");
        BInterface1 bInterface1 = (BInterface1) applicationContext.getBean("bInterface1");
        for (int i = 0; i < Constants.COUNT; i++) {
            String traceId = "A5(" + i + ")";
            bInterface1.async2ToB(traceId, traceId);
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}