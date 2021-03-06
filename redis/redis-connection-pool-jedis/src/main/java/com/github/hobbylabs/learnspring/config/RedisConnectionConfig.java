package com.github.hobbylabs.learnspring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

// https://mashhurs.wordpress.com/2020/03/26/jedis-vs-lettuce-java-redis-clients/
// https://www.oodlestechnologies.com/blogs/Configure-Connection-Pooling-With-Redis-In-Spring-Boot/
@Configuration
public class RedisConnectionConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.pool.max-total}")
    private int maxTotal;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.pool.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.redis.pool.test-on-return}")
    private boolean testOnReturn;
    @Value("${spring.redis.pool.test-while-idle}")
    private boolean testWhileIdle;
    @Value("${spring.redis.pool.min-evictable-idle-time-millis}")
    private int minEvictableIdleTimeMillis;
    @Value("${spring.redis.pool.time-tetween-eviction-runs-millis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${spring.redis.pool.num-tests-per-eviction-run:3}")
    private int numTestsPerEvictionRun;
    @Value("${spring.redis.pool.set-block-when-exhausted:true}")
    private boolean setBlockWhenExhausted;

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // This is a patch not to put the unsupposed string like "\xac\xed\x00\x05t\x00\x05"
        // in front of the key when see the keys with redis-cli.
        // See description: https://qiita.com/taka_22/items/673bb2e6bf7d4a303447
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.afterPropertiesSet();
        //template.setHashKeySerializer(stringSerializer);
        //template.setHashValueSerializer(stringSerializer);

        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration
                = new RedisStandaloneConfiguration(host, port);
        redisStandaloneConfiguration.setDatabase(0);

        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        JedisClientConfiguration config = builder.usePooling().poolConfig(jedisPoolConfig()).build();

        return new JedisConnectionFactory(redisStandaloneConfiguration, config);
    }

    private JedisPoolConfig jedisPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);
        poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        poolConfig.setBlockWhenExhausted(setBlockWhenExhausted);
        return poolConfig;
    }
}
