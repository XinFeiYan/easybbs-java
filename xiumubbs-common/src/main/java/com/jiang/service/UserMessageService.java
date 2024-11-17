package com.jiang.service;


import com.jiang.entity.dto.UserMessageCountDto;
import com.jiang.entity.po.UserMessage;
import com.jiang.entity.query.UserMessageQuery;
import com.jiang.entity.vo.PaginationResultVO;

import java.util.List;
import java.time.LocalDateTime;

/**
 * @Description(描述):UserMessageService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface UserMessageService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<UserMessage> findListByParam(UserMessageQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(UserMessageQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<UserMessage> findListByPage(UserMessageQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(UserMessage bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<UserMessage> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserMessage> listBean);
	/**
     * @Description(描述):根据MessageId查询
	 */
	UserMessage getByMessageId(Integer messageId);

	/**
     * @Description(描述):根据MessageId更新
	 */
	Integer updateByMessageId(UserMessage bean, Integer messageId);

	/**
     * @Description(描述):根据MessageId删除
	 */
	Integer deleteByMessageId(Integer messageId);

	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType查询
	 */
	UserMessage getByArticleIdAndCommentIdAndSendUserIdAndMessageType(String articleId, Integer commentId, String sendUserId, Integer messageType);

	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType更新
	 */
	Integer updateByArticleIdAndCommentIdAndSendUserIdAndMessageType(UserMessage bean, String articleId, Integer commentId, String sendUserId, Integer messageType);

	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType删除
	 */
	Integer deleteByArticleIdAndCommentIdAndSendUserIdAndMessageType(String articleId, Integer commentId, String sendUserId, Integer messageType);

	UserMessageCountDto getUserMessageCount(String userId);

	void readMessageByType(String receivedUserId,Integer messageType);
}