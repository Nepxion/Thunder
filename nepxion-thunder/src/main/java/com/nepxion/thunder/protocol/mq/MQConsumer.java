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

import javax.jms.BytesMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;

import com.nepxion.thunder.common.delegate.ThunderDelegateImpl;

public abstract class MQConsumer extends ThunderDelegateImpl implements SessionAwareMessageListener<BytesMessage> {

}