package com.nepxion.thunder.protocol.hessian;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.caucho.HessianServiceExporter;

import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.framework.bean.ApplicationFactoryBean;
import com.nepxion.thunder.security.SecurityExecutor;

public class HessianAuthServiceExporter extends HessianServiceExporter implements ApplicationContextAware {     
    private ApplicationContext applicationContext;
    private SecurityExecutor securityExecutor;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        ApplicationFactoryBean applicationFactoryBean = applicationContext.getBean(ApplicationFactoryBean.class);
        ExecutorContainer executorContainer = applicationFactoryBean.getExecutorContainer();
        securityExecutor = executorContainer.getSecurityExecutor();
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityExecutor != null) {
            boolean authorized = securityExecutor.execute(request, response);
            if (!authorized) {
                return;
            }
        }

        super.handleRequest(request, response);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}