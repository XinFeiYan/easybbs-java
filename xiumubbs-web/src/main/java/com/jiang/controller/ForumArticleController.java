package com.jiang.controller;

import com.jiang.Enums.*;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.CopyTools;
import com.jiang.Utils.RedisUtil;
import com.jiang.Utils.StringTools;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.config.WebConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.*;
import com.jiang.entity.query.ForumArticleAttachmentQuery;
import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.vo.*;
import com.jiang.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    private static final Logger log = LoggerFactory.getLogger(ForumArticleController.class);
    @Resource
    private ForumArticleService forumArticleService;
    @Resource
    private ForumArticleAttachmentService attachmentService;

    @Resource
    private LikeRecordService likeRecordService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ForumArticleAttachmentDownloadService forumArticleAttachmentDownloadService;

    @Resource
    private ForumArticleAttachmentService forumArticleAttachmentService;
    @Resource
    private WebConfig webConfig;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ForumBoardService forumBoardService;
    @RequestMapping("/loadArticle")
    public ResponseVO loadArticle(HttpSession session,Integer boardId,Integer pBoardId,Integer orderType,Integer pageNo){
        //添加排序信息
        ForumArticleQuery articleQuery = new ForumArticleQuery();
        //如果为null,就是查询全部，板块id
        articleQuery.setBoardId(boardId==0?null:boardId);
        //父板块id
        articleQuery.setPBoardId(pBoardId);
        articleQuery.setPageNo(pageNo);

        //未登录的只能看以审核的
        SessionWebUserDto userDto = getUserInfo4Session(session);
        if(userDto!=null){
            //登录可看自己的未审核
            articleQuery.setCurrentUserId(userDto.getUserId());
        }else{
            articleQuery.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }
        ArticleOrderTypeEnum orderTypeEnum = ArticleOrderTypeEnum.getByType(orderType);
        orderTypeEnum = orderTypeEnum==null?ArticleOrderTypeEnum.HOT:orderTypeEnum;
        articleQuery.setOrderBy(orderTypeEnum.getOrderSql());

        PaginationResultVO resultVO = forumArticleService.findListByPage(articleQuery);
        //将一个对象里面的集合转到另一个对象，必须要有同一参数名
        PaginationResultVO resultVO1 = convert2PaginationVO(resultVO, ForumArticleVO.class);
        //将里面每一个都放入redis
        List<ForumArticleVO> articleVOList = resultVO1.getList();
        for(ForumArticleVO forumArticleVO:articleVOList){
            forumArticleService.updateArticleLikeCount(forumArticleVO);
        }

        return getSuccessResponseVO(resultVO1);
    }

    @RequestMapping("/getArticleDetail")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO getArticleDetail(HttpSession session,@VerifyParam(required = true) String articleId){
        SessionWebUserDto sessionWebUserDto = getUserInfo4Session(session);
        ForumArticle forumArticle = this.forumArticleService.readArticle(articleId);
        //因为条件太长，设置一个。可以看文章条件，登录并且是自己发的文章或者是管理员
        Boolean canShowNoAudit = sessionWebUserDto!=null&&(sessionWebUserDto.getUserId().equals(forumArticle.getUserId())||sessionWebUserDto.getAdmin());
        if(forumArticle==null||
                (ArticleStatusEnum.NO_AUDIT.equals(forumArticle.getStatus())&&!canShowNoAudit) ||
                ArticleStatusEnum.DEL.getStatus().equals(forumArticle.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        //详情页VO
        ForumArticleDetailVO detailVO = new ForumArticleDetailVO();
        //将文章信息复制
        detailVO.setForumArticle(CopyTools.copy(forumArticle, ForumArticleVO.class));

        //有附件
        if(forumArticle.getAttachmentType()== Constants.ONE){
            ForumArticleAttachmentQuery articleAttachmentQuery = new ForumArticleAttachmentQuery();
            articleAttachmentQuery.setArticleId(articleId);
            //附件列表
            List<ForumArticleAttachment> forumArticleAttachmentList = attachmentService.findListByParam(articleAttachmentQuery);
            if(forumArticleAttachmentList!=null){
                detailVO.setAttachment(CopyTools.copy(forumArticleAttachmentList.get(0), ForumArticleAttachmentVO.class));
            }
        }
        //判读是否点赞
        if(sessionWebUserDto!=null){
            LikeRecord likeRecord =likeRecordService.getByObjectIdAndUserIdAndOpType(articleId,sessionWebUserDto.getUserId(), OperRecordOpTypeEnum.ARTICLE_LIKE.getType());
            if(likeRecord!=null){
                detailVO.setHaveLike(true);
            }
        }
        //从redis中更新点赞数
        forumArticleService.updateArticleLikeCount(detailVO.getForumArticle());
        return getSuccessResponseVO(detailVO);
    }

    @RequestMapping("/doLike")
    @GlobalInterceptor(checkLogin= true,checkParams = true)
    public ResponseVO doLike(HttpSession session,@VerifyParam(required = true)String articleId){
        SessionWebUserDto sessionWebUserDto = getUserInfo4Session(session);
        likeRecordService.doLike(articleId,sessionWebUserDto.getUserId(),sessionWebUserDto.getNickname(),OperRecordOpTypeEnum.ARTICLE_LIKE);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/getUserDownloadInfo")
    @GlobalInterceptor(checkLogin= true,checkParams = true)
    public ResponseVO getUserDownloadInfo(HttpSession session,@VerifyParam(required = true)String fileId){
        SessionWebUserDto sessionWebUserDto = getUserInfo4Session(session);
        UserInfo userInfo = userInfoService.getByUserId(sessionWebUserDto.getUserId());

        UserDownloadInfoVo userDownloadInfoVo = new UserDownloadInfoVo();
        userDownloadInfoVo.setUserIntegral(userInfo.getCurrentIntegral());

        //看附件是否下载过
        ForumArticleAttachmentDownload attachDownload = forumArticleAttachmentDownloadService.getByFileIdAndUserId(fileId, userInfo.getUserId());
        if(attachDownload!=null){
            userDownloadInfoVo.setHaveDownload(true);
        }
        return getSuccessResponseVO(userDownloadInfoVo);
    }

    //真实下载方法
    @RequestMapping("/attachmentDownload")
    @Transactional(rollbackFor = Exception.class)
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO attachmentDownLoad(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                         @VerifyParam(required = true) String fileId) {
        SessionWebUserDto userDto = getUserInfo4Session(session);
        ForumArticleAttachment forumArticleAttachment = forumArticleAttachmentService.attachmentDownLoad(fileId, userDto);

        //文件流下载,获得文件路径
        InputStream in = null;
        OutputStream out = null;
        String downloadFileName = forumArticleAttachment.getFileName();
        String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT + forumArticleAttachment.getFilePath();
        File file = new File(filePath);
        try {
            //文件输入流
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
            log.error("下载异常", e);
            throw new BusinessException("下载失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                log.error("IO异常", e);
            }
            try {
                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                log.error("IO异常", e);
            }
        }
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadBoard4Post")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadBoard4Post(HttpSession session){
        SessionWebUserDto userDto = getUserInfo4Session(session);
        //查询发表板块
        Integer postType=null;
        if(!userDto.getAdmin()){
            postType = Constants.ONE;
        }
        List<ForumBoard> list = forumBoardService.getBoardTree(postType);
        return getSuccessResponseVO(list);
    }

    @RequestMapping("/postArticle")
    @GlobalInterceptor(checkLogin = true,checkParams = true,frequencyType =UserOperFrequencyTypeEnum.POST_ARTICLE)
    public ResponseVO postArticle(HttpSession session,
                                  MultipartFile cover,
                                  MultipartFile attachment,
                                  Integer integral,
                                  @VerifyParam(required=true,max=150) String title,
                                  @VerifyParam(required=true) Integer pBoardId,
                                  Integer boardId,
                                  @VerifyParam(max=200)String summary,
                                  @VerifyParam(required=true)Integer editorType,
                                  @VerifyParam(required=true)String content,
                                  String markdownContent){
        title = StringTools.eecpapeHtml(title);
        SessionWebUserDto webUserDto = getUserInfo4Session(session);

        ForumArticle forumArticle = new ForumArticle();
        forumArticle.setBoardId(boardId);
        forumArticle.setPBoardId(pBoardId);
        forumArticle.setTitle(title);
        forumArticle.setSummary(summary);
        forumArticle.setContent(content);
        //如果是md
        if(EditorTypeEnum.MARKDOWN.getType().equals(editorType)&&StringTools.isEmpty(markdownContent)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        forumArticle.setMarkdownContent(markdownContent);
        forumArticle.setEditorType(editorType);
        forumArticle.setUserId(webUserDto.getUserId());
        forumArticle.setNickName(webUserDto.getNickname());
        forumArticle.setUserIpAddress(webUserDto.getProvince());

        //附件信息
        ForumArticleAttachment forumArticleAttachment = new ForumArticleAttachment();
        forumArticleAttachment.setIntegral(integral==null?0:integral);

        forumArticleService.postArticle(webUserDto.getAdmin(),forumArticle,forumArticleAttachment,cover,attachment);

        return getSuccessResponseVO(forumArticle.getArticleId());
    }

    //修改文章
    @RequestMapping("/articleDetail4Update")
    @GlobalInterceptor(checkLogin = true,checkParams = true)
    public ResponseVO articleDetail4Update(HttpSession session,@VerifyParam(required = true) String articleId){
        SessionWebUserDto userDto = getUserInfo4Session(session);
        ForumArticle forumArticle = forumArticleService.getByArticleId(articleId);

        if(forumArticle==null||!forumArticle.getUserId().equals(userDto.getUserId())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        ForumArticleDetailVO detailVO = new ForumArticleDetailVO();
        detailVO.setForumArticle(CopyTools.copy(forumArticle,ForumArticleVO.class));
        //如果有附件
        if(forumArticle.getAttachmentType()==Constants.ONE){
            ForumArticleAttachmentQuery attachmentQuery = new ForumArticleAttachmentQuery();
            attachmentQuery.setArticleId(articleId);

            List<ForumArticleAttachment> forumArticleAttachmentList = attachmentService.findListByParam(attachmentQuery);
            if(forumArticleAttachmentList!=null){
                detailVO.setAttachment(CopyTools.copy(forumArticleAttachmentList.get(0), ForumArticleAttachmentVO.class));
            }
        }
        return getSuccessResponseVO(detailVO);
    }

    @RequestMapping("/updateArticle")
    @GlobalInterceptor(checkLogin = true,checkParams = true)
    public ResponseVO updateArticle(HttpSession session,
                                  MultipartFile cover,
                                  MultipartFile attachment,
                                  Integer integral,
                                  @VerifyParam(required=true,max=150)String articleId,
                                  @VerifyParam(required=true,max=150) String title,
                                  @VerifyParam(required=true) Integer pBoardId,
                                  Integer boardId,
                                  @VerifyParam(max=200)String summary,
                                  @VerifyParam(required=true)Integer editorType,
                                  @VerifyParam(required=true)String content,
                                  String markdownContent,
                                  @VerifyParam(required=true) Integer attachmentType){
        title = StringTools.eecpapeHtml(title);
        SessionWebUserDto userDto = getUserInfo4Session(session);

        ForumArticle forumArticle = new ForumArticle();
        forumArticle.setArticleId(articleId);
        forumArticle.setPBoardId(pBoardId);
        forumArticle.setBoardId(boardId);
        forumArticle.setTitle(title);
        forumArticle.setContent(content);
        forumArticle.setMarkdownContent(markdownContent);
        forumArticle.setEditorType(editorType);
        forumArticle.setSummary(summary);
        forumArticle.setUserIpAddress(userDto.getProvince());
        forumArticle.setAttachmentType(attachmentType);
        forumArticle.setUserId(userDto.getUserId());
        //附件信息
        ForumArticleAttachment forumArticleAttachment = new ForumArticleAttachment();
        forumArticleAttachment.setIntegral(integral==null ? 0:integral);

        forumArticleService.updateArticle(userDto.getAdmin(),forumArticle,forumArticleAttachment,cover,attachment);

        return getSuccessResponseVO(forumArticle.getArticleId());
    }

    //修改文章
    @RequestMapping("/search")
    @GlobalInterceptor(checkLogin = true,checkParams = true)
    public ResponseVO search(@VerifyParam(required = true,min = 3) String keyword){
       ForumArticleQuery query = new ForumArticleQuery();
       query.setTitleFuzzy(keyword);
       PaginationResultVO resultVO = forumArticleService.findListByPage(query);
        return getSuccessResponseVO(resultVO);
    }

















}
