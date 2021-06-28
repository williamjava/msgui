package com.opensource.msgui.commons.utils.model;

import lombok.Data;

/**
 * Smtp:邮件配置对象
 */
@Data
public class Smtp {
	public String host;
	public String port;
	public String username;
	public String password;
}
