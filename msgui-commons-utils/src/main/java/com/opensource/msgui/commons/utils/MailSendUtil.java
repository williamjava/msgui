/**
 * Copyright (c) 2016,http://www.365wuliu.com/  All Rights Reserved.
 */
package com.opensource.msgui.commons.utils;


import com.opensource.msgui.commons.utils.model.Mail;
import com.opensource.msgui.commons.utils.model.Smtp;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * MailSendUtil:发邮件
 *
 */
@Slf4j
public class MailSendUtil {


	/**
	 * sendMailByTemplate:根据模板名称查找模板，加载模板内容后发送邮件，针对用户
	 *
	 * @param receiver
	 *            收件人地址
	 * @param subject
	 *            邮件主题
	 * @param map
	 *            邮件内容与模板内容转换对象
	 * @param templateName
	 *            模板文件名称
	 */
	public static boolean sendMailByTemplate(String receiver, String subject, Map<String, String> map, String templateName) {
		try {
			Smtp smtp = new Smtp();
			smtp.host = "smtp.exmail.qq.com";
			smtp.port = "465";
			smtp.username = "cccl-service@ccc-l.com";
			smtp.password = "NA5a484Xobsck8bc";
			String maiBody = TemplateFactory.generateHtmlFromFtl(templateName, map);
			Mail mail = new Mail();
			mail.from = "cccl-service@ccc-l.com";
			mail.screenname = "service";
			mail.subject = subject;
			mail.content = maiBody;
			mail.to = receiver;
			System.out.println("smtp:" + smtp.toString());
			Sendmail.sslSend(smtp, mail, null);

			return true;
		} catch (Exception e) {
			log.error("MailSendUtil sendMailByTemplate error:", e);
			return false;
		}
	}

}
