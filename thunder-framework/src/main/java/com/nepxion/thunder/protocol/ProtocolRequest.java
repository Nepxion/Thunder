package com.nepxion.thunder.protocol;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.common.config.ReferenceConfig;
import com.nepxion.thunder.common.util.RandomUtil;

public class ProtocolRequest extends ProtocolMessage {
    private static final long serialVersionUID = 3399899702039468806L;

    private ReferenceConfig referenceConfig;

    public ProtocolRequest() {
        String messageId = RandomUtil.uuidRandom();

        super.setMessageId(messageId);
    }

    // Request的MessageId自动产生，不需要设置
    @Override
    public void setMessageId(String messageId) {

    }

    public ReferenceConfig getReferenceConfig() {
        return referenceConfig;
    }

    public void setReferenceConfig(ReferenceConfig referenceConfig) {
        this.referenceConfig = referenceConfig;
    }
}