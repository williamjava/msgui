package com.opensource.msgui.commons.utils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * TemplateFactory:邮件内容模板生产工厂类
 */
@Slf4j
public class TemplateFactory {
	// 模板文件路径
	private static String templatePath = "/mail";
	// 模板文件内容编码
	private static final String ENCODING = "utf-8";
	// 模板生成配置
	private static Configuration conf = new Configuration();
	// 邮件模板缓存池
	private static Map<String, Template> tempMap = new HashMap<String, Template>();

	static {
		// 设置模板文件路径
		conf.setClassForTemplateLoading(TemplateFactory.class, templatePath);
	}

	/**
	 * 通过模板文件名称获取指定模板
	 * 
	 * @Date:2014年4月26日 下午3:09:05
	 * @param name
	 *            模板文件名称
	 * @return Template 模板对象
	 * @throws IOException
	 * @Description:
	 * @return Template
	 */
	private static Template getTemplateByName(String name) throws IOException {
		if (tempMap.containsKey(name)) {
			// 缓存中有该模板直接返回
			return tempMap.get(name);
		}
		// 缓存中没有该模板时，生成新模板并放入缓存中
		Template temp = conf.getTemplate(name);
		tempMap.put(name, temp);
		return temp;
	}

	/**
	 * 根据指定模板将内容输出到控制台
	 * 
	 * @Date:2014年4月26日 下午3:14:18
	 * @param name
	 *            模板文件名称
	 * @param map
	 *            与模板内容转换对象
	 * @Description:
	 * @return void
	 */
	public static void outputToConsole(String name, Map<String, String> map) {
		try {
			// 通过Template可以将模板文件输出到相应的流
			Template temp = getTemplateByName(name);
			temp.setEncoding(ENCODING);
			temp.process(map, new PrintWriter(System.out));
		} catch (TemplateException e) {
			log.error(e.toString(), e);
		} catch (IOException e) {
			log.error(e.toString(), e);
		}
	}

	/**
	 * 根据指定模板将内容直接输出到文件
	 * 
	 * @Date:2014年4月26日 下午3:20:15
	 * @param name
	 *            模板文件名称
	 * @param map
	 *            与模板内容转换对象
	 * @param outFile
	 *            输出的文件绝对路径
	 * @Description:
	 * @return void
	 */
	public static void outputToFile(String name, Map<String, String> map, String outFile) {
		FileWriter out = null;
		try {
			out = new FileWriter(new File(outFile));
			Template temp = getTemplateByName(name);
			temp.setEncoding(ENCODING);
			temp.process(map, out);
		} catch (IOException e) {
			log.error(e.toString(), e);
		} catch (TemplateException e) {
			log.error(e.toString(), e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				log.error(e.toString(), e);
			}
		}
	}

	/**
	 * 
	 * @Date:2014年4月26日 下午3:24:37
	 * @param name
	 *            模板文件的名称
	 * @param map
	 *            与模板内容转换对象
	 * @return
	 * @Description:
	 * @return String
	 */
	public static String generateHtmlFromFtl(String name, Map<String, String> map) {
		Writer out = null;
		try {
			out = new StringWriter(2048);
			Template temp = getTemplateByName(name);
			temp.setEncoding(ENCODING);
			temp.process(map, out);
		} catch (Exception e) {
			log.error(e.toString(), e);
			return "";
		}
		return out.toString();
	}

	/**
	 * 
	 * delHTMLTag:使用正则表达式删除HTML标签
	 * 
	 * @param htmlStr
	 * @return
	 * @return :String
	 * @Date:2017年3月31日 下午4:15:07
	 */
	public static String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		return htmlStr.trim(); // 返回文本字符串
	}

}
