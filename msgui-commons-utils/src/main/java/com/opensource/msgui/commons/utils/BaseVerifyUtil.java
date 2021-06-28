package com.opensource.msgui.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [基础验证工具类]
 * 
 */
public class BaseVerifyUtil {

	/**
	 * 正则表达式：验证手机号
	 *
	 * old: ^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\d{8}$
	 */
	public static final String REGEX_MOBILE = "^(13|14|15|16|17|18|19)\\d{9}$";

	/**
	 * 正则表达式：验证邮箱
	 */
	public static final String REGEX_EMAIL = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";

	/**
	 * 去除首位空格 包括特殊字符
	 */
	public static final Pattern PATTERN_TRIM = Pattern.compile("^[\\s\\u00A0]*(([^\\s\\u00A0].*[^\\s\\u00A0])|([^\\s\\u00A0]))?[\\s\\u00A0]*$");
	public static final Pattern PATTERN_ASC_16 = Pattern.compile("\\u00A0");

	/**
	 * 正则表达式：验证汉字
	 */
	public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

	/**
	 * 正则表达式：验证身份证
	 */
	public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

	/**
	 * 正则表达式：验证URL
	 */
	public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * 用户名，汉字、字母、数字的组合
	 */
	public static final String REGEX_USERNAME = "(^[a-z0-9A-Z])[a-z0-9A-Z_]+([a-z0-9-A-Z])";

	/**
	 * [是否不为空、或者为0]
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNotInteger(Integer num) {
		if (null == num || 0 == num) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * [是否不为空]
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNotZeroInteger(Integer num) {
		if (null == num) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 去除首位空格
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		if (str == null)
			return null;
		str = PATTERN_ASC_16.matcher(str).replaceAll(" ");
		Matcher m = PATTERN_TRIM.matcher(str);
		if (m.find()) {
			str = m.group(1);
			if (str == null)
				str = m.group(2);
		}
		if (str == null)
			return "";
		return str;
	}

	/**
	 * 校验汉字
	 * 
	 * @param chinese
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isChinese(String chinese) {
		return Pattern.matches(REGEX_CHINESE, chinese);
	}

	/**
	 * 校验身份证
	 * 
	 * @param idCard
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String idCard) {
		return Pattern.matches(REGEX_ID_CARD, idCard);
	}

	/**
	 * 验证字符串是否包含特殊符号
	 * 
	 * @param paraments
	 *            字符串
	 * @param split
	 *            检验自定义特殊符号
	 * @return
	 * @Creator: YANG GUO QING
	 * @Date:2016年5月12日 下午1:33:35
	 */
	public static boolean isNotSplit(String paraments, String split) {
		if (paraments.contains(split))
			return true;
		else
			return false;
	}

	/**
	 * [判断字符串是否为空]
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * [判断字符串是否不为空]
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if (null == str || "".equals(str.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * [判断是否是邮箱地址]
	 * 
	 * @param
	 * @return
	 */
	public static boolean isEmail(String email) {
		boolean tag = true;
		if (!email.matches(REGEX_EMAIL)) {
			tag = false;
		}
		return tag;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isTel(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 验证是否手机号
	 * 
	 * @param phone
	 * @return YangJie 2018年6月7日 下午1:42:51
	 */
	public static boolean isPhone(String phone) {
		if (phone.length() != 11) {
			return false;
		} else {
			Pattern p = Pattern.compile(REGEX_MOBILE);
			Matcher m = p.matcher(phone);
			boolean isMatch = m.matches();
			if (!isMatch) {
				return false;
			}
			return isMatch;
		}
	}

	// @描述：是否是xls的excel，返回true是xls
	public static boolean isXlsExcel(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	// @描述：是否是xlsx的excel，返回true是xlsx
	public static boolean isXlsxExcel(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	/**
	 * 验证用户名 汉字、字母、数字的组合
	 * 
	 * @param userName
	 * @return
	 */
	public static boolean isUserName(String userName) {
		return Pattern.matches(REGEX_USERNAME, userName);
	}

	/**
	 * 将ID格式转换成List集合
	 * @param ids
	 * @return 
	 * @return :List<Long> 
	 * @Creator:yangguoqing
	 * @Date:2018年5月3日 下午6:33:12
	 */
	public static List<Long> conventIdByList(String ids){
		List<Long> idList = new ArrayList<Long>();
		if(BaseVerifyUtil.isNotEmpty(ids)){
			if(ids.contains(",")){
				String[] idObject = ids.split(",");
				for (String key : idObject) {
					idList.add(Long.parseLong(key));
				}
			}else{
				idList.add(Long.parseLong(ids));
			}
		}
		return idList;
	}

	/**
	 * 将String格式转换成List集合
	 * @param str【需要分隔的字符串】
	 * @param split【分隔符】
	 * @return 
	 * @return :List<String> 
	 * @Creator:yangguoqing
	 * @Date:2019年10月30日 下午2:57:42
	 */
	public static List<String> conventStrByList(String str, String split) {
		List<String> strList = new ArrayList<>();
		if(BaseVerifyUtil.isNotEmpty(str)){
			if(str.contains(split)){
				String[] strObj = str.split(split);
				for(String key : strObj){
					strList.add(key);
				}
			}else{
				strList.add(str);
			}
		}
		return strList;
	}



}
