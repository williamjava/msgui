package com.opensource.msgui.commons.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

/**
 * @author gui
 */
public class ResponseResult implements Serializable {
  private static final long serialVersionUID = -9127050844792378533L;
  
  private Integer code;
  
  private String message;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String throwable;
  
  private Object data;
  
  public void setCode(Integer code) {
    this.code = code;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public void setThrowable(String throwable) {
    this.throwable = throwable;
  }
  
  public void setData(Object data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "ResponseResult(code=" + getCode() + ", message=" + getMessage() + ", throwable=" + getThrowable() + ", data=" + getData() + ")";
  }
  
  public Integer getCode() {
    return this.code;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public String getThrowable() {
    return this.throwable;
  }
  
  public Object getData() {
    return this.data;
  }
  
  public ResponseResult() {}
  
  public ResponseResult(ResponseCode code) {
    this.code = code.code();
    this.message = code.message();
  }
  
  public ResponseResult(ResponseCode code, String message) {
    this.code = code.code();
    this.message = message;
  }
  
  public ResponseResult(ResponseCode code, Object data) {
    this.code = code.code();
    this.message = code.message();
    this.data = data;
  }
  
  public ResponseResult(ResponseCode code, String message, Object data) {
    this.code = code.code();
    this.message = message;
    this.data = data;
  }
  
  public ResponseResult(ResponseCode code, Throwable throwable) {
    this.code = code.code();
    this.message = code.message();
    this.throwable = throwable.getMessage();
  }
  
  public static ResponseResult success() {
    return new ResponseResult(ResponseCode.SUCCESS);
  }
  
  public static ResponseResult success(String message) {
    return new ResponseResult(ResponseCode.SUCCESS, message);
  }
  
  public static ResponseResult success(Object data) {
    return new ResponseResult(ResponseCode.SUCCESS, data);
  }
  
  public static ResponseResult success(ResponseCode code, Object data) {
    return new ResponseResult(code, data);
  }
  
  public static ResponseResult success(String message, Object data) {
    return new ResponseResult(ResponseCode.SUCCESS, message, data);
  }
  
  public static ResponseResult failure() {
    return new ResponseResult(ResponseCode.FAILURE);
  }
  
  public static ResponseResult failure(String message) {
    return new ResponseResult(ResponseCode.FAILURE, message);
  }
  
  public static ResponseResult failure(ResponseCode code) {
    return new ResponseResult(code);
  }
  
  public static ResponseResult failure(ResponseCode code, Throwable throwable) {
    return new ResponseResult(code, throwable);
  }
  
  public static ResponseResult failure(ResponseCode code, Object data) {
    return new ResponseResult(code, data);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((this.data == null) ? 0 : this.data.hashCode());
    result = 31 * result + ((this.message == null) ? 0 : this.message.hashCode());
    result = 31 * result + ((this.code == null) ? 0 : this.code.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ResponseResult other = (ResponseResult)obj;
    if (this.data == null) {
      if (other.data != null) {
        return false;
      }
    } else if (!this.data.equals(other.data)) {
      return false;
    } 
    if (this.message == null) {
      if (other.message != null) {
        return false;
      }
    } else if (!this.message.equals(other.message)) {
      return false;
    } 
    if (this.code == null) {
      return (other.code == null);
    }
    return this.code.equals(other.code);
  }
}
