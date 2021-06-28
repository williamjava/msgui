package com.opensource.msgui.manager.redis.api;

public interface RedisService {
  public static final int SEC = 3600;
  
  void set(String paramString, Object paramObject);
  
  void set(String paramString, Object paramObject, int paramInt);
  
  void del(String paramString);
  
  String get(String paramString);
  
  boolean hasKey(String paramString);
}
