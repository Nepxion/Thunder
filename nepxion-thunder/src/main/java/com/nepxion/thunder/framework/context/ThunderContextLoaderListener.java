package com.nepxion.thunder.framework.context;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Map;

import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.framework.bean.ApplicationFactoryBean;
import com.nepxion.thunder.protocol.hessian.HessianServletGenerator;

public class ThunderContextLoaderListener extends ContextLoaderListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        
        String path = event.getServletContext().getRealPath("");
        
        generateHessianServlet(applicationContext, path);
    }
    
    private void generateHessianServlet(ApplicationContext applicationContext, String path) {
        ApplicationFactoryBean applicationFactoryBean = applicationContext.getBean(ApplicationFactoryBean.class);
        
        Map<String, ServiceEntity> serviceEntityMap = applicationFactoryBean.getCacheContainer().getServiceEntityMap();
        ThunderProperties properties = applicationFactoryBean.getProperties();
        
        HessianServletGenerator servletGenerator = new HessianServletGenerator(serviceEntityMap, properties, path);
        servletGenerator.generate();
    }
}