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
import javax.jms.JMSException;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.MQPropertyEntity;
import com.nepxion.thunder.protocol.ClientExecutorAdapter;
import com.nepxion.thunder.protocol.ProtocolResponse;

public class MQClientHandler extends MQConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(MQClientHandler.class);

    private MQBytesMessageConverter mqMessageConverter = new MQBytesMessageConverter();
    
    private boolean transportLogPrint;
    
    public MQClientHandler(MQPropertyEntity mqPropertyEntity) {
        try {
            transportLogPrint = mqPropertyEntity.getBoolean(ThunderConstants.TRANSPORT_LOG_PRINT_ATTRIBUTE_NAME);
        } catch (Exception e) {
            LOG.error("Get properties failed", e);
        }
    }
    
    @Override
    public void onMessage(final BytesMessage message, final Session session) throws JMSException {
        final ProtocolResponse response = (ProtocolResponse) mqMessageConverter.fromMessage(message);
        // 如果消费线程里面放子线程：
        // 好处是可以加快消费速度，减少MQ消息堆积，
        // 坏处是如果子线程消费速度跟不上，会造成消息在内存中的堆积，一旦服务器挂掉，消息全部丢失
        // 最终还是去掉子线程
        String interfaze = response.getInterface();
        /*ThreadPoolFactory.createThreadPoolClientExecutor(interfaze).submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {*/
                try {
                    String responseSelector = MQSelectorUtil.getResponseSelector(message);
                    // String requestSelector = MQSelectorUtil.getRequestSelector(message);
                    
                    if (transportLogPrint) {
                        LOG.info("Response from server={}, service={}", responseSelector, interfaze);
                    }
                    
                    ClientExecutorAdapter clientExecutorAdapter = executorContainer.getClientExecutorAdapter();
                    clientExecutorAdapter.handle(response);
                } catch (Exception e) {
                    LOG.error("Consume request failed", e);
                }

                /*return null;
            }
        });*/
    }
}