package com.example.permission.service;

import com.example.permission.common.CacheKeyConstants;
import com.example.permission.util.JsonUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author CookiesEason
 * 2019/01/21 14:27
 */
@Service
@Slf4j
public class SysCacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void saveCache(String value, int timeOutSeconds, CacheKeyConstants keyConstants) {
        saveCache(value, timeOutSeconds, keyConstants, null);
    }

    public void saveCache(String value, int timeOutSeconds, CacheKeyConstants keyConstants,
                          String... keys) {
        if (value == null) {
            return;
        }
        String key = generateCacheKey(keyConstants, keys);
        try {
            stringRedisTemplate.opsForValue().set(key, value, timeOutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("保存缓存失败,KEY:{},KEYS:{}", keyConstants, JsonUtil.obj2String(keys), e);
        }

    }

    public String getFromCache(CacheKeyConstants keyConstants, String... keys) {
        String key = generateCacheKey(keyConstants, keys);
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("获取缓存信息失败,KEY:{},KEYS:{}",  keyConstants, JsonUtil.obj2String(keys), e);
            return null;
        }
    }

    private String generateCacheKey(CacheKeyConstants keyConstants, String... keys) {
        String key = keyConstants.name();
        if (keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }

}
