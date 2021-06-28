package com.opensource.msgui.commons.utils.model;

import lombok.Data;

import java.util.Vector;

/**
 * Mail:邮件对象
 *
 */
@Data
public class Mail {
	public String from;
	public String to;
	public String screenname;
	public String cc;
	public String bcc;
	public String subject;
	public String content;
	public Vector<Object> attachments;
}
