package com.jiang.interceptor;


import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.entity.config.AdminConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.SessionAdminUserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AppInterceptor implements HandlerInterceptor {

    @Resource
    private AdminConfig adminConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler == null) {
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (request.getRequestURL().indexOf("checkCode") != -1 || request.getRequestURL().indexOf("login") != -1) {

            return true;
        }

        checkLogin();
        return true;
    }


    private void checkLogin(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        SessionAdminUserDto sessionUser = (SessionAdminUserDto)session.getAttribute(Constants.SESSION_KEY);
        //如果是开发环境就自己设置一个，不用登录
        if(sessionUser==null&&adminConfig.getIsDev()){
            sessionUser = new SessionAdminUserDto();
            sessionUser.setAccount("管理员");
            session.setAttribute(Constants.SESSION_KEY,sessionUser);
        }
        if(null==sessionUser){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }


    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request,response,handler,modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request,response,handler,ex);
    }

}
