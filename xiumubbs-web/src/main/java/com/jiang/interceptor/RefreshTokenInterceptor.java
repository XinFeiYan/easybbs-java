package com.jiang.interceptor;

import com.alibaba.fastjson.JSON;
import com.jiang.Utils.StringTools;
import com.jiang.Utils.UserHolder;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.SessionWebUserDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;


public class RefreshTokenInterceptor implements HandlerInterceptor {

    //因为这里是一个new出来的类，无法使用构造方法
    private RedisTemplate redisTemplate;

    public RefreshTokenInterceptor(){

    }
    public RefreshTokenInterceptor(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return true;
        }

        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        if(StringTools.isEmpty(token)){
            return true;
        }
        String key = Constants.LOGIN_USER_KEY+token;
        String userJson = (String)redisTemplate.opsForValue().get(key);
        if(StringTools.isEmpty(userJson)){
            return true;
        }
        SessionWebUserDto sessionWebUserDto = JSON.parseObject(userJson,SessionWebUserDto.class);
        UserHolder.saveUserDto(sessionWebUserDto);
        redisTemplate.expire(key,1, TimeUnit.HOURS);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
