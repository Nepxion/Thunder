package com.nepxion.thunder.event.protocol;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.entity.ActionType;
import com.nepxion.thunder.common.entity.ApplicationType;
import com.nepxion.thunder.common.entity.ProtocolType;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.event.eventbus.EventControllerFactory;
import com.nepxion.thunder.protocol.ProtocolMessage;

public class ProtocolEventFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolEventFactory.class);

    private static boolean eventNotification = false;

    public static void initialize(ThunderProperties properties) {
        eventNotification = properties.getBoolean(ThunderConstants.EVENT_NOTIFICATION_ATTRIBUTE_NAME);
        if (eventNotification) {
            LOG.info("Event notification is enabled...");
        }
    }

    public static void postServerConsumerEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.SERVICE;

        postConsumerEvent(applicationType, protocolType, message);
    }

    public static void postClientConsumerEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.REFERENCE;

        postConsumerEvent(applicationType, protocolType, message);
    }

    public static void postConsumerEvent(ApplicationType applicationType, ProtocolType protocolType, ProtocolMessage message) {
        ActionType actionType = ActionType.CONSUME;

        postEvent(applicationType, actionType, protocolType, message);
    }

    public static void postServerProducerEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.SERVICE;

        postProducerEvent(applicationType, protocolType, message);
    }

    public static void postClientProducerEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.REFERENCE;

        postProducerEvent(applicationType, protocolType, message);
    }

    public static void postProducerEvent(ApplicationType applicationType, ProtocolType protocolType, ProtocolMessage message) {
        ActionType actionType = ActionType.PRODUCE;

        postEvent(applicationType, actionType, protocolType, message);
    }

    public static void postServerSystemEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.SERVICE;

        postSystemEvent(applicationType, protocolType, message);
    }

    public static void postClientSystemEvent(ProtocolType protocolType, ProtocolMessage message) {
        ApplicationType applicationType = ApplicationType.REFERENCE;

        postSystemEvent(applicationType, protocolType, message);
    }

    public static void postSystemEvent(ApplicationType applicationType, ProtocolType protocolType, ProtocolMessage message) {
        ActionType actionType = ActionType.SYSTEM;

        postEvent(applicationType, actionType, protocolType, message);
    }

    public static void postEvent(ApplicationType applicationType, ActionType actionType, ProtocolType protocolType, ProtocolMessage message) {
        if (message.getException() == null) {
            return;
        }

        ProtocolEvent protocolEvent = new ProtocolEvent(applicationType, actionType, protocolType, message);

        if (eventNotification) {
            EventControllerFactory.getAsyncController().post(protocolEvent);
        }
    }
}