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
import com.nepxion.thunder.common.entity.CallbackType;
import com.nepxion.thunder.common.entity.MethodEntity;
import com.nepxion.thunder.framework.exception.FrameworkExceptionFactory;

public class MethodBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private MethodEntity methodEntity;

    public MethodBeanDefinitionParser(ThunderDelegate delegate) {
        super(delegate);
    }
    
    public MethodBeanDefinitionParser(ThunderDelegate delegate, MethodEntity methodEntity) {
        super(delegate);
        
        this.methodEntity = methodEntity;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        parseMethod(element, parserContext, builder);
    }
    
    protected void parseMethod(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String namespaceElementName = properties.getString(ThunderConstants.NAMESPACE_ELEMENT_NAME);
        String referenceElementName = ThunderConstants.REFERENCE_ELEMENT_NAME;
        String methodAttributeName = ThunderConstants.METHOD_ATTRIBUTE_NAME;
        String parameterTypesAttributeName = ThunderConstants.PARAMETER_TYPES_ATTRIBUTE_NAME;
        String traceIdIndexAttributeName = ThunderConstants.TRACE_ID_INDEX_ATTRIBUTE_NAME;
        String asyncAttributeName = ThunderConstants.ASYNC_ATTRIBUTE_NAME;
        String syncAttributeName = ThunderConstants.SYNC_ATTRIBUTE_NAME;
        String timeoutAttributeName = ThunderConstants.TIMEOUT_ATTRIBUTE_NAME;
        String asyncTimeoutAttributeName = ThunderConstants.ASYNC_TIMEOUT_ATTRIBUTE_NAME;
        String syncTimeoutAttributeName = ThunderConstants.SYNC_TIMEOUT_ATTRIBUTE_NAME;
        String broadcastAttributeName = ThunderConstants.BROADCAST_ATTRIBUTE_NAME;
        String callbackAttributeName = ThunderConstants.CALLBACK_ATTRIBUTE_NAME;
        String callbackTypeAttributeName = ThunderConstants.CALLBACK_TYPE_ATTRIBUTE_NAME;
        
        String method = element.getAttribute(methodAttributeName);
        String parameterTypes = element.getAttribute(parameterTypesAttributeName);
        String traceIdIndex = element.getAttribute(traceIdIndexAttributeName);
        String async = element.getAttribute(asyncAttributeName);
        String timeout = element.getAttribute(timeoutAttributeName);
        String broadcast = element.getAttribute(broadcastAttributeName);
        String callback = element.getAttribute(callbackAttributeName);
        
        if (StringUtils.isEmpty(async)) {
            async = properties.getString(asyncAttributeName);
        }
        
        if (StringUtils.isEmpty(broadcast)) {
            broadcast = properties.getString(broadcastAttributeName);
        }
        
        if (StringUtils.isEmpty(timeout) && !Boolean.valueOf(broadcast)) {
            if (Boolean.valueOf(async)) {
                timeout = properties.getString(asyncTimeoutAttributeName);
            } else {
                timeout = properties.getString(syncTimeoutAttributeName);
            }
        }

        if (Boolean.valueOf(async)) {
            if (Boolean.valueOf(broadcast)) {            
                if (StringUtils.isNotEmpty(callback)) {
                    throw FrameworkExceptionFactory.createMethodAttributeForbiddenException(asyncAttributeName, method, callbackAttributeName, broadcastAttributeName);
                }
                if (StringUtils.isNotEmpty(timeout)) {
                    throw FrameworkExceptionFactory.createMethodAttributeForbiddenException(asyncAttributeName, method, timeoutAttributeName, broadcastAttributeName);
                }
            }
        } else {
            if (Boolean.valueOf(broadcast)) {
                throw FrameworkExceptionFactory.createMethodAttributeForbiddenException(syncAttributeName, method, broadcastAttributeName);
            }
            if (StringUtils.isNotEmpty(callback)) {
                throw FrameworkExceptionFactory.createMethodAttributeForbiddenException(syncAttributeName, method, callbackAttributeName);
            }
        }
        
        if (builder != null) {
            builder.addPropertyValue(methodAttributeName, method);
        }

        if (StringUtils.isNotEmpty(parameterTypes)) {
            if (builder != null) {
                builder.addPropertyValue(parameterTypesAttributeName, parameterTypes);
            }
        }

        if (StringUtils.isNotEmpty(traceIdIndex)) {
            if (builder != null) {
                builder.addPropertyValue(traceIdIndexAttributeName, Integer.parseInt(traceIdIndex));
            } else {
                methodEntity.setTraceIdIndex(Integer.parseInt(traceIdIndex));
            }
        }

        if (builder != null) {
            builder.addPropertyValue(asyncAttributeName, Boolean.valueOf(async));
        } else {
            methodEntity.setAsync(Boolean.valueOf(async));
        }

        if (StringUtils.isNotEmpty(timeout)) {
            if (builder != null) {
                builder.addPropertyValue(timeoutAttributeName, Long.parseLong(timeout));
            } else {
                methodEntity.setTimeout(Long.parseLong(timeout));
            }
        }

        if (builder != null) {
            builder.addPropertyValue(broadcastAttributeName, Boolean.valueOf(broadcast));
        } else {
            methodEntity.setBroadcast(Boolean.valueOf(broadcast));
        }
        
        if (StringUtils.isNotEmpty(callback)) {
            // "promise"是关键字，如果把callback所对应bean id命名成"promise"，则会被认为无效，仍将Promise方式进行调用
            if (builder != null) {
                CallbackType callbackType = null;
                if (StringUtils.equalsIgnoreCase(callback, CallbackType.PROMISE.toString())) {
                    callbackType = CallbackType.PROMISE;
                } else {
                    callbackType = CallbackType.CALLBACK;
                    builder.addPropertyValue(callbackAttributeName, new RuntimeBeanReference(callback));
                } 
                
                builder.addPropertyValue(callbackTypeAttributeName, callbackType);
            } else {
                // 在reference节点上，callback只能定义为"promise"
                if (StringUtils.equalsIgnoreCase(callback, CallbackType.PROMISE.toString())) {
                    CallbackType callbackType = CallbackType.PROMISE;
                    methodEntity.setCallbackType(callbackType);
                } else {
                    throw FrameworkExceptionFactory.createValueLimitedException(namespaceElementName, referenceElementName, callbackAttributeName, CallbackType.PROMISE.toString());
                }
            }
        }
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MethodEntity.class;
    }
}