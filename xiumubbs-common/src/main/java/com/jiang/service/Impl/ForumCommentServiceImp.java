package com.jiang.service.Impl;


import com.jiang.Enums.*;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.FileUtils;
import com.jiang.Utils.StringTools;
import com.jiang.Utils.SysCacheUtils;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.FileUploadDto;
import com.jiang.entity.po.ForumArticle;
import com.jiang.entity.po.UserInfo;
import com.jiang.entity.po.UserMessage;
import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.query.ForumCommentQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.entity.query.UserMessageQuery;
import com.jiang.mapper.ForumArticleDao;
import com.jiang.mapper.UserMessageDao;
import com.jiang.rabbitmq.MqProducer;
import com.jiang.service.ForumCommentService;
import com.jiang.mapper.ForumCommentDao;
import com.jiang.entity.po.ForumComment;
import com.jiang.entity.vo.PaginationResultVO;

import com.jiang.service.UserInfoService;
import com.jiang.service.UserMessageService;
import org.apache.catalina.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description(描述):ForumCommentServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("forumCommentService")
public class ForumCommentServiceImp implements ForumCommentService {


	@Resource
	private ForumCommentDao<ForumComment,ForumCommentQuery> forumCommentDao;
	@Resource
	private ForumArticleDao<ForumArticle, ForumArticleQuery> forumArticleDao;

	@Resource
	private UserInfoService userInfoService;
	@Resource
	private UserMessageService userMessageService;
	@Resource
	private FileUtils fileUtils;

	@Resource
	private MqProducer mqProducer;

	@Lazy
	@Resource
	private ForumCommentService forumCommentService;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<ForumComment> findListByParam(ForumCommentQuery query){
		List<ForumComment> list = this.forumCommentDao.selectList(query);
		//获取二级评论，不为null,并且要开启查询二级评论，传进来就是父id
		if(query.getLoadChildren()!=null&&query.getLoadChildren()){
			ForumCommentQuery subQuery = new ForumCommentQuery();
			subQuery.setArticleId(query.getArticleId());
			//查询每个评论是否点赞，sql语句使用的关联点赞表
			subQuery.setQueryLikeType(query.getQueryLikeType());
			subQuery.setCurrentUserId(query.getCurrentUserId());
			subQuery.setStatus(query.getStatus());

			//使用jdk8表达式,取出所以的父节点,通过map取出父节点，再转化为list集合
			List<Integer> pcommentList = list.stream().map(ForumComment::getCommentId).distinct().collect(Collectors.toList());
			subQuery.setPcommentIdList(pcommentList);
			List<ForumComment> sublist = this.forumCommentDao.selectList(subQuery);
			//将二级评论转化成一个map，按照父ip分组导入
			Map<Integer,List<ForumComment>> tempMap=sublist.stream().collect(Collectors.groupingBy(ForumComment::getPCommentId));
			list.forEach(item->{
				item.setChildren(tempMap.get(item.getCommentId()));
			});

		}
		return list;
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(ForumCommentQuery query){
		return this.forumCommentDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<ForumComment> findListByPage(ForumCommentQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(page);
		List<ForumComment> list=this.findListByParam(query);

		PaginationResultVO<ForumComment> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(ForumComment bean){
		return this.forumCommentDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<ForumComment> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumCommentDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<ForumComment> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumCommentDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据CommentId查询
	 */
	@Override
	public ForumComment getByCommentId(Integer commentId){
		return this.forumCommentDao.selectByCommentId(commentId);
	}

	/**
     * @Description(描述):根据CommentId更新
	 */
	@Override
	public Integer updateByCommentId(ForumComment bean, Integer commentId){
		return this.forumCommentDao.updateByCommentId(bean,commentId);
	}


	/**
     * @Description(描述):根据CommentId删除
	 */
	@Override
	public Integer deleteByCommentId(Integer commentId){
		return this.forumCommentDao.deleteByCommentId(commentId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeTopType(String userId,Integer commentId,Integer topType){
		//评论置顶枚举
		CommentTopTypeEnum commentTopTypeEnum = CommentTopTypeEnum.getByType(topType);
		//查类型
		if(commentTopTypeEnum==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		//查评论
		ForumComment forumComment = forumCommentDao.selectByCommentId(commentId);
		if(forumComment==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		//查文章
		ForumArticle forumArticle = forumArticleDao.selectByArticleId(forumComment.getArticleId());
		if(forumArticle==null){
			throw new BusinessException(ResponseCodeEnum.CODE_500);
		}

		//只有作者能置顶,二级评论不能置顶
		if(!forumArticle.getUserId().equals(userId)||forumComment.getPCommentId()!=0){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}

		//如果是一样的就不用管
		if(forumComment.getTopType().equals(topType)){
			return;
		}

		//如果是要将评论置顶
		if(CommentTopTypeEnum.TOP.getType().equals(topType)){
			//先取消文章的置顶
			forumCommentDao.updateTopTypeByArticle(forumArticle.getArticleId());
		}
		ForumComment updateInfo = new ForumComment();
		updateInfo.setTopType(topType);
		forumCommentDao.updateByCommentId(updateInfo,commentId);
	}

	//发送评论
	@Transactional(rollbackFor = Exception.class)
	public void postComment(ForumComment comment, MultipartFile image){
		//判读文章,或者未审核
		ForumArticle forumArticle = forumArticleDao.selectByArticleId(comment.getArticleId());
		if(forumArticle==null|| !ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())){
			throw new BusinessException("文章不存在");
		}
		//如果是二级评论，看父评论是否存在
		ForumComment pComment = null;
		if(comment.getPCommentId()!=0){
			pComment = forumCommentDao.selectByCommentId(comment.getPCommentId());
			if(pComment==null){
				throw new BusinessException("评论不存在");
			}
		}
		//回复二级评论，就是三级评论（多级评论同理），传入二级评论的id
		if(!StringTools.isEmpty(comment.getReplyNickName())){
			UserInfo userInfo = userInfoService.getByUserId(comment.getReplyUserId());
			if(userInfo == null){
				throw new BusinessException("回复的用户不存在");
			}
			comment.setReplyNickName(userInfo.getNickName());
		}
		comment.setPostTime(LocalDateTime.now());
		if(image!=null){
			FileUploadDto uploadDto = fileUtils.uploadFile2local(image,Constants.FILE_FOLDER_IMAGE,FileUploadTypeEnum.COMMENT_IMAGE);
			//设置本地路径
			comment.setImgPath(uploadDto.getLocalPath());
		}

		Boolean needAudit = SysCacheUtils.getSysSetting().getAuditSetting().getCommentAudit();
		comment.setStatus(needAudit?CommentStatusEnum.NO_AUDIT.getStatus():CommentStatusEnum.AUDIT.getStatus());
		this.forumCommentDao.insert(comment);
		if(needAudit){
			return;
		}
		//不需要审核直接发消息，发送评论消息
		updateCommentInfo(comment,forumArticle,pComment);
	}

	public void updateCommentInfo(ForumComment comment,ForumArticle forumArticle,ForumComment pComment){
		Integer commentIntegral = SysCacheUtils.getSysSetting().getCommentSetting().getCommentIntegral();
		if(commentIntegral>0){
			userInfoService.updateUserIntegral(comment.getUserId(),UserIntegralOperTypeEnum.POST_COMMENT,UserIntegralChangeTypeEnum.ADD.getChangeType(),commentIntegral);
		}

		if(comment.getPCommentId()==0){
			forumArticleDao.updateArticleCount(UpdateArticleCountTypeEnum.COMMENT_COUNT.getType(),Constants.ONE,comment.getArticleId());
		}

		//记录消息
		UserMessage userMessage = new UserMessage();
		userMessage.setMessageType(MessageTypeEnum.COMMENT.getType());
		userMessage.setCreateTime(LocalDateTime.now());
		userMessage.setArticleId(forumArticle.getArticleId());
		userMessage.setArticleTitle(forumArticle.getTitle());
		userMessage.setCommentId(comment.getCommentId());
		userMessage.setSendUserId(comment.getUserId());
		userMessage.setSendNickName(comment.getNickName());
		userMessage.setMessageContent(comment.getContent());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());

		//收件人不一样
		if(comment.getPCommentId()==0){
			userMessage.setReceivedUserId(forumArticle.getUserId());
		}else if(comment.getPCommentId()!=0 && StringTools.isEmpty(comment.getReplyUserId())){
			userMessage.setReceivedUserId(pComment.getUserId());
		}else if(comment.getPCommentId()!=0&&!StringTools.isEmpty(comment.getReplyUserId())){
			userMessage.setReceivedUserId(comment.getReplyUserId());
		}

		//发送人与接收人不一样,发送消息
		if(!comment.getUserId().equals(userMessage.getReceivedUserId())){
			mqProducer.sendReminder(userMessage);
			//userMessageService.add(userMessage);
		}
	}

	@Override
	public void delComment(String commentIds) {
		String[] commentIdArray = commentIds.split(",");
		for(String commentIdStr:commentIdArray){
			Integer commentId = Integer.parseInt(commentIdStr);
			forumCommentService.delCommentSingle(commentId);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delCommentSingle(Integer commentId) {
		ForumComment comment = forumCommentDao.selectByCommentId(commentId);
		if(comment==null||CommentStatusEnum.DEL.getStatus().equals(comment.getStatus())){
			return;
		}
		ForumComment forumComment = new ForumComment();
		forumComment.setStatus(CommentStatusEnum.DEL.getStatus());
		forumCommentDao.updateByCommentId(forumComment,commentId);

		//删除已审核评论
		if(CommentStatusEnum.AUDIT.getStatus().equals(comment.getStatus())){
			//评论数不算二级评论
			if(comment.getPCommentId()==0){
				forumArticleDao.updateArticleCount(UpdateArticleCountTypeEnum.COMMENT_COUNT.getType(),-1,comment.getArticleId());
			}
			Integer integer = SysCacheUtils.getSysSetting().getCommentSetting().getCommentIntegral();
			userInfoService.updateUserIntegral(comment.getUserId(),UserIntegralOperTypeEnum.DEL_COMMENT,UserIntegralChangeTypeEnum.REDUCE.getChangeType(),integer);
		}

		UserMessage userMessage = new UserMessage();
		userMessage.setReceivedUserId(comment.getUserId());
		userMessage.setMessageType(MessageTypeEnum.SYS.getType());
		userMessage.setCreateTime(LocalDateTime.now());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		userMessage.setMessageContent("评论"+comment.getContent()+"被管理员删除");
		mqProducer.sendReminder(userMessage);

	}

	@Override
	public void auditComment(String commentIds) {
		String[] commentIdArray = commentIds.split(",");
		for(String commentIdStr:commentIdArray){
			Integer commentId = Integer.parseInt(commentIdStr);
			forumCommentService.auditCommentSingle(commentId);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void auditCommentSingle(Integer commentId) {

	}
}