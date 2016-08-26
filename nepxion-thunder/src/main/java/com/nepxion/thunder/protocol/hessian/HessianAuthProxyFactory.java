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

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.client.HessianURLConnectionFactory;
import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.constant.ThunderConstants;

public class HessianAuthProxyFactory extends HessianProxyFactory {
    private Map<String, ReferenceConfig> referenceConfigMap;

    public HessianAuthProxyFactory() {
        super();
    }

    public HessianAuthProxyFactory(ClassLoader loader) {
        super(loader);
    }

    @Override
    protected HessianConnectionFactory createHessianConnectionFactory() {
        return new HessianURLConnectionFactory() {
            @Override
            public HessianConnection open(URL url) throws IOException {
                HessianConnection connection = super.open(url);
                
                String interfaze = HessianUrlUtil.extractInterface(url.toString());
                ReferenceConfig referenceConfig = referenceConfigMap.get(interfaze);
                String secretKey = referenceConfig.getSecretKey();
                int version = referenceConfig.getVersion();
                
                connection.addHeader(ThunderConstants.INTERFACE_ATTRIBUTE_NAME, interfaze);
                connection.addHeader(ThunderConstants.SECRET_KEY_ATTRIBUTE_NAME, secretKey);
                connection.addHeader(ThunderConstants.VERSION_ATTRIBUTE_NAME, String.valueOf(version));

                return connection;
            }
        };
    }
    
    protected void setReferenceConfigMap(Map<String, ReferenceConfig> referenceConfigMap) {
        this.referenceConfigMap = referenceConfigMap;
    }
}