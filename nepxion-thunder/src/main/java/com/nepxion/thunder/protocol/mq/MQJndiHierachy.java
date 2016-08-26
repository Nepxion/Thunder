package com.nepxion.thunder.protocol.mq;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Properties;

import javax.jms.ConnectionFactory;

import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

import com.nepxion.thunder.common.constant.ThunderConstants;

public class MQJndiHierachy extends MQHierachy {

    @Override
    public void initialize() throws Exception {
        super.initialize();
        
        String jndiInitialContextFactoryClass = mqPropertyEntity.getMQEntity().getJndiInitialContextFactoryClass();
        String url = mqPropertyEntity.getString(ThunderConstants.MQ_URL_ATTRIBUTE_NAME);
        String userName = mqPropertyEntity.getString(ThunderConstants.MQ_USER_NAME_ATTRIBUTE_NAME);
        String password = mqPropertyEntity.getString(ThunderConstants.MQ_PASSWORD_ATTRIBUTE_NAME);
        String jndiName = mqPropertyEntity.getString(ThunderConstants.MQ_JNDI_NAME_ATTRIBUTE_NAME);
        
        Properties environment = new Properties();
        environment.put("java.naming.factory.initial", jndiInitialContextFactoryClass);
        environment.put("java.naming.provider.url", url);
        environment.put("java.naming.security.principal", userName);
        environment.put("java.naming.security.credentials", password);

        JndiTemplate jndiTemplate = new JndiTemplate();
        jndiTemplate.setEnvironment(environment);

        JndiObjectFactoryBean targetConnectionFactory = new JndiObjectFactoryBean();
        targetConnectionFactory.setJndiTemplate(jndiTemplate);
        targetConnectionFactory.setJndiName(jndiName);
        targetConnectionFactory.setLookupOnStartup(true);
        targetConnectionFactory.afterPropertiesSet();
        
        setTargetConnectionFactory((ConnectionFactory) targetConnectionFactory.getObject());
        
        afterPropertiesSet();
    }
}