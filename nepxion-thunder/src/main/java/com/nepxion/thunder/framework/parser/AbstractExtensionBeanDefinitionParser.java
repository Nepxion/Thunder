package com.nepxion.thunder.framework.parser;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.container.ExecutorContainer;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.util.ClassUtil;

public abstract class AbstractExtensionBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final String DELEGATE = "delegate";
    
    protected ThunderDelegate delegate;
    protected ThunderProperties properties;
    protected CacheContainer cacheContainer;
    protected ExecutorContainer executorContainer;
    
    public AbstractExtensionBeanDefinitionParser(ThunderDelegate delegate) {
        this.delegate = delegate;
        this.properties = delegate.getProperties();
        this.cacheContainer = delegate.getCacheContainer();
        this.executorContainer = delegate.getExecutorContainer();
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) { 
        builder.addPropertyValue(DELEGATE, delegate);
    }
    
    protected <T> T createDelegate(String delegateClassId) throws Exception {        
        return delegate.createDelegate(delegateClassId);
    }
    
    protected String createBeanName(Class<?> clazz) {
        return ClassUtil.convertBeanName(clazz);
    }
}