package com.faith.mygateway.security.config;

import com.alibaba.fastjson.JSONObject;
import com.faith.mygateway.security.utils.MapObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 86183
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FunRedisClient {

    final RedisTemplate<String, Object> template;

    /**
     * 设置缓存
     *
     * @param key   key
     * @param value value
     */
    public void set(String key, String value) {
        template.opsForValue().set(key, value);
        log.debug("Redis:set cache key={},value={}", key, value);
    }

    public void setWithExpire(String key, String value, Long expire, TimeUnit unit) {
        template.opsForValue().set(key, value, expire, unit);
        log.debug("Redis:setWithExpire cache key={},value={},expire={},unit={}", key, value, expire, unit);
    }

    /**
     * hash 缓存
     *
     * @param key  key
     * @param bean bean
     */
    public void hmSetAll(String key, Object bean) {
        template.opsForHash().putAll(key, MapObjectUtils.beanToMapSkipNullValue(bean));
        log.debug("Redis:hmSet cache key={},value={}", key, bean);
    }

    /**
     * hash 缓存带过期时间
     *
     * @param key  key
     * @param bean bean
     */
    public void hmSetAllExpire(String key, Object bean, int expire, TimeUnit timeUnit) {
        synchronized (FunRedisClient.class) {
            hmSetAll(key, bean);
            expire(key, expire, timeUnit);
        }
        log.debug("Redis:hmSetAllExpire cache key={},value={},expire={},timeUnit={}", key, bean, expire, timeUnit);
    }

    public <T> T hmGetAll(String key, Class<T> type) {
        Map<Object, Object> entries = template.opsForHash().entries(key);
        return JSONObject.parseObject(JSONObject.toJSONString(entries), type);
    }

    public void expire(String key, long expire, TimeUnit timeUnit) {
        template.expire(key, expire, timeUnit);
        log.debug("Redis: expire key={},expire={},timeUnit={}", key, expire, timeUnit);
    }

    /**
     * 设置缓存，并且自己指定过期时间 ex(秒) px(毫秒)
     *
     * @param key        key
     * @param expireTime expireTime
     * @param timeUnit   timeUnit
     */
    public void setEx(String key, Object value, long expireTime, TimeUnit timeUnit) {
        template.opsForValue().set(key, value, expireTime, timeUnit);
        log.debug("Redis:setWithExpireTime cache key={},value={},expireTime={},timeUnit={}", key, value, expireTime, timeUnit);
    }


    /**
     * setNx
     *
     * @param key   key
     * @param value value
     */
    public boolean setNx(final String key, final String value) {
        log.debug("Redis:setNx cache key={},value={}", key, value);
        return Boolean.TRUE.equals(template.opsForValue().setIfAbsent(key, value));
    }

    /**
     * setNx with expireTime
     *
     * @param key        key
     * @param value      value
     * @param expireTime expireTime
     */

    public boolean setNx(final String key, final String value, final long expireTime, TimeUnit timeUnit) {
        log.debug("Redis:setNx cache key={},value={},expireTime={},timeUnit={}", key, value, expireTime, timeUnit);
        return Boolean.TRUE.equals(template.opsForValue().setIfAbsent(key, value, expireTime, timeUnit));
    }

    /**
     * 获取指定key的缓存
     *
     * @param key key
     */
    public Object get(String key) {
        Object value = template.opsForValue().get(key);
        log.debug("Redis:get cache key={},value={}", key, value);
        return value;
    }

    /**
     * 删除 key
     *
     * @param key key
     */
    public boolean del(String key) {
        Boolean delete = template.delete(key);
        log.debug("Redis:delete cache key={} ok? {}", key, delete);
        return Boolean.TRUE.equals(delete);
    }

    /**
     * 获取key的过期时间
     *
     * @param key key
     * @return 剩余的生存时间
     */
    public Long ttl(String key) {
        Long expire = template.getExpire(key);
        log.debug("Redis:ttl cache key={},value={}", key, expire);
        return expire;
    }

    /**
     * 判断key是否存在
     *
     * @param key key
     * @return 存在：是，不存在：否
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }
}
