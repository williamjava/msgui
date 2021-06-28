package com.opensource.msgui.commons.utils;

import com.opensource.msgui.commons.utils.model.EmailFileVo;
import com.opensource.msgui.commons.utils.model.Mail;
import com.opensource.msgui.commons.utils.model.Smtp;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Sendmail:发邮件主要类
 *
 */
public class Sendmail {

    private final static String MIMETYPE = "text/html; charset=UTF-8";


    /**
     * sslSend:ssl发送邮件
     *
     * @param smtp
     * @param mail
     * @param emailFileList
     * @return :void
     * @throws MessagingException
     * @throws AddressException
     * @Date:2017年8月22日 下午4:13:35
     */
    @SuppressWarnings("restriction")
    public static void sslSend(Smtp smtp, Mail mail, List<EmailFileVo> emailFileList) throws AddressException, MessagingException, IOException {
        if (BaseVerifyUtil.isEmpty(mail.to) && BaseVerifyUtil.isEmpty(mail.cc) && BaseVerifyUtil.isEmpty(mail.bcc)) {
            return;
        }
        List<InputStream> inputStreamList = new ArrayList<>();
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            Properties props = System.getProperties();
            props.setProperty("mail.smtp.host", smtp.host);
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", smtp.port);
            props.setProperty("mail.smtp.socketFactory.port", smtp.port);
            props.put("mail.smtp.auth", "true");

            final String username = smtp.username;
            final String password = smtp.password;

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message msg = new MimeMessage(session);
            if (BaseVerifyUtil.isNotEmpty(mail.screenname)) {
                msg.setFrom(new InternetAddress(MimeUtility.encodeText(mail.screenname) + "<" + mail.from + ">"));
            } else {
                msg.setFrom(new InternetAddress(mail.from));
            }

            if (BaseVerifyUtil.isNotEmpty(mail.to)) {
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.to, false));
            }
            if (BaseVerifyUtil.isNotEmpty(mail.cc)) {
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.cc, false));
            }
            if (BaseVerifyUtil.isNotEmpty(mail.bcc)) {
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail.bcc, false));
            }
            Multipart mPart = new MimeMultipart();
            MimeBodyPart mBodyContent = new MimeBodyPart();
            mBodyContent.setContent(mail.content, MIMETYPE);
            mPart.addBodyPart(mBodyContent);

            /*添加附件*/
            if (emailFileList != null && emailFileList.size() > 0) {
                for (int i = 0, j = emailFileList.size(); i < j; i++) {
                    EmailFileVo emailFileVo = emailFileList.get(i);
                    InputStream inputStream = emailFileVo.getInputStream();
                    String fileName = emailFileVo.getFileName();
                    if (inputStream != null) {
                        MimeBodyPart fileBody = new MimeBodyPart();
                        DataSource source = new ByteArrayDataSource(inputStream, "application/msexcel");
                        fileBody.setDataHandler(new DataHandler(source));
                        // 中文乱码问题
                        fileBody.setFileName(MimeUtility.encodeText(fileName));
                        mPart.addBodyPart(fileBody);
                        inputStreamList.add(inputStream);
                    }
                }
            }

            msg.setSubject(mail.subject);
            msg.setContent(mPart);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } finally {
            //关闭流
            if (inputStreamList.size() > 0) {
                for (int i = 0, j = inputStreamList.size(); i < j; i++) {
                    inputStreamList.get(i).close();
                }
            }
        }
    }


}
