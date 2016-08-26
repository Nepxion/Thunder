package com.nepxion.thunder.testcase.invocation;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.invocation.MethodInvocation;

public class InvocationTest {
    private static final Logger LOG = LoggerFactory.getLogger(InvocationTest.class);

    @Test
    public void test() throws Exception {
        InvocationEntity entity = new InvocationEntity();
        LOG.info("First name={}, age={}, ips={}, weight={}, height={}", entity.getName(), entity.getAge(), entity.getIps(), entity.getWeight(), entity.getHeight());
        
        MethodInvocation invocation1 = new MethodInvocation();
        invocation1.setMethodName("setName");
        invocation1.setParameterTypes(new Class<?>[] {String.class});
        invocation1.setParameters(new Object[] {"Zhangsan"});
        System.out.println(invocation1.invoke(entity));
        
        MethodInvocation invocation2 = new MethodInvocation();
        invocation2.setMethodName("setAge");
        invocation2.setParameterTypes(new Class<?>[] {int.class});
        invocation2.setParameters(new Object[] {18});
        System.out.println(invocation2.invoke(entity));
        
        MethodInvocation invocation3 = new MethodInvocation();
        invocation3.setMethodName("setIps");
        invocation3.setParameterTypes(new Class<?>[] {long[].class});
        invocation3.setParameters(new Object[] {new long[]{19216801, 19216802}});
        System.out.println(invocation3.invoke(entity));
        
        MethodInvocation invocation4 = new MethodInvocation();
        invocation4.setMethodName("setFacade");
        invocation4.setParameterTypes(new Class<?>[] {int.class, int.class});
        invocation4.setParameters(new Object[] {100, 170});
        System.out.println(invocation4.invoke(entity));

        LOG.info("Last name={}, age={}, ips={}, weight={}, height={}", entity.getName(), entity.getAge(), entity.getIps(), entity.getWeight(), entity.getHeight());
    }
}