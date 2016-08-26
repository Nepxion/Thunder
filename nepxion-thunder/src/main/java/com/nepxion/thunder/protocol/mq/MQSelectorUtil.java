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

import javax.jms.JMSException;
import javax.jms.Message;

import com.nepxion.thunder.common.entity.ApplicationEntity;
import com.nepxion.thunder.common.entity.SelectorType;

public class MQSelectorUtil {

    public static String getRequestSelector(Message message) throws JMSException {
        return message.getStringProperty(SelectorType.REQUEST_SELECTOR.toString());
    }

    public static void setRequestSelector(Message message, String selector) throws JMSException {
        message.setStringProperty(SelectorType.REQUEST_SELECTOR.toString(), selector);
    }

    public static void setRequestSelector(Message message, ApplicationEntity applicationEntity) throws JMSException {
        String selector = applicationEntity.toUrl();
        setRequestSelector(message, selector);
    }

    public static String getResponseSelector(Message message) throws JMSException {
        return message.getStringProperty(SelectorType.RESPONSE_SELECTOR.toString());
    }

    public static void setResponseSelector(Message message, String selector) throws JMSException {
        message.setStringProperty(SelectorType.RESPONSE_SELECTOR.toString(), selector);
    }

    public static void setResponseSelector(Message message, ApplicationEntity applicationEntity) throws JMSException {
        String selector = applicationEntity.toUrl();
        setResponseSelector(message, selector);
    }
}