package com.jiang.Utils;

import com.jiang.entity.constants.Constants;
import com.jiang.service.Impl.EmailCodeServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CookieUtil {
    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImp.class);
    public static void writeLoginToken(String token, HttpServletResponse response){
        Cookie cookie = new Cookie(Constants.SESSION_KEY, token);
        /*cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");*/
        //设置生存时间.0无效，-1永久有效，时间是秒，生存时间设置为1年
        cookie.setMaxAge(60 * 60 * 24);
        //设置安全机制
        cookie.setHttpOnly(true);
        logger.info("写 cookie name：{},value：{}",cookie.getName(),cookie.getValue());
        //将cookie写到浏览器上
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return null;
        }
        for(Cookie cookie:cookies){
            if(Constants.SESSION_KEY.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void deleteLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return ;
        }

        for (Cookie cookie : cookies) {
            if(Constants.SESSION_KEY.equals(cookie.getName())){
                //设置cookie的有效期为0
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                return;
            }
        }

    }
}
