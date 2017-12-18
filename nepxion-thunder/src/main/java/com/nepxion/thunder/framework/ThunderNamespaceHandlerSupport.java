package com.nepxion.thunder.framework;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.property.ThunderPropertiesManager;
import com.nepxion.thunder.framework.parser.ApplicationBeanDefinitionParser;
import com.nepxion.thunder.framework.parser.MethodBeanDefinitionParser;
import com.nepxion.thunder.framework.parser.MonitorBeanDefinitionParser;
import com.nepxion.thunder.framework.parser.ProtocolBeanDefinitionParser;
import com.nepxion.thunder.framework.parser.ReferenceBeanDefinitionParser;
import com.nepxion.thunder.framework.parser.RegistryBeanDefinitionParser;
import com.nepxion.thunder.framework.parser.ServiceBeanDefinitionParser;
import com.nepxion.thunder.framework.parser.StrategyBeanDefinitionParser;

public class ThunderNamespaceHandlerSupport extends NamespaceHandlerSupport {
    static {
        System.out.println("");
        System.out.println("╔════╦╗         ╔╗");
        System.out.println("║╔╗╔╗║║         ║║");
        System.out.println("╚╝║║╚╣╚═╦╗╔╦═╗╔═╝╠══╦═╗");
        System.out.println("  ║║ ║╔╗║║║║╔╗╣╔╗║║═╣╔╝");
        System.out.println("  ║║ ║║║║╚╝║║║║╚╝║║═╣║");
        System.out.println("  ╚╝ ╚╝╚╩══╩╝╚╩══╩══╩╝");
        System.out.println("Nepxion Thunder  v1.0.0.RELEASE");
        System.out.println("");
    }
    
    @Override
    public void init() {
        ThunderProperties properties = ThunderPropertiesManager.getProperties();
                
        CacheContainer cacheContainer = new CacheContainer();
        ExecutorContainer executorContainer = new ExecutorContainer();
        
        ThunderDelegate delegate = new ThunderDelegateImpl();
        delegate.setProperties(properties);
        delegate.setCacheContainer(cacheContainer);
        delegate.setExecutorContainer(executorContainer);

        registerBeanDefinitionParser(ThunderConstants.APPLICATION_ELEMENT_NAME, new ApplicationBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstants.PROTOCOL_ELEMENT_NAME, new ProtocolBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstants.REGISTRY_ELEMENT_NAME, new RegistryBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstants.STRATEGY_ELEMENT_NAME, new StrategyBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstants.MONITOR_ELEMENT_NAME, new MonitorBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstants.SERVICE_ELEMENT_NAME, new ServiceBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstants.REFERENCE_ELEMENT_NAME, new ReferenceBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstants.METHOD_ELEMENT_NAME, new MethodBeanDefinitionParser(delegate));
    }
}