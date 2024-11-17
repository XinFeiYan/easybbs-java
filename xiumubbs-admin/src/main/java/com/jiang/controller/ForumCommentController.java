package com.jiang.controller;

import com.jiang.Enums.*;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.StringTools;
import com.jiang.Utils.SysCacheUtils;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.ForumComment;
import com.jiang.entity.po.LikeRecord;
import com.jiang.entity.query.ForumCommentQuery;
import com.jiang.entity.vo.PaginationResultVO;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.service.ForumCommentService;
import com.jiang.service.LikeRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class ForumCommentController extends BaseController {
    @Resource
    private ForumCommentService forumCommentService;

    @Resource
    private LikeRecordService likeRecordService;


    @RequestMapping("/loadComment")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO loadComment(HttpSession session,
                                  @VerifyParam(required = true) String articleId,
                                  Integer pageNo,
                                  Integer orderType){
        //能否查看评论
        if(!SysCacheUtils.getSysSetting().getCommentSetting().getCommentOpen()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        ForumCommentQuery commentQuery = new ForumCommentQuery();
        commentQuery.setArticleId(articleId);
        //评论顺序
        String orderSql = orderType==null||orderType== Constants.ZERO? CommentOrderTypeEnum.HOS.getOrderSql():CommentOrderTypeEnum.NEW.getOrderSql();
        //设置置顶
        commentQuery.setOrderBy("top_type desc,"+orderSql);

        //自己的未审核与自己点赞可见
        SessionWebUserDto userDto = getUserInfo4Session(session);
        if(userDto!=null){
            commentQuery.setQueryLikeType(true);
            //登录可看自己的的
            commentQuery.setCurrentUserId(userDto.getUserId());
        }else{
            commentQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }

        commentQuery.setPageNo(pageNo);
        commentQuery.setPageSize(PageSizeEnum.SIZE50.getSize());
        //先查一级评论
        commentQuery.setPCommentId(0);
        commentQuery.setLoadChildren(true);
        PaginationResultVO resultVO = forumCommentService.findListByPage(commentQuery);
        return getSuccessResponseVO(convert2PaginationVO(resultVO, ForumComment.class));
    }

    //评论点赞
    @RequestMapping("/doLike")
    @GlobalInterceptor(checkParams = true,checkLogin = true,frequencyType = UserOperFrequencyTypeEnum.DO_LIKE)
    public ResponseVO doLike(HttpSession session,
                                  @VerifyParam(required = true) Integer commentId){
        SessionWebUserDto userDto= getUserInfo4Session(session);
        String objectId = String.valueOf(commentId);

        likeRecordService.doLike(objectId,userDto.getUserId(),userDto.getNickname(), OperRecordOpTypeEnum.COMMENT_LIKE);

        //返回点赞信息
        LikeRecord likeRecord =
                likeRecordService.getByObjectIdAndUserIdAndOpType(objectId,userDto.getUserId(),OperRecordOpTypeEnum.COMMENT_LIKE.getType());
        ForumComment comment = forumCommentService.getByCommentId(commentId);
        comment.setLikeType(likeRecord==null?0:1);
        return getSuccessResponseVO(comment);
    }

    //评论置顶
    @RequestMapping("/changeTopType")
    @GlobalInterceptor(checkParams = true,checkLogin = true)
    public ResponseVO changeTopType(HttpSession session,
                                    @VerifyParam(required = true) Integer commentId,Integer topType){

        forumCommentService.changeTopType(getUserInfo4Session(session).getUserId(),commentId,topType);
        return getSuccessResponseVO(null);

    }


    @RequestMapping("/postComment")
    @GlobalInterceptor(checkParams = true,checkLogin = true,frequencyType = UserOperFrequencyTypeEnum.POST_COMMENT)
    public ResponseVO postComment(HttpSession session,
                                  @VerifyParam(required = true) String articleId,
                                  @VerifyParam(required=true) Integer pCommentId,
                                  @VerifyParam(min=5,max=800) String content,
                                  MultipartFile image,
                                  String replyUserId){

        //要开启评论
        if(!SysCacheUtils.getSysSetting().getCommentSetting().getCommentOpen()){
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
        if(image==null&& StringTools.isEmpty(content)){
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
        SessionWebUserDto userDto = getUserInfo4Session(session);

        content = StringTools.eecpapeHtml(content);
        ForumComment comment = new ForumComment();
        comment.setUserId(userDto.getUserId());
        comment.setNickName(userDto.getNickname());
        comment.setUserIpAddress(userDto.getProvince());
        comment.setPCommentId(pCommentId);
        comment.setArticleId(articleId);
        comment.setContent(content);
        comment.setReplyUserId(replyUserId);//二级回复的人
        comment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

        forumCommentService.postComment(comment,image);
        //回复二级评论，返回全部当前下面的全部评论
        if(pCommentId!=0){
            ForumCommentQuery forumCommentQuery = new ForumCommentQuery();
            forumCommentQuery.setArticleId(articleId);
            forumCommentQuery.setPCommentId(pCommentId);
            forumCommentQuery.setOrderBy("comment_id asc");
            List<ForumComment> children = forumCommentService.findListByParam(forumCommentQuery);
            return getSuccessResponseVO(children);
        }
        return getSuccessResponseVO(comment);

    }

























}
