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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.MethodEntity;
import com.nepxion.thunder.common.entity.MethodKey;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ReferenceEntity;
import com.nepxion.thunder.framework.bean.ReferenceFactoryBean;
import com.nepxion.thunder.framework.exception.FrameworkException;
import com.nepxion.thunder.framework.exception.FrameworkExceptionFactory;
import com.nepxion.thunder.protocol.ClientExecutor;
import com.nepxion.thunder.protocol.ClientExecutorAdapter;
import com.nepxion.thunder.protocol.ClientInterceptorAdapter;

@SuppressWarnings("all")
public class ReferenceBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {

    public ReferenceBeanDefinitionParser(ThunderDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        String namespaceElementName = properties.getString(ThunderConstants.NAMESPACE_ELEMENT_NAME);
        String referenceElementName = ThunderConstants.REFERENCE_ELEMENT_NAME;
        String methodElementName = ThunderConstants.METHOD_ELEMENT_NAME;
        String interfaceAttributeName = ThunderConstants.INTERFACE_ATTRIBUTE_NAME;
        String serverAttributeName = ThunderConstants.SERVER_ATTRIBUTE_NAME;
        
        String interfaze = element.getAttribute(interfaceAttributeName);
        String server = element.getAttribute(serverAttributeName);
        
        if (StringUtils.isEmpty(interfaze)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, referenceElementName, interfaceAttributeName);
        }
        
        MethodEntity methodEntity = new MethodEntity();
        MethodBeanDefinitionParser methodBeanDefinitionParser = new MethodBeanDefinitionParser(delegate, methodEntity);
        methodBeanDefinitionParser.parseMethod(element, null, null);

        Map methodMap = parseMethodElements(element, parserContext, builder);
        
        ReferenceEntity referenceEntity = new ReferenceEntity();
        referenceEntity.setInterface(interfaze);
        referenceEntity.setServer(server);

        builder.addPropertyValue(interfaceAttributeName, interfaze);
        builder.addPropertyValue(createBeanName(MethodEntity.class), methodEntity);
        builder.addPropertyValue(methodElementName, methodMap);
        builder.addPropertyValue(createBeanName(ReferenceEntity.class), referenceEntity);
        
        ClientExecutor clientExecutor = createClientExecutor();
        builder.addPropertyValue(createBeanName(ClientExecutor.class), clientExecutor);
        
        ClientExecutorAdapter clientExecutorAdapter = createClientExecutorAdapter();
        builder.addPropertyValue(createBeanName(ClientExecutorAdapter.class), clientExecutorAdapter);
        
        ClientInterceptorAdapter clientInterceptorAdapter = createClientInterceptorAdapter();
        builder.addPropertyValue(createBeanName(ClientInterceptorAdapter.class), clientInterceptorAdapter);
    }

    private Map parseMethodElements(Element referenceElement, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String methodElementName = ThunderConstants.METHOD_ELEMENT_NAME;
        String methodAttributeName = ThunderConstants.METHOD_ATTRIBUTE_NAME;
        String parameterTypesAttributeName = ThunderConstants.PARAMETER_TYPES_ATTRIBUTE_NAME;

        List<Element> methodElements = DomUtils.getChildElementsByTagName(referenceElement, methodElementName);

        ManagedMap methodMap = new ManagedMap(methodElements.size());
        methodMap.setMergeEnabled(true);
        methodMap.setSource(parserContext.getReaderContext().extractSource(referenceElement));

        for (Element methodElement : methodElements) {
            String method = methodElement.getAttribute(methodAttributeName);
            String parameterTypes = methodElement.getAttribute(parameterTypesAttributeName);
            
            MethodKey methodKey = new MethodKey();
            methodKey.setMethod(method);
            methodKey.setParameterTypes(parameterTypes);
            if (methodMap.containsKey(methodKey)) {
                throw FrameworkExceptionFactory.createMethodDuplicatedException(methodElementName, methodKey);
            }
            
            methodMap.put(methodKey, parserContext.getDelegate().parseCustomElement(methodElement, builder.getRawBeanDefinition()));
        }

        return methodMap;
    }
    
    protected ClientExecutor createClientExecutor() {
        ClientExecutor clientExecutor = executorContainer.getClientExecutor();
        if (clientExecutor == null) {
            ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
            String clientExecutorId = protocolEntity.getClientExecutorId();

            try {
                clientExecutor = createDelegate(clientExecutorId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ClientExecutor failed", e);
            }
            
            executorContainer.setClientExecutor(clientExecutor);
        }
        
        return clientExecutor;
    }
    
    protected ClientExecutorAdapter createClientExecutorAdapter() {
        ClientExecutorAdapter clientExecutorAdapter = executorContainer.getClientExecutorAdapter();
        if (clientExecutorAdapter == null) {
            String clientExecutorAdapterId = ThunderConstants.CLIENT_EXECUTOR_ADAPTER_ID;
            try {
                clientExecutorAdapter = createDelegate(clientExecutorAdapterId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ClientExecutorAdapter failed", e);
            }
            
            executorContainer.setClientExecutorAdapter(clientExecutorAdapter);
        }
        
        return clientExecutorAdapter;
    }
    
    protected ClientInterceptorAdapter createClientInterceptorAdapter() {
        ClientInterceptorAdapter clientInterceptorAdapter = executorContainer.getClientInterceptorAdapter();
        if (clientInterceptorAdapter == null) {
            String clientInterceptorAdapterId = ThunderConstants.CLIENT_INTERCEPTOR_ADAPTER_ID;
            try {
                clientInterceptorAdapter = createDelegate(clientInterceptorAdapterId);
            } catch (Exception e) {
                throw new FrameworkException("Creat ClientInterceptorAdapter failed", e);
            }
            
            executorContainer.setClientInterceptorAdapter(clientInterceptorAdapter);
        }
        
        return clientInterceptorAdapter;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ReferenceFactoryBean.class;
    }
}