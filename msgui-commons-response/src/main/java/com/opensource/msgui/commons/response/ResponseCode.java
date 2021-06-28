package com.opensource.msgui.commons.response;

/**
 * @author gui
 */
public enum ResponseCode {
  /**
   * 响应Code码枚举
   */
  SUCCESS(Integer.valueOf(20000), "成功"),
  FAILURE(Integer.valueOf(20002), "失败"),
  PARAM_IS_INVALID(Integer.valueOf(10001), "参数无效"),
  PARAM_IS_BLANK(Integer.valueOf(10002), "参数为空"),
  PARAM_TYPE_BIND_ERROR(Integer.valueOf(10003), "参数类型错误"),
  PARAM_NOT_COMPLETE(Integer.valueOf(10004), "参数缺失"),
  USER_NOT_LOGGED_IN(Integer.valueOf(20001), "用户未登录"),
  USER_LOGIN_ERROR(Integer.valueOf(20002), "账号不存在或密码错误"),
  USER_ACCOUNT_FORBIDDEN(Integer.valueOf(20003), "账号已被禁用"),
  USER_NOT_EXIST(Integer.valueOf(20004), "用户不存在"),
  USER_HAS_EXISTED(Integer.valueOf(20005), "用户已存在"),
  SPECIFIED_QUESTIONED_USER_NOT_EXIST(Integer.valueOf(30001), "业务系统中用户不存在"),
  SYSTEM_INNER_ERROR(Integer.valueOf(40001), "系统繁忙，请稍后重试"),
  RESULT_DATA_NONE(Integer.valueOf(50001), "数据未找到"),
  DATA_IS_WRONG(Integer.valueOf(50002), "数据有误"),
  DATA_ALREADY_EXISTED(Integer.valueOf(50003), "数据已存在"),
  INTERFACE_INNER_INVOKE_ERROR(Integer.valueOf(60001), "内部系统接口调用异常"),
  INTERFACE_OUTER_INVOKE_ERROR(Integer.valueOf(60002), "外部系统接口调用异常"),
  INTERFACE_FORBID_VISIT(Integer.valueOf(60003), "该接口禁止访问"),
  INTERFACE_ADDRESS_INVALID(Integer.valueOf(60004), "接口地址无效"),
  INTERFACE_REQUEST_TIMEOUT(Integer.valueOf(60005), "接口请求超时"),
  INTERFACE_EXCEED_LOAD(Integer.valueOf(60006), "接口负载过高"),
  PERMISSION_NO_ACCESS(Integer.valueOf(70001), "无访问权限");
  
  private Integer code;
  
  private String message;
  
  ResponseCode(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
  
  public Integer code() {
    return this.code;
  }
  
  public String message() {
    return this.message;
  }
  
  public static String getMessage(String name) {
    for (ResponseCode item : values()) {
      if (item.name().equals(name)) {
        return item.message;
      }
    } 
    return name;
  }
  
  public static Integer getCode(String name) {
    for (ResponseCode item : values()) {
      if (item.name().equals(name)) {
        return item.code;
      }
    } 
    return null;
  }

  @Override
  public String toString() {
    return name();
  }
}
