package com.jiang.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 将拦截器注入
 */
@Configuration("webConfigurer")
public class WebConfigurer implements WebMvcConfigurer {

    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/ucenter/**").order(1);
        registry.addInterceptor(new RefreshTokenInterceptor(redisTemplate)).addPathPatterns("/**").order(0);
    }

    //拦截器注入
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }
}
