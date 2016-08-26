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

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nepxion.thunder.common.constant.ThunderConstants;

public class BEnd1 {    
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        System.setProperty(ThunderConstants.PORT_PARAMETER_NAME, "2000");
        
        // new FileSystemXmlApplicationContext("file://192.168.15.82\\Thunder\\Trace\\trace-b-context.xml"); 
        // new ClassPathXmlApplicationContext("http://www.nepxion.com/Thunder/Trace/trace-b-context.xml");
        new ClassPathXmlApplicationContext("classpath*:trace-b-context.xml");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}