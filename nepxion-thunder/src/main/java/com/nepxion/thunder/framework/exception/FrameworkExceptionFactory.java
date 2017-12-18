package com.nepxion.thunder.framework.exception;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;

import com.nepxion.thunder.common.entity.MethodKey;

public class FrameworkExceptionFactory {
    // 必要属性不可缺失，否则抛异常
    public static FrameworkException createAttributeMissingException(String namespaceName, String elementName, String attributeName) {
        return new FrameworkException("<" + namespaceName + ":" + elementName + "> attribute '" + attributeName + "' is missing");
    }

    // 必要属性值不可缺失，否则抛异常
    public static FrameworkException createValueNullException(String namespaceName, String elementName, String attributeName) {
        return new FrameworkException("<" + namespaceName + ":" + elementName + "> attribute '" + attributeName + "' value is null");
    }

    // 必要属性必须为指定值，否则抛异常
    public static FrameworkException createValueLimitedException(String namespaceName, String elementName, String attributeName, String attributeValue) {
        return new FrameworkException("<" + namespaceName + ":" + elementName + "> attribute '" + attributeName + "' value is only limited to " + attributeValue);
    }

    // 某种类型方法在某种调用方式下不允许某种属性存在，否则抛异常
    public static FrameworkException createMethodAttributeForbiddenException(String invokeMode, String methodName, String attributeName) {
        return new FrameworkException("Attribute '" + attributeName + "' in " + invokeMode + (StringUtils.isNotEmpty(methodName) ? " method [" + methodName + "]" : " mode") + " is forbidden");
    }

    // 某种类型方法在某种调用方式下不允许某种属性存在，否则抛异常
    public static FrameworkException createMethodAttributeForbiddenException(String invokeMode, String methodName, String attributeName, String situationName) {
        return new FrameworkException("Attribute '" + attributeName + "' in " + invokeMode + (StringUtils.isNotEmpty(methodName) ? " method [" + methodName + "]" : " mode") + " with " + situationName + " is forbidden");
    }

    // 方法名未找到，抛异常
    public static FrameworkException createMethodNameNotFoundException(String namespaceName, MethodKey methodKey) {
        String method = methodKey.getMethod();

        return new FrameworkException("Not found method [" + method + "] in relevant interface, check <" + namespaceName + ":method method=\"" + method + "\" ...>  in Spring xml defination");
    }

    // 方法键未找到，抛异常
    public static FrameworkException createMethodKeyNotFoundException(String namespaceName, MethodKey methodKey) {
        String method = methodKey.getMethod();
        String parameterTypes = methodKey.getParameterTypes();

        return new FrameworkException("Not found method [" + method + "] with parameterTypes [" + parameterTypes + "] in relevant interface, check <" + namespaceName + ":method method=\"" + method + "\" parameterTypes=\"" + parameterTypes + "\" ...>  in Spring xml defination");
    }

    // 方法参数缺失，抛异常
    public static FrameworkException createMethodParameterTypesMissingException(String namespaceName, MethodKey methodKey) {
        String method = methodKey.getMethod();

        return new FrameworkException("ParameterTypes in method [" + method + "] is missing, specify parameterTypes for it in Spring xml defination");
    }

    // 方法重复定义，抛异常
    public static FrameworkException createMethodDuplicatedException(String namespaceName, MethodKey methodKey) {
        String method = methodKey.getMethod();
        String parameterTypes = methodKey.getParameterTypes();

        return new FrameworkException("Duplicated method [" + method + "] defination, check <" + namespaceName + ":method method=\"" + method + "\" parameterTypes=\"" + parameterTypes + "\" ...>  in Spring xml defination");
    }
}