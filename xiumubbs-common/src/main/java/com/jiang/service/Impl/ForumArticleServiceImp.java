package com.jiang.service.Impl;


import com.jiang.Enums.*;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.*;
import com.jiang.entity.config.AppConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.FileUploadDto;
import com.jiang.entity.dto.SysSetting4AuditDto;
import com.jiang.entity.dto.SysSettingDto;
import com.jiang.entity.po.*;

import com.jiang.entity.query.ForumArticleAttachmentQuery;
import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.entity.vo.ForumArticleVO;
import com.jiang.mapper.ForumArticleAttachmentDao;
import com.jiang.service.ForumArticleService;
import com.jiang.mapper.ForumArticleDao;
import com.jiang.entity.vo.PaginationResultVO;

import com.jiang.service.ForumBoardService;
import com.jiang.service.UserInfoService;
import com.jiang.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description(描述):ForumArticleServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("forumArticleService")
public class ForumArticleServiceImp implements ForumArticleService {


	@Resource
	private ForumArticleDao<ForumArticle,ForumArticleQuery> forumArticleDao;

	@Resource
	private ForumBoardService forumBoardService;

	@Resource
	private FileUtils fileUtils;

	@Resource
	private AppConfig appConfig;
	@Resource
	private ForumArticleAttachmentDao<ForumArticleAttachment, ForumArticleAttachmentQuery> forumArticleAttachmentDao;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private ImageUtils imageUtils;

	@Autowired
	private RedisTemplate redisTemplate;

	//加一个野指针，避免循环依赖
	@Lazy
	@Resource
	private ForumArticleService forumArticleService;

	@Resource
	private UserMessageService userMessageService;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<ForumArticle> findListByParam(ForumArticleQuery query){
		return this.forumArticleDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(ForumArticleQuery query){
		return this.forumArticleDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<ForumArticle> list=this.findListByParam(query);
		PaginationResultVO<ForumArticle> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(ForumArticle bean){
		return this.forumArticleDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<ForumArticle> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumArticleDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<ForumArticle> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumArticleDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据ArticleId查询
	 */
	@Override
	public ForumArticle getByArticleId(String articleId){
		return this.forumArticleDao.selectByArticleId(articleId);
	}

	/**
     * @Description(描述):根据ArticleId更新
	 */
	@Override
	public Integer updateByArticleId(ForumArticle bean, String articleId){
		return this.forumArticleDao.updateByArticleId(bean,articleId);
	}


	/**
     * @Description(描述):根据ArticleId删除
	 */
	@Override
	public Integer deleteByArticleId(String articleId){
		return this.forumArticleDao.deleteByArticleId(articleId);
	}

	@Override
	public ForumArticle readArticle(String articleId){
		ForumArticle forumArticle = this.forumArticleDao.selectByArticleId(articleId);
		if(forumArticle==null){
			throw new BusinessException(ResponseCodeEnum.CODE_404);
		}
		if(ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())){
			//更新点赞数量
			this.forumArticleDao.updateArticleCount(UpdateArticleCountTypeEnum.READ_COUNT.getType(), Constants.ONE,articleId);
		}
		return forumArticle;
	}

	//发表文章
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void postArticle(Boolean isAdmin, ForumArticle article, ForumArticleAttachment articleAttachment, MultipartFile cover, MultipartFile attachment) {

		LocalDateTime curTime = LocalDateTime.now();
		String articleId = StringTools.getRandomString(Constants.LENGTH_15);
		article.setArticleId(articleId);
		article.setPostTime(curTime);
		article.setLastUpdateTime(curTime);

		if(cover!=null){
			FileUploadDto fileUploadDto = fileUtils.uploadFile2local(cover,Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
			article.setCover(fileUploadDto.getLocalPath());
		}

		if(attachment!=null){
			uploadAttachment(article,articleAttachment,attachment,false);
			article.setAttachmentType(ArticleAttachmentTypeEnum.HAVE_ATTACHMENT.getType());
		}else{
			article.setAttachmentType(ArticleAttachmentTypeEnum.NO_ATTACHMENT.getType());
		}

		//文章审核
		if(isAdmin){
			article.setStatus(ArticleStatusEnum.AUDIT.getStatus());
		}else{
			SysSetting4AuditDto auditDto = SysCacheUtils.getSysSetting().getAuditSetting();
			article.setStatus(auditDto.getPostAudit() ? ArticleStatusEnum.NO_AUDIT.getStatus() : ArticleStatusEnum.AUDIT.getStatus());
		}

		//替换图片，将临时图片复制到图片文件夹中
		String content = article.getContent();
		if(!StringTools.isEmpty(content)){
			String month = imageUtils.resetImageHtml(content);
			String replaceMonth = month+"/";
			content = content.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
			article.setContent(content);
			String markdownContent = article.getMarkdownContent();
			if(!StringTools.isEmpty(markdownContent)){
				markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
				article.setMarkdownContent(markdownContent);
			}
		}

		this.forumArticleDao.insert(article);

		//增加积分
		Integer postIntegral = SysCacheUtils.getSysSetting().getPostSetting().getPostIntegral();
		if(postIntegral>0&&ArticleStatusEnum.AUDIT.getStatus().equals(article.getStatus())){
			userInfoService.updateUserIntegral(article.getUserId(),UserIntegralOperTypeEnum.POST_ARTICLE,UserIntegralChangeTypeEnum.ADD.getChangeType(),postIntegral);
		}
	}
	//更新文章
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateArticle(Boolean isAdmin, ForumArticle article, ForumArticleAttachment articleAttachment, MultipartFile cover, MultipartFile attachment) {
		ForumArticle dbInfo = forumArticleDao.selectByArticleId(article.getArticleId());
		if(!isAdmin && !dbInfo.getUserId().equals(article.getUserId())){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		article.setLastUpdateTime(LocalDateTime.now());
		resetBoardInfo(isAdmin,article);

		if(cover!=null){
			FileUploadDto fileUploadDto = fileUtils.uploadFile2local(cover,Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
			article.setCover(fileUploadDto.getLocalPath());
		}
		if(attachment!=null){
			uploadAttachment(article,articleAttachment,attachment,true);
			article.setAttachmentType(ArticleAttachmentTypeEnum.HAVE_ATTACHMENT.getType());
		}

		ForumArticleAttachment attachmentInfo = null;
		ForumArticleAttachmentQuery attachmentQuery = new ForumArticleAttachmentQuery();
		attachmentQuery.setArticleId(article.getArticleId());
		List<ForumArticleAttachment> articleAttachmentList = this.forumArticleAttachmentDao.selectList(attachmentQuery);
		if(!articleAttachmentList.isEmpty()){
			attachmentInfo = articleAttachmentList.get(0);
		}
		//不上传附件,删掉原来的附件
		if(attachmentInfo!=null){
			if(article.getAttachmentType()==Constants.ZERO){
				new File(appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_ATTACHMENT+attachmentInfo.getFilePath()).delete();
				this.forumArticleAttachmentDao.deleteByFileId(attachmentInfo.getFileId());
			}else{
				//积分变得就更新
				if(!attachmentInfo.getIntegral().equals(articleAttachment.getIntegral())){
					ForumArticleAttachment updateIntegral= new ForumArticleAttachment();
					updateIntegral.setIntegral(articleAttachment.getIntegral());
					this.forumArticleAttachmentDao.updateByFileId(updateIntegral,attachmentInfo.getFileId());
				}
			}
		}

		//文章是否审核
		if(isAdmin){
			article.setStatus(ArticleStatusEnum.AUDIT.getStatus());
		}else{
			SysSetting4AuditDto auditDto = SysCacheUtils.getSysSetting().getAuditSetting();
			article.setStatus(auditDto.getPostAudit()?ArticleStatusEnum.NO_AUDIT.getStatus():ArticleStatusEnum.AUDIT.getStatus());
		}

		//替换图片
		String content = article.getContent();
		if(!StringTools.isEmpty(content)){
			String month = imageUtils.resetImageHtml(content);
			String replaceMonth = month+"/";
			content = content.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
			article.setContent(content);
			String markdownContent = article.getMarkdownContent();
			if(!StringTools.isEmpty(markdownContent)){
				markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
				article.setMarkdownContent(markdownContent);
			}
		}

		this.forumArticleDao.updateByArticleId(article,article.getArticleId());

	}
	//板块校验，管理员才做板块校验
	public void resetBoardInfo(Boolean isAdmin,ForumArticle forumArticle){
		ForumBoard pBoard = forumBoardService.getByBoardId(forumArticle.getPBoardId());
		if(pBoard==null||pBoard.getPostType()==Constants.ZERO&&!isAdmin){
			throw new BusinessException("一级板块不存在");
		}
		forumArticle.setPBoardName(pBoard.getBoardName());

		//0表示没有二级板块
		if(forumArticle.getBoardId()!=null&&forumArticle.getBoardId()!=0){
			ForumBoard board = forumBoardService.getByBoardId(forumArticle.getBoardId());
			if(board==null||board.getPostType()==Constants.ZERO&&!isAdmin){
				throw new BusinessException("二级板块不存在");
			}
			forumArticle.setBoardName(board.getBoardName());
		}else{
			forumArticle.setBoardId(0);
			forumArticle.setBoardName("");
		}
	}

	public void uploadAttachment(ForumArticle article,ForumArticleAttachment articleAttachment,MultipartFile file,Boolean isUpdate){
		Integer allowSizeMb = SysCacheUtils.getSysSetting().getPostSetting().getAttachmentSize();
		long allowSize = allowSizeMb*Constants.FILE_SIZE_1M;
		if(file.getSize()>allowSize){
			throw new BusinessException("文件最大上传"+allowSize+"M");
		}

		ForumArticleAttachment dbInfo = null;
		//true表示重新上传附件
		if(isUpdate){
			ForumArticleAttachmentQuery attachmentQuery = new ForumArticleAttachmentQuery();
			attachmentQuery.setArticleId(article.getArticleId());
			List<ForumArticleAttachment> articleAttachmentList = this.forumArticleAttachmentDao.selectList(attachmentQuery);
			if(!articleAttachmentList.isEmpty()){
				dbInfo = articleAttachmentList.get(0);
				//删除本地附件
				new File(appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_ATTACHMENT+dbInfo.getFilePath()).delete();
			}
		}

		//上传附件
		FileUploadDto fileUploadDto = fileUtils.uploadFile2local(file,Constants.FILE_FOLDER_ATTACHMENT,FileUploadTypeEnum.ARTICLE_ATTACHMENT);

		if(dbInfo==null){
			articleAttachment.setFileId(StringTools.getRandomString(Constants.LENGTH_15));
			articleAttachment.setArticleId(article.getArticleId());
			articleAttachment.setFileName(fileUploadDto.getOriginalFileName());
			articleAttachment.setArticleId(article.getArticleId());
			articleAttachment.setFilePath(fileUploadDto.getLocalPath());
			articleAttachment.setFileSize(file.getSize());
			articleAttachment.setDownloadCount(Constants.ZERO);
			articleAttachment.setFileType(AttachmentFileTypeEnum.ZIP.getType());
			articleAttachment.setUserId(article.getUserId());
			forumArticleAttachmentDao.insert(articleAttachment);
		}else{
			//更新文件信息
			ForumArticleAttachment updateInfo = new ForumArticleAttachment();
			updateInfo.setFileName(fileUploadDto.getOriginalFileName());
			updateInfo.setFileSize(file.getSize());
			updateInfo.setFilePath(fileUploadDto.getLocalPath());
			forumArticleAttachmentDao.updateByFileId(updateInfo,dbInfo.getFileId());
		}
	}

	@Override
	public void delArticle(String articleIds) {
		String[] articleIdArray = articleIds.split(",");
		for(String articleId:articleIdArray){
			forumArticleService.delArticleSignle(articleId);
		}
	}

	//文章删除具体操作
	@Transactional(rollbackFor = Exception.class)
	public void delArticleSignle(String articleId){
		ForumArticle article = getByArticleId(articleId);
		if(article==null||ArticleStatusEnum.DEL.getStatus().equals(article.getStatus())){
			return;
		}
		ForumArticle updateInfo  = new ForumArticle();
		updateInfo.setStatus(ArticleStatusEnum.DEL.getStatus());
		forumArticleDao.updateByArticleId(updateInfo,articleId);

		Integer integral = SysCacheUtils.getSysSetting().getPostSetting().getPostIntegral();
		if(integral>0&&ArticleStatusEnum.AUDIT.getStatus().equals(article.getStatus())){
			userInfoService.updateUserIntegral(article.getUserId(),UserIntegralOperTypeEnum.DEL_ARTICLE,UserIntegralChangeTypeEnum.REDUCE.getChangeType(),integral);
		}
		UserMessage userMessage = new UserMessage();
		userMessage.setReceivedUserId(article.getUserId());
		userMessage.setMessageType(MessageTypeEnum.SYS.getType());
		userMessage.setCreateTime(LocalDateTime.now());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		userMessage.setMessageContent("文章《"+article.getTitle()+"》已被管理员删掉");
		userMessageService.add(userMessage);

	}


	public void updateBoard(String articleId,Integer pBoardId,Integer boardId){
		ForumArticle forumArticle = new ForumArticle();
		forumArticle.setBoardId(boardId);
		forumArticle.setPBoardId(pBoardId);

		resetBoardInfo(true,forumArticle);
		forumArticleDao.updateByArticleId(forumArticle,articleId);
	}

	@Override
	public void auditArticle(String articleIds) {
		String[] articleArray = articleIds.split(",");
		for(String article:articleArray){
			forumArticleService.auditArticleSignle(article);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void auditArticleSignle(String articleId) {
		ForumArticle article = getByArticleId(articleId);
		if(article==null || !ArticleStatusEnum.NO_AUDIT.getStatus().equals(article.getStatus())){
			return;
		}
		ForumArticle updateInfo  = new ForumArticle();
		updateInfo.setStatus(ArticleStatusEnum.AUDIT.getStatus());
		forumArticleDao.updateByArticleId(updateInfo,articleId);

		Integer integral = SysCacheUtils.getSysSetting().getPostSetting().getPostIntegral();
		if(integral>0&& ArticleStatusEnum.NO_AUDIT.getStatus().equals(article.getStatus())){
			userInfoService.updateUserIntegral(article.getUserId(),UserIntegralOperTypeEnum.POST_ARTICLE,UserIntegralChangeTypeEnum.ADD.getChangeType(),integral);
		}
	}

	public void updateArticleLikeCount(ForumArticleVO forumArticleVO){
		//更新文章的点赞数,如果redis中有就从redis中获取，没有就更新到redis中
		Object likes = redisTemplate.opsForHash().get(RedisUtil.getArticleLikeCount(),forumArticleVO.getArticleId());
		if(likes==null){
			redisTemplate.opsForHash().put(RedisUtil.getArticleLikeCount(),forumArticleVO.getArticleId(),forumArticleVO.getGoodCount());
		}else{
			forumArticleVO.setGoodCount(Integer.parseInt(likes.toString()));
		}
	}

}