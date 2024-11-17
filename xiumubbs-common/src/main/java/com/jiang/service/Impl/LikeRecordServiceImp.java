package com.jiang.service.Impl;


import com.jiang.Enums.*;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.RedisUtil;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.po.ForumArticle;
import com.jiang.entity.po.ForumComment;
import com.jiang.entity.po.UserMessage;
import com.jiang.entity.query.*;
import com.jiang.mapper.ForumArticleDao;
import com.jiang.mapper.ForumCommentDao;
import com.jiang.mapper.UserMessageDao;
import com.jiang.rabbitmq.MqProducer;
import com.jiang.service.ForumArticleService;
import com.jiang.service.LikeRecordService;
import com.jiang.mapper.LikeRecordDao;
import com.jiang.entity.po.LikeRecord;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.scanner.Constant;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description(描述):LikeRecordServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("likeRecordService")
public class LikeRecordServiceImp implements LikeRecordService {


	@Resource
	private LikeRecordDao<LikeRecord,LikeRecordQuery> likeRecordDao;
	@Resource
	private UserMessageDao<UserMessage, UserMessageQuery> userMessageDao;

	@Resource
	private ForumArticleDao<ForumArticle, ForumArticleQuery> forumArticleDao;
	@Resource
	private ForumCommentDao<ForumComment, ForumCommentQuery> forumCommentDao;

	@Resource
	private MqProducer mqProducer;

	@Autowired
	private RedisTemplate redisTemplate;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<LikeRecord> findListByParam(LikeRecordQuery query){
		return this.likeRecordDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(LikeRecordQuery query){
		return this.likeRecordDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<LikeRecord> findListByPage(LikeRecordQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<LikeRecord> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<LikeRecord> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(LikeRecord bean){
		return this.likeRecordDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<LikeRecord> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.likeRecordDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<LikeRecord> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.likeRecordDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据OpId查询
	 */
	@Override
	public LikeRecord getByOpId(Integer opId){
		return this.likeRecordDao.selectByOpId(opId);
	}

	/**
     * @Description(描述):根据OpId更新
	 */
	@Override
	public Integer updateByOpId(LikeRecord bean, Integer opId){
		return this.likeRecordDao.updateByOpId(bean,opId);
	}


	/**
     * @Description(描述):根据OpId删除
	 */
	@Override
	public Integer deleteByOpId(Integer opId){
		return this.likeRecordDao.deleteByOpId(opId);
	}


	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType查询
	 */
	@Override
	public LikeRecord getByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType){
		return this.likeRecordDao.selectByObjectIdAndUserIdAndOpType(objectId,userId,opType);
	}

	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType更新
	 */
	@Override
	public Integer updateByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType){
		return this.likeRecordDao.updateByObjectIdAndUserIdAndOpType(bean,objectId,userId,opType);
	}


	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType删除
	 */
	@Override
	public Integer deleteByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType){
		return this.likeRecordDao.deleteByObjectIdAndUserIdAndOpType(objectId,userId,opType);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void doLike(String articleId, String userId, String nickName, OperRecordOpTypeEnum opTypeEnum){
		UserMessage userMessage = new UserMessage();
		userMessage.setCreateTime(LocalDateTime.now());

		//看点赞还是取消点赞，取消点赞再点赞不会再发消息
		switch (opTypeEnum){
			case ARTICLE_LIKE:
				//查文章看看
				ForumArticle forumArticle = this.forumArticleDao.selectByArticleId(articleId);
				if(forumArticle==null){
					throw new BusinessException("文章不存在");
				}
				//点赞
				articleLike(articleId,userId,forumArticle,opTypeEnum);

				userMessage.setArticleId(articleId);
				userMessage.setArticleTitle(forumArticle.getTitle());
				userMessage.setMessageType(MessageTypeEnum.ARTICLE_LIKE.getType());
				userMessage.setCommentId(Constants.ZERO);
				userMessage.setReceivedUserId(forumArticle.getUserId());
				break;
			case COMMENT_LIKE:
				//查看评论,通过评论id查
				ForumComment forumComment = this.forumCommentDao.selectByCommentId(Integer.parseInt(articleId));
				if(forumComment ==null){
					throw new BusinessException("评论不存在");
				}
				commentLike(articleId,userId,forumComment,opTypeEnum);
				forumArticle = this.forumArticleDao.selectByArticleId(forumComment.getArticleId());

				userMessage.setArticleId(forumArticle.getArticleId());
				userMessage.setArticleTitle(forumArticle.getTitle());
				userMessage.setMessageType(MessageTypeEnum.COMMENT_LIKE.getType());
				userMessage.setCommentId(forumComment.getCommentId());
				userMessage.setReceivedUserId(forumComment.getUserId());
				userMessage.setMessageContent(forumComment.getContent());
				break;
		}

		userMessage.setSendNickName(nickName);
		userMessage.setSendUserId(userId);
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		//作者点赞不发消息
		if(userId.equals(userMessage.getReceivedUserId())){
			throw new BusinessException("不能给自己点赞");
		}

		//查询消息，如果已经存在消息，就不发送
		UserMessage dbInfo = userMessageDao.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(userMessage.getArticleId(),userMessage.getCommentId(),
				userMessage.getSendUserId(),userMessage.getMessageType());

		if(dbInfo==null){
			//只有第一次点赞才发消息
			//userMessageDao.insert(userMessage);
			mqProducer.sendReminder(userMessage);
		}

	}

	public void articleLike(String articleId,String userId,ForumArticle forumArticle,OperRecordOpTypeEnum opTypeEnum){

		LikeRecord record = this.likeRecordDao.selectByObjectIdAndUserIdAndOpType(articleId,userId,opTypeEnum.getType());
		//点赞数
		Object likes = redisTemplate.opsForHash().get(RedisUtil.getArticleLikeCount(),articleId);

		//点赞判断
		//boolean isMember = redisTemplate.opsForSet().isMember(RedisUtil.getArticleLike(articleId),userId);
		Integer changeCount = 0;
		if(record!=null){
			changeCount=-1;
			//取消点赞，点赞减一
			this.likeRecordDao.deleteByObjectIdAndUserIdAndOpType(articleId,userId,opTypeEnum.getType());
			//删除redis的点赞信息
			//redisTemplate.opsForSet().remove(RedisUtil.getArticleLike(articleId),userId);
		}else{
			changeCount=1;
			//点赞
			LikeRecord likeRecord = new LikeRecord();
			likeRecord.setObjectId(articleId);
			likeRecord.setUserId(userId);
			likeRecord.setOpType(opTypeEnum.getType());
			likeRecord.setCreateTime(LocalDateTime.now());
			likeRecord.setAuthorUserId(forumArticle.getUserId());

			this.likeRecordDao.insert(likeRecord);

			//点赞成功，将数据传一份到redis中,设置集合健一天过期
			//redisTemplate.opsForSet().add(RedisUtil.getArticleLike(articleId),userId);
			//redisTemplate.expire(RedisUtil.getArticleLike(articleId),1, TimeUnit.DAYS);
		}

		//如果没有先获取文章信息，就点赞
		if(likes==null){
			throw new BusinessException("点赞信息错误");
		}
		redisTemplate.opsForHash().put(RedisUtil.getArticleLikeCount(),articleId,Integer.parseInt(likes.toString())+changeCount);
		//forumArticleDao.updateArticleCount(UpdateArticleCountTypeEnum.GOOD_COUNT.getType(), changeCount,articleId);
	}


	public void commentLike(String objectId,String userId,ForumComment forumComment,OperRecordOpTypeEnum operRecordOpTypeEnum){
		LikeRecord record = likeRecordDao.selectByObjectIdAndUserIdAndOpType(objectId,userId,operRecordOpTypeEnum.getType());
		Integer changeCount = 0;
		if(record != null){
			changeCount = -1;
			this.likeRecordDao.deleteByObjectIdAndUserIdAndOpType(objectId,userId,operRecordOpTypeEnum.getType());
		}else{
			changeCount = 1;
			//点赞
			LikeRecord likeRecord = new LikeRecord();
			likeRecord.setObjectId(objectId);
			likeRecord.setUserId(userId);
			likeRecord.setOpType(operRecordOpTypeEnum.getType());
			likeRecord.setAuthorUserId(forumComment.getUserId());
			likeRecord.setCreateTime(LocalDateTime.now());
			this.likeRecordDao.insert(likeRecord);
		}
		//评论点赞变化
		forumCommentDao.updateCommentGoodCount(changeCount,objectId);
	}



}