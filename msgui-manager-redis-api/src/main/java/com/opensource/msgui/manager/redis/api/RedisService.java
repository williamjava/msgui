package com.opensource.msgui.manager.redis.api;

import java.util.concurrent.TimeUnit;

public interface RedisService {
  public static final int SEC = 3600;
  
  void set(String paramString, Object paramObject);
  
  void set(String paramString, Object paramObject, int paramInt);

  Boolean setIfAbsent(String key, Object value, long expiredTime, TimeUnit timeout);
  
  void del(String paramString);
  
  String get(String paramString);
  
  boolean hasKey(String paramString);
}
