package com.nepxion.thunder.event.smtp;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;

public class SmtpExecutor {
    protected String host;
    protected String user;
    protected String password;

    protected Session session;

    public SmtpExecutor(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;

        this.session = createSession();
    }

    protected Properties createProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.socketFactory.port", "25");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        return properties;
    }

    protected Authenticator createAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        };
    }

    protected Session createSession() {
        Properties properties = createProperties();
        Authenticator authenticator = createAuthenticator();

        Session session = Session.getDefaultInstance(properties, authenticator);

        return session;
    }

    public void sendText(String from, String to, String cc, String bcc, String subject, String text) throws Exception {
        send(from, to, cc, bcc, subject, text, false, null);
    }

    public void sendHtml(String from, String to, String cc, String bcc, String subject, String text, String encoding) throws Exception {
        send(from, to, cc, bcc, subject, text, true, encoding);
    }

    /**
     * 发送邮件
     * @param from      发送者地址
     * @param to        接受者地址，可以多个，用逗号隔开
     * @param cc        抄送者地址，可以多个，用逗号隔开
     * @param bcc       暗抄送者地址，可以多个，用逗号隔开
     * @param subject   邮件主体
     * @param text      邮件内容，支持Html格式
     * @param htmlStyle 邮件内容，支持Html格式
     * @param encoding  编码
     * @throws Exception
     */
    public void send(String from, String to, String cc, String bcc, String subject, String text, boolean htmlStyle, String encoding) throws Exception {
        MimeMessage message = new MimeMessage(session);

        message.setSentDate(new Date());
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        if (StringUtils.isNotEmpty(cc)) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
        }
        if (StringUtils.isNotEmpty(bcc)) {
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
        }
        message.setSubject(subject);
        if (htmlStyle) {
            message.setContent(text, "text/html;charset=" + encoding);
        } else {
            message.setText(text);
        }

        Transport.send(message);
    }
}