package com.jiang.controller;

import com.jiang.Enums.ArticleStatusEnum;
import com.jiang.Enums.MessageTypeEnum;
import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Enums.UserStatusEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.CopyTools;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.UserInfo;
import com.jiang.entity.query.*;
import com.jiang.entity.vo.ForumArticleVO;
import com.jiang.entity.vo.PaginationResultVO;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.entity.vo.UserInfoVO;
import com.jiang.service.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController("userCenterController")
@RequestMapping("/user")
public class UserInfoController extends BaseController {

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/loadUserList")
    public ResponseVO loadUserList(UserInfoQuery userInfoQuery){
        userInfoQuery.setOrderBy("join_time desc");
        return getSuccessResponseVO(userInfoService.findListByPage(userInfoQuery));
    }

    @RequestMapping("/updateUserStatus")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO updateUserStatus(@VerifyParam(required = true) Integer status,@VerifyParam(required = true) String userId){
        userInfoService.updateUserStatus(status,userId);
        return getSuccessResponseVO(null);
    }


}

