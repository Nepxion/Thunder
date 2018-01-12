package com.nepxion.thunder.event.smtp;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstant;
import com.nepxion.thunder.common.property.ThunderProperties;

public class SmtpEventFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SmtpEventFactory.class);

    public static void initialize(ThunderProperties properties) {
        boolean smtpNotification = properties.getBoolean(ThunderConstant.SMTP_NOTIFICATION_ATTRIBUTE_NAME);
        if (smtpNotification) {
            new SmtpEventInterceptor(properties);

            LOG.info("Smtp mail notification is enabled...");
        }
    }
}