package com.nepxion.thunder.framework.property;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Iterator;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.nepxion.thunder.common.util.MathsUtil;

public class ThunderPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private Properties properties;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties) throws BeansException {
        super.processProperties(beanFactoryToProcess, properties);
        
        this.properties = properties;
        for (Iterator<?> iterator = properties.keySet().iterator(); iterator.hasNext();) {
            Object key = iterator.next();
            String value = (String) properties.get(key);
            Long result = MathsUtil.calculate(value);
            if (result != null) {
                properties.put(key, result);
            }
        }
    }
    
    public Properties getProperties() {
        return properties;
    }
}