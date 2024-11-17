package com.jiang.service.Impl;


import com.jiang.Enums.MessageStatusEnum;
import com.jiang.Enums.MessageTypeEnum;
import com.jiang.entity.dto.UserMessageCountDto;
import com.jiang.entity.query.UserMessageQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.UserMessageService;
import com.jiang.mapper.UserMessageDao;
import com.jiang.entity.po.UserMessage;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description(描述):UserMessageServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("userMessageService")
public class UserMessageServiceImp implements UserMessageService {


	@Resource
	private UserMessageDao<UserMessage,UserMessageQuery> userMessageDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<UserMessage> findListByParam(UserMessageQuery query){
		return this.userMessageDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(UserMessageQuery query){
		return this.userMessageDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<UserMessage> findListByPage(UserMessageQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<UserMessage> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<UserMessage> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(UserMessage bean){
		return this.userMessageDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<UserMessage> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.userMessageDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<UserMessage> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.userMessageDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据MessageId查询
	 */
	@Override
	public UserMessage getByMessageId(Integer messageId){
		return this.userMessageDao.selectByMessageId(messageId);
	}

	/**
     * @Description(描述):根据MessageId更新
	 */
	@Override
	public Integer updateByMessageId(UserMessage bean, Integer messageId){
		return this.userMessageDao.updateByMessageId(bean,messageId);
	}


	/**
     * @Description(描述):根据MessageId删除
	 */
	@Override
	public Integer deleteByMessageId(Integer messageId){
		return this.userMessageDao.deleteByMessageId(messageId);
	}


	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType查询
	 */
	@Override
	public UserMessage getByArticleIdAndCommentIdAndSendUserIdAndMessageType(String articleId, Integer commentId, String sendUserId, Integer messageType){
		return this.userMessageDao.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(articleId,commentId,sendUserId,messageType);
	}

	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType更新
	 */
	@Override
	public Integer updateByArticleIdAndCommentIdAndSendUserIdAndMessageType(UserMessage bean, String articleId, Integer commentId, String sendUserId, Integer messageType){
		return this.userMessageDao.updateByArticleIdAndCommentIdAndSendUserIdAndMessageType(bean,articleId,commentId,sendUserId,messageType);
	}


	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType删除
	 */
	@Override
	public Integer deleteByArticleIdAndCommentIdAndSendUserIdAndMessageType(String articleId, Integer commentId, String sendUserId, Integer messageType){
		return this.userMessageDao.deleteByArticleIdAndCommentIdAndSendUserIdAndMessageType(articleId,commentId,sendUserId,messageType);
	}

	public UserMessageCountDto getUserMessageCount(String userId){
		List<Map> mapList = userMessageDao.selectUserMessageCount(userId);
		UserMessageCountDto messageCountDto = new UserMessageCountDto();
		Long totalCount = 0L;
		for(Map item:mapList){
			Integer type =  (Integer) item.get("messageType");
			Long count = (Long)item.get("count");
			totalCount+=count;
			MessageTypeEnum messageType = MessageTypeEnum.getByType(type);
			switch(messageType){
				case SYS:
					messageCountDto.setSys(count);
					break;
				case COMMENT:
					messageCountDto.setReply(count);
					break;
				case ARTICLE_LIKE:
					messageCountDto.setLikePost(count);
					break;
				case COMMENT_LIKE:
					messageCountDto.setLikeComment(count);
					break;
				case DOWNLOAD_ATTACHMENT:
					messageCountDto.setDownloadAttachment(count);
					break;
			}
		}
		messageCountDto.setTotal(totalCount);
		return messageCountDto;
	}

	public void readMessageByType(String receivedUserId,Integer messageType){
		this.userMessageDao.updateMessageStatusBatch(receivedUserId,messageType,MessageStatusEnum.READ.getStatus());
	}
}