package com.cxn.seckill.service;

import com.alibaba.fastjson.JSON;
import com.cxn.seckill.config.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: seckill
 * @description: RedisService类
 * @author: cxn
 * @create: 2018-04-26 22:21
 * @Version v1.0
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取单个对象
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // generate real key
            String realKey = prefix.getPrefix() + key;
            String value = jedis.get(realKey);
            T t = stringToBean(value, clazz);
            if (t == null) {
                return null;
            }
            return t;
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 设置对象
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix, String key, T value){

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            // generate real key
            String realKey = keyPrefix.getPrefix() + key;
            int expireSeconds = keyPrefix.expireSeconds();
            if (expireSeconds <= 0){
                jedis.set(realKey, str);
            }else{
                jedis.setex(realKey, expireSeconds, str);
            }

            return true;
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 是否存在
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix keyPrefix, String key){

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // generate real key
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 清理缓存
     * @param keyPrefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix keyPrefix, String key){

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // generate real key
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.del(realKey) > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加一
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix keyPrefix, String key){

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // generate real key
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少一
     * @param keyPrefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix keyPrefix, String key){

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // generate real key
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将Bean对象转化为String类型
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();

        if (clazz == int.class || clazz == Integer.class) {
            return value + "";
        }else if(clazz == long.class || clazz == Long.class){
            return value + "";
        }else if(clazz == String.class){
            return (String) value;
        }

        return JSON.toJSONString(value);
    }

    /**
     * 将对象转化为bean对象
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static  <T> T stringToBean(String value, Class<?> clazz) {
        if (value == null || value.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(value);
        }else if(clazz == long.class || clazz == Long.class){
            return (T)Long.valueOf(value);
        }else if(clazz == String.class){
            return (T)value;
        }else{
            return (T)JSON.toJavaObject(JSON.parseObject(value), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
