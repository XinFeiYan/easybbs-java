package com.jiang.controller;

import com.jiang.Exception.BusinessException;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.config.AdminConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.ForumArticle;
import com.jiang.entity.po.ForumArticleAttachment;
import com.jiang.entity.po.ForumComment;
import com.jiang.entity.query.ForumArticleAttachmentQuery;
import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.query.ForumCommentQuery;
import com.jiang.entity.vo.PaginationResultVO;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.service.ForumArticleAttachmentService;
import com.jiang.service.ForumArticleService;
import com.jiang.service.ForumCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumArticleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);

    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private ForumArticleAttachmentService forumArticleAttachmentService;

    @Resource
    private AdminConfig adminConfig;

    @Resource
    private ForumCommentService forumCommentService;
    @RequestMapping("/loadArticle")
    public ResponseVO loadArticle(ForumArticleQuery articleQuery){
        articleQuery.setOrderBy("post_time desc");
        return getSuccessResponseVO(forumArticleService.findListByPage(articleQuery));
    }

    @RequestMapping("/delArticle")
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO delArticle(@VerifyParam(required = true) String articleIds){
         forumArticleService.delArticle(articleIds);
        return getSuccessResponseVO(null);
    }
    //更新板块
    @RequestMapping("/updateBoard")
    public ResponseVO updateBoard(@VerifyParam(required = true)String articleId,
                                  @VerifyParam(required = true)Integer pBoardId,
                                  @VerifyParam(required = true)Integer boardId){

        boardId = boardId==null?0:boardId;

        forumArticleService.updateBoard(articleId,pBoardId,boardId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/getAttachment")
    public ResponseVO getAttachment(@VerifyParam(required = true)String articleId){

        ForumArticleAttachmentQuery  attachmentQuery = new ForumArticleAttachmentQuery();
        attachmentQuery.setArticleId(articleId);
        List<ForumArticleAttachment> attachmentList = forumArticleAttachmentService.findListByParam(attachmentQuery);
        if(attachmentList.isEmpty()){
            throw new BusinessException("附件不存在");
        }
        return getSuccessResponseVO(null);
    }

    //真实下载方法
    @RequestMapping("/attachmentDownload")
    @Transactional(rollbackFor = Exception.class)
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO attachmentDownLoad(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                         @VerifyParam(required = true) String fileId) {
        SessionWebUserDto userDto = getUserInfo4Session(session);
        ForumArticleAttachment forumArticleAttachment = forumArticleAttachmentService.attachmentDownLoad(fileId, userDto);

        //文件流下载
        InputStream in = null;
        OutputStream out = null;
        String downloadFileName = forumArticleAttachment.getFileName();
        String filePath = adminConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + forumArticleAttachment.getFilePath();
        File file = new File(filePath);
        try {
            in = new FileInputStream(file);
            out = response.getOutputStream();
            response.setContentType("application/x-msdownload; charset=UTF-8");
            // 解决中文文件名乱码问题
            if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0) {//IE浏览器
                downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
            } else {
                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
            }
            //输出下载名称
            response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadFileName + "\"");
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len); // write
            }
            out.flush();
        } catch (Exception e) {
            logger.error("下载异常", e);
            throw new BusinessException("下载失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                logger.error("IO异常", e);
            }
            try {
                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                logger.error("IO异常", e);
            }
        }
        return getSuccessResponseVO(null);
    }

    //置顶
    @RequestMapping("/topArticle")
    public ResponseVO topArticle(@VerifyParam(required = true)String articleId,Integer topType){

        ForumArticle forumArticle = new ForumArticle();
        forumArticle.setTopType(topType);
        forumArticleService.updateByArticleId(forumArticle,articleId);
        return getSuccessResponseVO(null);
    }

    //文章审核
    @RequestMapping("/auditArticle")
    public ResponseVO auditArticle(@VerifyParam(required = true)String articleIds){
        forumArticleService.auditArticle(articleIds);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadComment")
    public ResponseVO loadComment(ForumCommentQuery forumCommentQuery) {
        forumCommentQuery.setLoadChildren(true);
        forumCommentQuery.setOrderBy("post_time desc");
        PaginationResultVO<ForumComment> resultVO = forumCommentService.findListByPage(forumCommentQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadComment4Article")
    public ResponseVO loadComment4Article(ForumCommentQuery forumCommentQuery) {
        forumCommentQuery.setLoadChildren(true);
        forumCommentQuery.setOrderBy("post_time desc");
        //只查一级评论
        forumCommentQuery.setPCommentId(0);
        return getSuccessResponseVO(forumCommentService.findListByParam(forumCommentQuery));
    }

    //
    @RequestMapping("/delComment")
    public ResponseVO delComment(@VerifyParam(required = true)String commentIds) {
        forumCommentService.delComment(commentIds);
        return getSuccessResponseVO(null);
    }


    @RequestMapping("newLoadComment")
    public ResponseVO newLoadComment(ForumCommentQuery forumCommentQuery){

        return getSuccessResponseVO(forumCommentService.findListByPage(forumCommentQuery));
    }

}
