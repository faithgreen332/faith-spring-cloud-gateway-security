package com.faith.mygateway.security.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Leeko
 * @date 2022/3/28
 **/
@Configuration
@RequiredArgsConstructor
public class RedisClusterConfig {

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private String timeout;

    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    @Value("${spring.redis.lettuce.pool.max-active}")
    private String maxActive;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private String maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle}")
    private String minIdle;

    @Value("${spring.redis.lettuce.pool.max-wait}")
    private String maxWaitMillis;

    @Bean
    public RedisTemplate<String, Object> stringObjectRestTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);


        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        // 连接池配置
        GenericObjectPoolConfig<String> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(maxIdle == null ? 8 : Integer.parseInt(maxIdle));
        poolConfig.setMinIdle(minIdle == null ? 1 : Integer.parseInt(minIdle));
        poolConfig.setMaxTotal(maxActive == null ? 8 : Integer.parseInt(maxActive));
        poolConfig.setMaxWaitMillis(maxWaitMillis == null ? 5000L : Long.parseLong(maxWaitMillis));
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .build();
        // 单机redis
       /* RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host == null || "".equals(host) ? "localhost" : host.split(":")[0]);
        redisConfig.setPort(Integer.valueOf(host == null || "".equals(host) ? "6379" : host.split(":")[1]));
        if (password != null && !"".equals(password)) {
            redisConfig.setPassword(password);
        }
*/
        // 哨兵redis
        // RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();

        // 集群redis,要真的是集群才行，不然初始化会报错
        RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
        Set<RedisNode> nodeses = new HashSet<>();
        String[] hostses = nodes.split(",");
        for (String h : hostses) {
            String host = h.split(":")[0];
            int port = Integer.parseInt(h.split(":")[1]);
            nodeses.add(new RedisNode(host, port));
        }

        redisConfig.setClusterNodes(nodeses);
        // 跨集群执行命令时要遵循的最大重定向数量
        redisConfig.setMaxRedirects(3);
        redisConfig.setPassword(password);

        return new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
    }
}
