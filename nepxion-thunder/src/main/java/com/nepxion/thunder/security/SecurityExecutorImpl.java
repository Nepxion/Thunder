package com.nepxion.thunder.security;

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
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.nepxion.thunder.common.config.ApplicationConfig;
import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.config.ServiceConfig;
import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ServiceEntity;
import com.nepxion.thunder.protocol.ProtocolRequest;
import com.nepxion.thunder.protocol.ProtocolResponse;

public class SecurityExecutorImpl extends ThunderDelegateImpl implements SecurityExecutor {

    @Override
    public boolean execute(ProtocolRequest request, ProtocolResponse response) {
        String interfaze = request.getInterface();

        Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
        ServiceEntity serviceEntity = serviceEntityMap.get(interfaze);

        ApplicationConfig applicationConfig = cacheContainer.getApplicationConfig();
        if (serviceEntity != null && applicationConfig != null) {
            int frequency = applicationConfig.getFrequency();
            AtomicLong defaultToken = serviceEntity.getDefaultToken();
            AtomicLong token = serviceEntity.getToken();
            // 限流，frequency > 0 && defaultToken > 0代表接口限流是启用的
            if (frequency > 0 && defaultToken.get() > 0 && token.addAndGet(-1) < 0) {
                ApplicationEntity applicationEntity = cacheContainer.getApplicationEntity();

                response.setResult(null);
                response.setException(SecurityExceptionFactory.createReachMaxLimitationException(interfaze, applicationEntity));

                return false;
            }
        }

        Map<String, ServiceConfig> serviceConfigMap = cacheContainer.getServiceConfigMap();
        ServiceConfig serviceConfig = serviceConfigMap.get(interfaze);
        ReferenceConfig referenceConfig = request.getReferenceConfig();
        if (serviceConfig != null && referenceConfig != null) {
            // 服务端和客户端的密钥匹配检查，不匹配不能访问，默认空字符串
            String serviceSecretKey = serviceConfig.getSecretKey();
            String referenceSecretKey = referenceConfig.getSecretKey();
            if (!StringUtils.equals(serviceSecretKey, referenceSecretKey)) {
                response.setResult(null);
                response.setException(SecurityExceptionFactory.createSecretKeysNotMatchedException(interfaze));

                return false;
            }

            // 服务端和客户端的版本匹配检查，不匹配不能访问，默认版本为0
            int serviceVersion = serviceConfig.getVersion();
            int referenceVersion = referenceConfig.getVersion();
            if (serviceVersion != referenceVersion) {
                response.setResult(null);
                response.setException(SecurityExceptionFactory.createVersionsNotMatchedException(interfaze));

                return false;
            }
        }

        return true;
    }
    
    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String interfaze = request.getHeader(ThunderConstants.INTERFACE_ATTRIBUTE_NAME);
        String referenceSecretKey = request.getHeader(ThunderConstants.SECRET_KEY_ATTRIBUTE_NAME);
        int referenceVersion = Integer.parseInt(request.getHeader(ThunderConstants.VERSION_ATTRIBUTE_NAME));
        
        Map<String, ServiceEntity> serviceEntityMap = cacheContainer.getServiceEntityMap();
        ServiceEntity serviceEntity = serviceEntityMap.get(interfaze);
        
        ApplicationConfig applicationConfig = cacheContainer.getApplicationConfig();
        if (applicationConfig != null) {
            int frequency = applicationConfig.getFrequency();
            AtomicLong defaultToken = serviceEntity.getDefaultToken();
            AtomicLong token = serviceEntity.getToken();
            // 限流，frequency > 0 && defaultToken > 0代表接口限流是启用的
            if (frequency > 0 && defaultToken.get() > 0 && token.addAndGet(-1) < 0) {
                response.sendError(SecurityExceptionFactory.SC_REACH_MAX_LIMITATION, SecurityExceptionFactory.SC_REACH_MAX_LIMITATION_DESC);
                
                return false;
            }
        }

        Map<String, ServiceConfig> serviceConfigMap = cacheContainer.getServiceConfigMap();
        ServiceConfig serviceConfig = serviceConfigMap.get(interfaze);
        if (serviceConfig != null) {
            // 服务端和客户端的密钥匹配检查，不匹配不能访问，默认空字符串
            String serviceSecretKey = serviceConfig.getSecretKey();
            if (!StringUtils.equals(serviceSecretKey, referenceSecretKey)) {
                response.sendError(SecurityExceptionFactory.SC_SECRET_KEYS_NOT_MATCHED, SecurityExceptionFactory.SC_SECRET_KEYS_NOT_MATCHED_DESC);

                return false;
            }

            // 服务端和客户端的版本匹配检查，不匹配不能访问，默认版本为0
            int serviceVersion = serviceConfig.getVersion();
            if (serviceVersion != referenceVersion) {
                response.sendError(SecurityExceptionFactory.SC_VERSIONS_NOT_MATCHED, SecurityExceptionFactory.SC_VERSIONS_NOT_MATCHED_DESC);

                return false;
            }
        }
        
        return true;
    }
}