package com.nepxion.thunder.common.util;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.nepxion.thunder.common.entity.MethodEntity;
import com.nepxion.thunder.common.entity.MethodKey;
import com.nepxion.thunder.common.invocation.MethodInvocation;

public class ClassUtil {
    public static String convertParameter(String methodName, Class<?>[] parameterTypes) {
        return convertParameter(methodName, convertParameter(parameterTypes));
    }

    public static String convertParameter(String methodName, String parameterTypes) {
        StringBuilder builder = new StringBuilder();
        builder.append(methodName);
        builder.append("(");
        builder.append(parameterTypes);
        builder.append(")");

        return builder.toString();
    }

    public static String convertParameter(Class<?>[] parameterTypes) {
        if (ArrayUtils.isEmpty(parameterTypes)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Class<?> clazz : parameterTypes) {
            builder.append("," + clazz.getName());
        }

        String parameter = builder.toString().trim();
        if (parameter.length() > 0) {
            return parameter.substring(1);
        }

        return "";
    }
    
    public static List<String> convertMethodList(Class<?> clazz) {
        List<String> methodList = new ArrayList<String>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String methodDescription = convertParameter(method.getName(), method.getParameterTypes());
            methodList.add(methodDescription);
        }
        
        return methodList;
    }
    
    public static Map<MethodKey, MethodEntity> convertMethodMap(Class<?> clazz, MethodEntity entity) {
        Map<MethodKey, MethodEntity> methodMap = new HashMap<MethodKey, MethodEntity>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            String parameterTypes = convertParameter(method.getParameterTypes());
            
            MethodKey methodKey = new MethodKey();
            methodKey.setMethod(methodName);
            methodKey.setParameterTypes(parameterTypes);
            
            MethodEntity methodEntity = new MethodEntity();
            methodEntity.setMethod(methodName);
            methodEntity.setParameterTypes(parameterTypes);
            methodEntity.setTraceIdIndex(entity.getTraceIdIndex());
            methodEntity.setAsync(entity.isAsync());
            methodEntity.setTimeout(entity.getTimeout());
            methodEntity.setBroadcast(entity.isBroadcast());
            methodEntity.setCallbackType(entity.getCallbackType());
            
            methodMap.put(methodKey, methodEntity);
        }
        
        return methodMap;
    }
    
    // 根据接口类中Method获取用户定义的MethodKey
    public static MethodKey getMethodKey(Map<MethodKey, MethodEntity> methodMap, String method) {
        for (Map.Entry<MethodKey, MethodEntity> entry : methodMap.entrySet()) {
            MethodKey key = entry.getKey();
            if (StringUtils.equals(key.getMethod(), method)) {
                return key;
            }
        }

        return null;
    }
    
    // 根据接口类中Method判断用户定义的MethodKey是否重复定义(前提，用户设置ParameterTypes为空)
    public static boolean duplicatedMethodKey(Map<MethodKey, MethodEntity> methodMap, String method) {
        int count = 0;
        for (Map.Entry<MethodKey, MethodEntity> entry : methodMap.entrySet()) {
            MethodKey key = entry.getKey();
            if (StringUtils.equals(key.getMethod(), method)) {
                count++;
            }
        }

        return count > 1;
    }
    
    // 根据接口类中Method和ParameterTypes判断用户定义的MethodKey是否存在
    public static boolean hasMethodKey(Map<MethodKey, MethodEntity> methodMap, MethodKey methodKey) {
        for (Map.Entry<MethodKey, MethodEntity> entry : methodMap.entrySet()) {
            MethodKey key = entry.getKey();
            if (key.equals(methodKey)) {
                return true;
            }
        }

        return false;
    }

    // Example:convert Class[com.nepxion.thunder.service.UserService] to "userService"
    public static String convertBeanName(Class<?> clazz) {
        String className = clazz.getSimpleName();

        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    // Example:convert "com.nepxion.thunder.service.UserService" to "userService"
    //         or "UserService" to "userService"
    public static String convertBeanName(String className) {
        if (className.lastIndexOf(".") > -1) {
            className = className.substring(className.lastIndexOf(".") + 1);
        }

        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(String className) throws Exception {
        return (T) Class.forName(className).newInstance();
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T createInstance(String className, Object... args) throws Exception {
        Class<?> clazz = Class.forName(className);
        
        Class<?>[] argsClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argsClasses[i] = args[i].getClass();
        }
        
        Constructor<?> constructor = clazz.getConstructor(argsClasses);
        
        return (T) constructor.newInstance(args);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T createConstructor(String className, Class<?>... parameterTypes) throws Exception {        
        return (T) Class.forName(className).getConstructor(parameterTypes);
    }
    
    public static Object invoke(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Exception {
        MethodInvocation invocation = new MethodInvocation();
        invocation.setMethodName(methodName);
        invocation.setParameterTypes(parameterTypes);
        invocation.setParameters(parameters);
        
        return invocation.invoke(object); 
    }
}