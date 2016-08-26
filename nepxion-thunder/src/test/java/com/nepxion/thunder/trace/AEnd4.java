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
import com.nepxion.thunder.common.promise.PromiseDone;
import com.nepxion.thunder.common.promise.PromiseExecutor;
import com.nepxion.thunder.common.promise.PromiseFail;
import com.nepxion.thunder.common.promise.PromisePipe;
import com.nepxion.thunder.trace.service.BInterface1;
import com.nepxion.thunder.trace.service.CInterface;
import com.nepxion.thunder.trace.service.Constants;

public class AEnd4 {    
    // 本地链式两次异步调用
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        System.setProperty(ThunderConstants.PORT_PARAMETER_NAME, "1005");
        
        // ApplicationContext applicationContext = new FileSystemXmlApplicationContext("file://192.168.15.82\\Thunder\\Trace\\trace-a-context.xml"); 
        // ApplicationContext applicationContext = new ClassPathXmlApplicationContext("http://www.nepxion.com/Thunder/Trace/trace-a-context.xml");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:trace-a-context.xml");
        final BInterface1 bInterface1 = (BInterface1) applicationContext.getBean("bInterface1");
        final CInterface cInterface = (CInterface) applicationContext.getBean("cInterface");
        for (int i = 0; i < Constants.COUNT; i++) {
            final String traceId = "A4(" + i + ")";
            
            PromiseExecutor promiseExecutor = new PromiseExecutor();
            promiseExecutor.then(new PromisePipe<Void, String[]>() {
                @Override
                public void onResult(Void origin) {                    
                    bInterface1.async1ToB(traceId, traceId);
                }
            }).then(new PromisePipe<String[], String[]>() {
                @Override
                public void onResult(String[] result) {
                    String traceId = result[0];
                    String path = result[1] + " -> A";
                    
                    if (Constants.PRINT) {
                        System.out.println("链式调用：收到B端返回消息：" + path + "，并返回到A端");
                    }
                
                    cInterface.async1ToC(traceId, path);
                }
            }).done(new PromiseDone<String[]>() {
                @Override
                public void onDone(String[] result) {
                    String path = result[1] + " -> A";
                    
                    if (Constants.PRINT) {
                        System.out.println("链式调用：收到C端返回消息：" + path + "，并返回到A端");
                        System.out.println("链式调用结束");
                    }
                }
            }).fail(new PromiseFail() {
                @Override
                public void onFail(Exception exception) {
                    exception.printStackTrace();
                }
            });
            promiseExecutor.execute();

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