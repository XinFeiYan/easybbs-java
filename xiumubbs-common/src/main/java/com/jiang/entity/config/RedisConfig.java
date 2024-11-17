package com.jiang.entity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate template = new RedisTemplate<>();
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setConnectionFactory(connectionFactory);
        //字符串序列化方法
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        //hash用这个序列化会导入无法直接向hash存入int数据
        //template.setHashKeySerializer(stringSerializer);
        //template.setHashValueSerializer(stringSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
