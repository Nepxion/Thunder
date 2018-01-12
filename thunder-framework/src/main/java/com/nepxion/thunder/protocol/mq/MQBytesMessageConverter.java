package com.nepxion.thunder.protocol.mq;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import com.nepxion.thunder.serialization.SerializerExecutor;

public class MQBytesMessageConverter implements MessageConverter {
    private static final Logger LOG = LoggerFactory.getLogger(MQBytesMessageConverter.class);

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (!(message instanceof BytesMessage)) {
            LOG.error("Message should be instance of BytesMessage");

            return null;
        }

        BytesMessage bytesMessage = (BytesMessage) message;
        int length = (int) bytesMessage.getBodyLength();
        byte[] bytes = new byte[length];
        bytesMessage.readBytes(bytes);

        return SerializerExecutor.deserialize(bytes);
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        if (!(object instanceof Serializable)) {
            LOG.error("Object should be instance of Serializable");

            return null;
        }

        Serializable serializable = (Serializable) object;

        byte[] bytes = SerializerExecutor.serialize(serializable);
        BytesMessage bytesMessage = session.createBytesMessage();
        bytesMessage.writeBytes(bytes);

        return bytesMessage;
    }
}