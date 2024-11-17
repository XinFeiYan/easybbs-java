package com.jiang.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 将拦截器注入
 */
@Configuration("webAppConfigurer")
public class WebAppConfigurer implements WebMvcConfigurer {

    @Resource
    private AppInterceptor appInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appInterceptor).addPathPatterns("/**");
    }

    //拦截器注入
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }
}
