package com.nepxion.thunder.event.smtp;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nepxion.thunder.common.constant.ThunderConstants;
import com.nepxion.thunder.common.property.ThunderProperties;
import com.nepxion.thunder.common.util.StringUtil;
import com.nepxion.thunder.event.protocol.ProtocolEvent;
import com.nepxion.thunder.event.protocol.ProtocolEventInterceptor;

public class SmtpEventInterceptor extends ProtocolEventInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(SmtpEventInterceptor.class);

    private ThunderProperties properties;
    private List<String> notificationExclusionList;
    
    private SmtpExecutor smtpExecutor;

    public SmtpEventInterceptor(ThunderProperties properties) {
        this.properties = properties;

        String notificationExclusion =  properties.getString(ThunderConstants.SMTP_NOTIFICATION_EXCLUSION_ATTRIBUTE_NAME);
        String host = properties.getString(ThunderConstants.SMTP_HOST_ATTRIBUTE_NAME);
        String user = properties.getString(ThunderConstants.SMTP_USER_ATTRIBUTE_NAME);
        String password = properties.getString(ThunderConstants.SMTP_PASSWORD_ATTRIBUTE_NAME);
        
        notificationExclusionList = new ArrayList<String>();
        String[] notificationExclusionArray = StringUtils.split(notificationExclusion, ",");
        for (String exclusion : notificationExclusionArray) {
            if (StringUtils.isNotEmpty(exclusion)) {
                notificationExclusionList.add(exclusion.trim());
            }
        }

        boolean ssl = properties.getBoolean(ThunderConstants.SMTP_SSL_ATTRIBUTE_NAME);
        if (ssl) {
            smtpExecutor = new SmtpSslExecutor(host, user, password);
        } else {
            smtpExecutor = new SmtpExecutor(host, user, password);
        }
    }

    @Override
    protected void onEvent(ProtocolEvent event) {
        String exception = event.toException();
        for (String notificationExclusion : notificationExclusionList) {
            if (exception.contains(notificationExclusion)) {
                return;
            }
        }
        
        try {
            send(event);
        } catch (Exception e) {
            LOG.error("Send mail failed", e);
        }
    }

    private void send(ProtocolEvent event) throws Exception {
        String from = properties.getString(ThunderConstants.SMTP_MAIL_FROM_ATTRIBUTE_NAME);
        String to = properties.getString(ThunderConstants.SMTP_MAIL_TO_ATTRIBUTE_NAME);
        String cc = properties.getString(ThunderConstants.SMTP_MAIL_CC_ATTRIBUTE_NAME);
        String bcc = properties.getString(ThunderConstants.SMTP_MAIL_BCC_ATTRIBUTE_NAME);
        String namespace = properties.getString(ThunderConstants.NAMESPACE_ELEMENT_NAME);
        String subject = StringUtil.firstLetterToUpper(namespace) + "调用异常通知";

        String text = event.toString();
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<style type=text/css>");
        builder.append("table{border-collapse:collapse;border-spacing:0;border-left:1px solid #888;border-top:1px solid #888;}");
        builder.append("td{border-right:1px solid #888;border-bottom:1px solid #888;padding:5px 15px;font-size:12px;}");
        builder.append("</style>");
        builder.append("</head>");
        // builder.append("<h3>" + subject + "</h3>");
        builder.append("<body>");
        builder.append("<table>");
        builder.append("<tr>");
        builder.append("<td bgcolor=#efefef>参数</td>");
        builder.append("<td bgcolor=#efefef>值</td>");
        builder.append("</tr>");
        Map<String, Object> map = event.toMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = null;
            if (entry.getValue() != null) {
                value = entry.getValue().toString().replace("\n", "<br>"); // 换行符
                if (StringUtils.equals(key, "exception")) {
                    value = value.replace(" ", "&nbsp;");
                    value = value.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"); // tab符
                }
                value = value.trim();
            }

            builder.append("<tr>");
            builder.append("<td>");
            builder.append(key);
            builder.append("</td>");
            builder.append("<td>");
            builder.append(value);
            builder.append("</td>");
            builder.append("</tr>");
        }
        builder.append("</table>");
        builder.append("</body>");
        builder.append("</html>");
        text = builder.toString();

        smtpExecutor.sendHtml(from, to, cc, bcc, subject, text, ThunderConstants.ENCODING_FORMAT);
    }
}