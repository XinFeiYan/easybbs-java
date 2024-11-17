package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description(描述):用户消息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface UserMessageDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据MessageId查询
	 */
	T selectByMessageId(@Param("messageId") Integer messageId);

	/**
     * @Description(描述):根据MessageId更新
	 */
	Integer updateByMessageId(@Param("bean") T t, @Param("messageId") Integer messageId);

	/**
     * @Description(描述):根据MessageId删除
	 */
	Integer deleteByMessageId(@Param("messageId") Integer messageId);

	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType查询
	 */
	T selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(@Param("articleId") String articleId, @Param("commentId") Integer commentId, @Param("sendUserId") String sendUserId, @Param("messageType") Integer messageType);

	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType更新
	 */
	Integer updateByArticleIdAndCommentIdAndSendUserIdAndMessageType(@Param("bean") T t, @Param("articleId") String articleId, @Param("commentId") Integer commentId, @Param("sendUserId") String sendUserId, @Param("messageType") Integer messageType);

	/**
     * @Description(描述):根据ArticleIdAndCommentIdAndSendUserIdAndMessageType删除
	 */
	Integer deleteByArticleIdAndCommentIdAndSendUserIdAndMessageType(@Param("articleId") String articleId, @Param("commentId") Integer commentId, @Param("sendUserId") String sendUserId, @Param("messageType") Integer messageType);

	List<Map> selectUserMessageCount(@Param("userId") String userId);

	void updateMessageStatusBatch(@Param("receivedUserId") String receivedUserId,@Param("messageType") Integer messageType,@Param("status") Integer status);

}