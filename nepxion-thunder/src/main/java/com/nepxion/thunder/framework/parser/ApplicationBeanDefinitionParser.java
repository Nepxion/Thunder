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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.util.HostUtil;
import com.nepxion.thunder.framework.bean.ApplicationFactoryBean;
import com.nepxion.thunder.framework.exception.FrameworkException;
import com.nepxion.thunder.framework.exception.FrameworkExceptionFactory;

@SuppressWarnings("all")
public class ApplicationBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationBeanDefinitionParser.class);

    public ApplicationBeanDefinitionParser(ThunderDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        String namespaceElementName = properties.getString(ThunderConstants.NAMESPACE_ELEMENT_NAME);
        String applicationElementName = ThunderConstants.APPLICATION_ELEMENT_NAME;
        String applicationAttributeName = ThunderConstants.APPLICATION_ATTRIBUTE_NAME;
        String groupAttributeName = ThunderConstants.GROUP_ATTRIBUTE_NAME;
        String clusterAttributeName = ThunderConstants.CLUSTER_ATTRIBUTE_NAME;
        String hostAttributeName = ThunderConstants.HOST_ATTRIBUTE_NAME;
        String portAttributeName = ThunderConstants.PORT_ATTRIBUTE_NAME;
        String hostParameterName = ThunderConstants.HOST_PARAMETER_NAME;
        String portParameterName = ThunderConstants.PORT_PARAMETER_NAME;

        String application = element.getAttribute(applicationAttributeName);
        if (StringUtils.isEmpty(application)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, applicationElementName, applicationAttributeName);
        }

        LOG.info("Application is {}", application);
        
        String group = element.getAttribute(groupAttributeName);
        if (StringUtils.isEmpty(group)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, applicationElementName, groupAttributeName);
        }
        
        LOG.info("Group is {}", group);
        
        String cluster = element.getAttribute(clusterAttributeName);
        if (StringUtils.isEmpty(cluster)) {
            throw FrameworkExceptionFactory.createAttributeMissingException(namespaceElementName, applicationElementName, clusterAttributeName);
        }
        
        LOG.info("Cluster is {}", cluster);

        // 通过-DThunderHost获取值
        String host = System.getProperty(hostParameterName);
        if (StringUtils.isEmpty(host)) {
            host = element.getAttribute(hostAttributeName);
            if (StringUtils.isEmpty(host)) {
                host = HostUtil.getLocalhost();
            }
        }
        
        LOG.info("Host is {}", host);

        // 通过-DThunderPort获取值
        String port = System.getProperty(portParameterName);
        if (StringUtils.isEmpty(port)) {
            port = element.getAttribute(portAttributeName);
            if (StringUtils.isEmpty(port)) {
                throw new FrameworkException("Port value is null");
            }
        }
        
        LOG.info("Port is {}", port);
        
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApplication(application);
        applicationEntity.setGroup(group);
        applicationEntity.setCluster(cluster);
        applicationEntity.setHost(host);
        applicationEntity.setPort(Integer.parseInt(port));

        cacheContainer.setApplicationEntity(applicationEntity);
        builder.addPropertyValue(createBeanName(ApplicationEntity.class), applicationEntity);
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ApplicationFactoryBean.class;
    }
}