package com.opensource.msgui.manager.redis.api.impl;

import com.opensource.msgui.manager.redis.api.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, Object value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, int seconds) {
        this.redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void del(String key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public String get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Object o = this.redisTemplate.opsForValue().get(key);
        return (null == o) ? null : o.toString();
    }

    @Override
    public boolean hasKey(String key) {
        return this.redisTemplate.hasKey(key).booleanValue();
    }
}