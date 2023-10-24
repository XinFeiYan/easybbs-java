package com.jiang.controller;

import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.StringTools;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.CreateImageCode;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.service.EmailCodeService;
import com.jiang.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class AcountController extends BaseController {

    @Resource
    private EmailCodeService emailCodeService;

    @Resource
    private UserInfoService userInfoService;
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
        vCode.write(response.getOutputStream());
    }

    @RequestMapping("/sendEmailCode")
    public ResponseVO sendEmailCode(HttpSession session,String email,String checkCode,Integer type){
        try{
            if(StringTools.isEmpty(email)||StringTools.isEmpty(checkCode)||type==null){
                throw new BusinessException(ResponseCodeEnum.CODE_500);
            }
            if(checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))){
                throw new BusinessException("图片验证码错误");
            }
            emailCodeService.sendEmailCode(email,type);
        }finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
        }

       return null;
    }

    @RequestMapping("/register")
    public ResponseVO register(HttpSession session,String email,String emailCode,String nickName,String password,String checkCode){

        try{
            if(StringTools.isEmpty(email)||StringTools.isEmpty(emailCode)||StringTools.isEmpty(nickName)||StringTools.isEmpty(password)||
                    StringTools.isEmpty(checkCode)){
                throw new BusinessException(ResponseCodeEnum.CODE_500);
            }
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            userInfoService.register(email,emailCode,nickName,password);
            return getSuccessResponseVO(null);
        }finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }

























}
