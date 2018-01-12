package com.nepxion.thunder.framework.parser;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.delegate.ThunderDelegate;
import com.nepxion.thunder.common.entity.ProtocolEntity;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.framework.bean.ProtocolFactoryBean;

@SuppressWarnings("all")
public class ProtocolBeanDefinitionParser extends AbstractExtensionBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolBeanDefinitionParser.class);

    public ProtocolBeanDefinitionParser(ThunderDelegate delegate) {
        super(delegate);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        super.doParse(element, parserContext, builder);

        String typeAttributeName = ThunderConstant.TYPE_ATTRIBUTE_NAME;

        String type = element.getAttribute(typeAttributeName);
        ProtocolType protocolType = null;
        if (StringUtils.isNotEmpty(type)) {
            protocolType = ProtocolType.fromString(type);
        } else {
            protocolType = ProtocolType.NETTY;
        }

        LOG.info("Protocol type is {}", protocolType);

        ProtocolEntity protocolEntity = new ProtocolEntity();
        protocolEntity.setType(protocolType);

        cacheContainer.setProtocolEntity(protocolEntity);
        builder.addPropertyValue(createBeanName(ProtocolEntity.class), protocolEntity);
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ProtocolFactoryBean.class;
    }
}