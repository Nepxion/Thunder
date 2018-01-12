package com.nepxion.thunder.test.trace;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Arrays;

import com.nepxion.thunder.common.entity.ActionType;
import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.util.ExceptionUtil;
import com.nepxion.thunder.event.protocol.ProtocolEvent;
import com.nepxion.thunder.event.protocol.ProtocolEventInterceptor;
import com.nepxion.thunder.protocol.ProtocolMessage;

public class TraceEventInterceptor extends ProtocolEventInterceptor {
    @Override
    protected void onEvent(ProtocolEvent event) {
        ApplicationType applicationType = event.getApplicationType();
        ActionType actionType = event.getActionType();
        ProtocolType protocolType = event.getProtocolType();
        ProtocolMessage protocolMessage = event.getProtocolMessage();

        if (TraceConstants.PRINT) {
            System.out.println("--------------------收到异步事件通知--------------------");
            System.out.println("Application type=" + applicationType);
            System.out.println("Action type=" + actionType);
            System.out.println("Protocol type=" + protocolType);
            if (actionType != ActionType.SYSTEM) {
                System.out.println("Trace id=" + protocolMessage.getTraceId());
                System.out.println("Interface=" + protocolMessage.getInterface());
                System.out.println("Method=" + protocolMessage.getMethod());
                System.out.println("Parameter types=" + Arrays.asList(protocolMessage.getParameterTypes()));
                System.out.println("Parameters=" + Arrays.asList(protocolMessage.getParameters()));
            } else {
                System.out.println("From url=" + protocolMessage.getFromUrl());
                System.out.println("To url=" + protocolMessage.getToUrl());
            }
            System.out.println("Exception=" + ExceptionUtil.toExceptionString(protocolMessage.getException()));
            System.out.println("-------------------------------------------------------");
        }
    }
}