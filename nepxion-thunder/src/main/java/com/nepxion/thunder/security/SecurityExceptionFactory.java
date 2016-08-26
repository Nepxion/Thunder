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

import com.nepxion.thunder.common.entity.ApplicationEntity;

public class SecurityExceptionFactory {
    public static final int SC_REACH_MAX_LIMITATION = 780213;
    public static final int SC_SECRET_KEYS_NOT_MATCHED = 780214;
    public static final int SC_VERSIONS_NOT_MATCHED = 780215;

    public static final String SC_REACH_MAX_LIMITATION_DESC = "Reach max limitation";
    public static final String SC_SECRET_KEYS_NOT_MATCHED_DESC = "Secret keys can't match";
    public static final String SC_VERSIONS_NOT_MATCHED_DESC = "Versions can't match";
    
    public static final String HTTP_RESPONSE_CODE = "Server returned HTTP response code: ";
    
    public static SecurityException createReachMaxLimitationException(String interfaze, ApplicationEntity applicationEntity) {
        return new SecurityException(SC_REACH_MAX_LIMITATION_DESC + ", interface=" + interfaze + ", host=" + applicationEntity.getHost() + ", port=" + applicationEntity.getPort());
    }
    
    public static SecurityException createReachMaxLimitationException(String message, String interfaze, ApplicationEntity applicationEntity) {
        if (message.indexOf(HTTP_RESPONSE_CODE + SC_REACH_MAX_LIMITATION) > -1) {
            return createReachMaxLimitationException(interfaze, applicationEntity);
        }
        
        return null;
    }
    
    public static SecurityException createSecretKeysNotMatchedException(String interfaze) {
        return new SecurityException(SC_SECRET_KEYS_NOT_MATCHED_DESC + ", interface=" + interfaze);
    }
    
    public static SecurityException createSecretKeysNotMatchedException(String message, String interfaze) {
        if (message.indexOf(HTTP_RESPONSE_CODE + SC_SECRET_KEYS_NOT_MATCHED) > -1) {
            return createSecretKeysNotMatchedException(interfaze);
        }
        
        return null;
    }
    
    public static SecurityException createVersionsNotMatchedException(String interfaze) {
        return new SecurityException(SC_VERSIONS_NOT_MATCHED_DESC + ", interface=" + interfaze);
    }
    
    public static SecurityException createVersionsNotMatchedException(String message, String interfaze) {
        if (message.indexOf(HTTP_RESPONSE_CODE + SC_VERSIONS_NOT_MATCHED) > -1) {
            return createVersionsNotMatchedException(interfaze);
        }
        
        return null;
    }
    
    public static SecurityException createException(String interfaze, ApplicationEntity applicationEntity, Exception e) {
        String message = e.getCause().getMessage();
        
        SecurityException reachMaxLimitationException = createReachMaxLimitationException(message, interfaze, applicationEntity);
        if (reachMaxLimitationException != null) {
            return reachMaxLimitationException;
        }
        
        SecurityException secretKeysNotMatchedException = createSecretKeysNotMatchedException(message, interfaze);
        if (secretKeysNotMatchedException != null) {
            return secretKeysNotMatchedException;
        }
        
        SecurityException versionsNotMatchedException = createVersionsNotMatchedException(message, interfaze);
        if (versionsNotMatchedException != null) {
            return versionsNotMatchedException;
        }
        
        return null;
    }
}