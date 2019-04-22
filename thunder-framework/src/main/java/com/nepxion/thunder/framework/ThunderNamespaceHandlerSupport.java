package com.nepxion.thunder.framework;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.nepxion.thunder.common.constant.ThunderConstant;
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
import com.taobao.text.Color;

public class ThunderNamespaceHandlerSupport extends NamespaceHandlerSupport {
    static {
        /*String bannerShown = System.getProperty(BannerConstant.BANNER_SHOWN, "true");
        if (Boolean.valueOf(bannerShown)) {
            System.out.println("");
            System.out.println("╔════╦╗         ╔╗");
            System.out.println("║╔╗╔╗║║         ║║");
            System.out.println("╚╝║║╚╣╚═╦╗╔╦═╗╔═╝╠══╦═╗");
            System.out.println("  ║║ ║╔╗║║║║╔╗╣╔╗║║═╣╔╝");
            System.out.println("  ║║ ║║║║╚╝║║║║╚╝║║═╣║");
            System.out.println("  ╚╝ ╚╝╚╩══╩╝╚╩══╩══╩╝");
            System.out.println("Nepxion Thunder  v" + ThunderConstant.THUNDER_VERSION);
            System.out.println("");
        }*/

        LogoBanner logoBanner = new LogoBanner(ThunderNamespaceHandlerSupport.class, "/com/nepxion/thunder/resource/logo.txt", "Welcome to Nepxion", 7, 5, new Color[] { Color.red, Color.green, Color.cyan, Color.blue, Color.yellow, Color.magenta, Color.red }, true);

        NepxionBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", ThunderConstant.THUNDER_VERSION, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/Thunder", 0, 1));
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

        registerBeanDefinitionParser(ThunderConstant.APPLICATION_ELEMENT_NAME, new ApplicationBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstant.PROTOCOL_ELEMENT_NAME, new ProtocolBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstant.REGISTRY_ELEMENT_NAME, new RegistryBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstant.STRATEGY_ELEMENT_NAME, new StrategyBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstant.MONITOR_ELEMENT_NAME, new MonitorBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstant.SERVICE_ELEMENT_NAME, new ServiceBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstant.REFERENCE_ELEMENT_NAME, new ReferenceBeanDefinitionParser(delegate));
        registerBeanDefinitionParser(ThunderConstant.METHOD_ELEMENT_NAME, new MethodBeanDefinitionParser(delegate));
    }
}