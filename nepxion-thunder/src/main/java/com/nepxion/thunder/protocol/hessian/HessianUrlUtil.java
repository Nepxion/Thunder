package com.nepxion.thunder.protocol.hessian;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.container.CacheContainer;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.entity.WebServiceEntity;

public class HessianUrlUtil {

    // 转化为URL
    // 例如http://192.168.0.1:8080/thunder/hessian/com.nepxion.thunder.xxxService，
    // 带interface全路径是为了客户端把interface传送获取，便于解析密钥等缓存
    public static String toUrl(String interfaze, ApplicationEntity applicationEntity, CacheContainer cacheContainer) {
        ProtocolEntity protocolEntity = cacheContainer.getProtocolEntity();
        WebServiceEntity webServiceEntity = cacheContainer.getWebServiceEntity();
        String host = applicationEntity.getHost();
        int port = applicationEntity.getPort();
        ProtocolType type = protocolEntity.getType();
        String path = webServiceEntity.getPath();

        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(host);
        builder.append(":");
        builder.append(port);
        builder.append("/");
        builder.append(path);
        builder.append("/");
        builder.append(type);
        builder.append("/");
        builder.append(interfaze);

        return builder.toString();
    }

    public static String toUrl(Class<?> interfaze, ApplicationEntity applicationEntity, CacheContainer cacheContainer) {
        String interfaceName = interfaze.getName();

        return toUrl(interfaceName, applicationEntity, cacheContainer);
    }
    
    // 萃取Interface
    // 例如，http://192.168.0.1:8080/thunder/hessian/com.nepxion.thunder.xxxService，
    // 萃取为com.nepxion.thunder.xxxService
    public static String extractInterface(String url) {        
        return url.substring(url.lastIndexOf("/") + 1);
    }
}