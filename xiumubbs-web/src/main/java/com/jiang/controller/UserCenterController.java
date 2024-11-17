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
import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.query.LikeRecordQuery;
import com.jiang.entity.query.UserIntegralRecordQuery;
import com.jiang.entity.query.UserMessageQuery;
import com.jiang.entity.vo.ForumArticleVO;
import com.jiang.entity.vo.PaginationResultVO;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.entity.vo.UserInfoVO;
import com.jiang.service.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController("userCenterController")
@RequestMapping("/ucenter")
public class UserCenterController extends BaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private LikeRecordService likeRecordService;
    
    @Resource
    private UserIntegralRecordService userIntegralRecordService;

    @Resource
    private UserMessageService userMessageService;
    @RequestMapping("/getUserInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getUserInfo(@VerifyParam(required = true) String userId){
        UserInfo userInfo = userInfoService.getByUserId(userId);

        if(userId==null|| UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }

        ForumArticleQuery forumArticleQuery = new ForumArticleQuery();
        forumArticleQuery.setUserId(userId);
        forumArticleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        Integer postCount = forumArticleService.findCountByParam(forumArticleQuery);

        UserInfoVO userInfoVO = CopyTools.copy(userInfo,UserInfoVO.class);
        userInfoVO.setPostCount(postCount);

        LikeRecordQuery recordQuery = new LikeRecordQuery();
        recordQuery.setAuthorUserId(userId);
        //根据条件查找数量
        Integer likeCount = likeRecordService.findCountByParam(recordQuery);
        userInfoVO.setLikeCount(likeCount);

        return getSuccessResponseVO(userInfoVO);
    }

    //获取文章信息
    @RequestMapping("/loadUserArticle")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadUserArticle(HttpSession session, @VerifyParam(required = true) String userId, @VerifyParam(required = true) Integer type,Integer pageNo){
        UserInfo userInfo = userInfoService.getByUserId(userId);
        if(userId==null|| UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }

        ForumArticleQuery articleQuery = new ForumArticleQuery();
        articleQuery.setOrderBy("post_time desc");
        articleQuery.setPageNo(pageNo);
        if(type==0){
            articleQuery.setUserId(userId);
        }else if(type==1){
            articleQuery.setCommentUserId(userId);
        }else if(type==2){
            articleQuery.setLikeUserId(userId);
        }
        SessionWebUserDto userDto = getUserInfo4Session(session);
        if(userDto!=null){
            articleQuery.setCurrentUserId(userDto.getUserId());
        }else {
            articleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }

        PaginationResultVO resultVO = forumArticleService.findListByPage(articleQuery);
        return getSuccessResponseVO(convert2PaginationVO(resultVO, ForumArticleVO.class));
    }

    //修改用户信息
    @RequestMapping("/updateUserInfo")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO updateUserInfo(HttpSession session, Integer sex, @VerifyParam(max=100) String personDescription, MultipartFile avatar){
        SessionWebUserDto userDto = getUserInfo4Session(session);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userDto.getUserId());
        userInfo.setSex(sex);
        userInfo.setPersonDescription(personDescription);
        userInfoService.updateUserInfo(userInfo,avatar);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadUserIntegralRecord")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadUserIntegralRecord(HttpSession session,Integer pageNo,String createTimeStart,String createTimeEnd){
        UserIntegralRecordQuery recordQuery = new UserIntegralRecordQuery();
        recordQuery.setUserId(getUserInfo4Session(session).getUserId());
        recordQuery.setPageNo(pageNo);
        recordQuery.setCreateTimeStart(createTimeStart);
        recordQuery.setCreateTimeEnd(createTimeEnd);
        recordQuery.setOrderBy("record_id desc");
        PaginationResultVO resultVO = userIntegralRecordService.findListByPage(recordQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getMessageCount")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getMessageCount(HttpSession session){
        SessionWebUserDto userDto = getUserInfo4Session(session);
        return getSuccessResponseVO(userMessageService.getUserMessageCount(userDto.getUserId()));
    }

    @RequestMapping("/loadMessageList")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadMessageList(HttpSession session,@VerifyParam(required = true) String code,Integer pageNo){
        MessageTypeEnum typeEnum = MessageTypeEnum.getByCode(code);
        if(typeEnum==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        SessionWebUserDto userDto = getUserInfo4Session(session);

        UserMessageQuery query = new UserMessageQuery();
        query.setPageNo(pageNo);
        query.setReceivedUserId(userDto.getUserId());
        query.setMessageType(typeEnum.getType());
        query.setOrderBy("message_id desc");
        PaginationResultVO resultVO = userMessageService.findListByPage(query);
        //更新状态，将当前标记为已读
        if(pageNo==null||pageNo==1){
            userMessageService.readMessageByType(userDto.getUserId(),typeEnum.getType());
        }
        return getSuccessResponseVO(resultVO);
    }

}

