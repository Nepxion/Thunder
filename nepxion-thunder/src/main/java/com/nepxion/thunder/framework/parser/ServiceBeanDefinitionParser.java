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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.framework.bean.ServiceFactoryBean;
import com.nepxion.thunder.framework.exception.FrameworkException;
import com.nepxion.thunder.framework.exception.FrameworkExceptionFactory;
import com.nepxion.thunder.protocol.ServerExecutor;
import com.nepxion.thunder.protocol.ServerExecutorAdapter;
import com.nepxion.thunder.security.SecurityExecutor;

public class ServiceBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    
    public ServiceBeanDefinitionParser(ThunderDelegate delegate) {
        super(delegate);
    }
    
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);
        
        String namespaceElementName = properties.getString(ThunderConstants.NAMESPACE_ELEMENT_NAME);
        String serviceElementName = ThunderConstants.SERVICE_ELEMENT_NAME;
        String interfaceAttributeName = ThunderConstants.INTERFACE_ATTRIBUTE_NAME;
        String serverAttributeName = ThunderConstants.SERVER_ATTRIBUTE_NAME;
        String refAttributeName = ThunderConstants.REF_ATTRIBUTE_NAME;

        String interfaze = element.getAttribute(interfaceAttributeName);
        String server = element.getAttribute(serverAttributeName);
        String ref = element.getAttribute(refAttributeName);
        
        if (StringUtils.isEmpty(interfaze)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, serviceElementName, interfaceAttributeName);
        }

        if (StringUtils.isEmpty(ref)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, serviceElementName, refAttributeName);
        }
        
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setInterface(interfaze);
        serviceEntity.setServer(server);
        
        builder.addPropertyValue(interfaceAttributeName, interfaze);
		builder.addPropertyValue(serviceElementName, new RuntimeBeanReference(ref));
        builder.addPropertyValue(createBeanName(ServiceEntity.class), serviceEntity);
        
        ServerExecutor serverExecutor = createServerExecutor();
        builder.addPropertyValue(createBeanName(ServerExecutor.class), serverExecutor);
        
        ServerExecutorAdapter serverExecutorAdapter = createServerExecutorAdapter();
        builder.addPropertyValue(createBeanName(ServerExecutorAdapter.class), serverExecutorAdapter);
        
        SecurityExecutor securityExecutor = createSecurityExecutor();
        builder.addPropertyValue(createBeanName(SecurityExecutor.class), securityExecutor);
    }
    
    protected ServerExecutor createServerExecutor() {
        ServerExecutor serverExecutor = executorContainer.getServerExecutor();
        if (serverExecutor == null) {
            ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
            String serverExecutorId = protocolEntity.getServerExecutorId();
            
            try {
                serverExecutor = createDelegate(serverExecutorId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ServerExecutor failed", e);
            }
            
            executorContainer.setServerExecutor(serverExecutor);
        }
        
        return serverExecutor;
    }
    
    protected ServerExecutorAdapter createServerExecutorAdapter() {
        ServerExecutorAdapter serverExecutorAdapter = executorContainer.getServerExecutorAdapter();
        if (serverExecutorAdapter == null) {
            String serverExecutorAdapterId = ThunderConstants.SERVER_EXECUTOR_ADAPTER_ID;
            try {
                serverExecutorAdapter = createDelegate(serverExecutorAdapterId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ServerExecutorAdapter failed", e);
            }
            
            executorContainer.setServerExecutorAdapter(serverExecutorAdapter);
        }
        
        return serverExecutorAdapter;
    }
    
    protected SecurityExecutor createSecurityExecutor() {
        SecurityExecutor securityExecutor = executorContainer.getSecurityExecutor();
        if (securityExecutor == null) {
            String securityExecutorId = ThunderConstants.SECURITY_EXECUTOR_ID;
            try {
                securityExecutor = createDelegate(securityExecutorId);
            } catch (Exception e) {
                throw new FrameworkException("Creat SecurityExecutor failed", e);
            }
            
            executorContainer.setSecurityExecutor(securityExecutor);
        }

        return securityExecutor;
    }
    
    @Override
    protected Class<?> getBeanClass(Element element) {
        return ServiceFactoryBean.class;
    }
}