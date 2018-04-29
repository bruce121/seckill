package com.cxn.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.CountDownLatch;

/**
 * @program: seckill
 * @description: Redis配置类，使用java配置的方式，配置一个id为jedisPool的Bean。
 * @author: cxn
 * @create: 2018-04-26 22:34
 * @Version v1.0
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean(value="jedisPool")
    public JedisPool jedisPoolFactory(){
        final CountDownLatch countDownLatch = new CountDownLatch(100);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperties.getPoolMaxIdle());
        jedisPoolConfig.setMaxTotal(redisProperties.getPoolMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getPoolMaxWait());

        JedisPool jedisPool = new JedisPool(
                jedisPoolConfig,
                redisProperties.getHost(),
                redisProperties.getPort(),
                redisProperties.getTimeout(),
                // redisProperties.getPassword(),
                null,
                redisProperties.getDatabase());

        return jedisPool;
    }
}
