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

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;

import com.nepxion.thunder.common.constant.ThunderConstants;

public class MQTemplate extends JmsTemplate {

    public MQTemplate() {
        super();
    }

    public MQTemplate(ConnectionFactory connectionFactory) {
        super();
    }

    @Override
    protected void doSend(Session session, Destination destination, MessageCreator messageCreator) throws JMSException {
        MessageProducer producer = null;
        try {
            Message message = messageCreator.createMessage(session);
            boolean async = message.getBooleanProperty(ThunderConstants.ASYNC_ATTRIBUTE_NAME);
            long timeout = message.getLongProperty(ThunderConstants.TIMEOUT_ATTRIBUTE_NAME);
            
            producer = createProducer(session, destination);
            // DeliveryMode.PERSISTENT:持久化模式，消息在硬盘堆积模式
            // DeliveryMode.NON_PERSISTENT:非持久化模式，消息在内存堆积模式
            if (async) {
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            } else {
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            }
            producer.setTimeToLive(timeout);

            doSend(producer, message);

            if (session.getTransacted() && isSessionLocallyTransacted(session)) {
                JmsUtils.commitIfNecessary(session);
            }
        } finally {
            if (producer != null) {
                JmsUtils.closeMessageProducer(producer);
            }
        }
    }
}