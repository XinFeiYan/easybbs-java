package com.jiang.controller;

import com.alibaba.fastjson.JSON;
import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Enums.VerifyRegexEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.JWTUtil;
import com.jiang.Utils.StringTools;
import com.jiang.Utils.SysCacheUtils;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.config.AdminConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.*;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.service.EmailCodeService;
import com.jiang.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class AccountController extends BaseController {

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private AdminConfig adminConfig;

    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session,Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130,40,5,10);
        //告诉浏览器不要缓存
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-control","no-cache");
        //表示响应过期
        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();

        if(type==null||type==0){
            //发送的图片验证码
            session.setAttribute(Constants.CHECK_CODE_KEY,code);
        }else{
            //与邮箱有关的图片验证码
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL,code);
        }
        //将图片写入返回文件流
        vCode.write(response.getOutputStream());
    }

    @RequestMapping("/sendEmailCode")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO sendEmailCode(HttpSession session,
                                    @VerifyParam(required = true,regex = VerifyRegexEnum.EMAIL,max=150)String email,
                                    @VerifyParam(required = true) String checkCode,
                                    @VerifyParam(required = true)Integer type){
        try{
            if(StringTools.isEmpty(email)||StringTools.isEmpty(checkCode)||type==null){
                throw new BusinessException(ResponseCodeEnum.CODE_500);
            }
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))){
                throw new BusinessException("图片验证码错误");
            }
            emailCodeService.sendEmailCode(email,type);
        }finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
        }

       return null;
    }

    @RequestMapping("/login")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO login(HttpSession session, HttpServletRequest request,HttpServletResponse response,
                            @VerifyParam(required = true,regex = VerifyRegexEnum.EMAIL,max=150) String account,
                            @VerifyParam(required = true)String password,
                            @VerifyParam(required = true)String checkCode){


            if(StringTools.isEmpty(account)||StringTools.isEmpty(password)||StringTools.isEmpty(checkCode)){
                throw new BusinessException(ResponseCodeEnum.CODE_500);
            }
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }


            if(!adminConfig.getAdminAccount().equals(account)||!StringTools.encodeMd5(adminConfig.getAdminPassword()).equals(password)){
                throw new BusinessException("账号或密码错误");
            }
            SessionAdminUserDto sessionUser = new SessionAdminUserDto();
            sessionUser.setAccount("管理员");
            session.setAttribute(Constants.SESSION_KEY,sessionUser);
            return getSuccessResponseVO(sessionUser);
    }

    @RequestMapping("/logout")
    @GlobalInterceptor
    //要加一个token，这是前端没有实现
    public ResponseVO logout(HttpSession session,HttpServletRequest request){
        /**
         * 删除delete，返回删除成功，同时删除本地session
         */
        /*String sessionId = CookieUtil.readLoginToken(request);
        //删除session
        CookieUtil.deleteLoginToken(request,response);
        //删除缓存
        redisTemplate.delete(sessionId);*/
        //session.invalidate();
        //删除redis中的token
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }

        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        if(token==null){
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/getSysSetting")
    @GlobalInterceptor
    public ResponseVO getSysSetting(){
        SysSettingDto sysSettingDto = SysCacheUtils.getSysSetting();
        //这里面有三个值,只要其中一个，使用Map
        SysSetting4CommentDto commentSetting = sysSettingDto.getCommentSetting();
        Map<String,Object> result = new HashMap<>();
        result.put("commentOpen",commentSetting.getCommentOpen());
        return getSuccessResponseVO(result);
    }


    @RequestMapping("/resetPwd")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO resetPwd(HttpSession session,
                               @VerifyParam(required = true,regex = VerifyRegexEnum.EMAIL,max=150) String email,
                               @VerifyParam(required = true)String password,
                               @VerifyParam(required = true)String emailCode,
                               @VerifyParam(required = true)String checkCode){
        try{
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            userInfoService.resetPwd(email,password,emailCode);
            return getSuccessResponseVO(null);
        }finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }

    }























}
