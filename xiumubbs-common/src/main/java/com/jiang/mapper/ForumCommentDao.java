package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):评论
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface ForumCommentDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据CommentId查询
	 */
	T selectByCommentId(@Param("commentId") Integer commentId);

	/**
     * @Description(描述):根据CommentId更新
	 */
	Integer updateByCommentId(@Param("bean") T t, @Param("commentId") Integer commentId);

	/**
     * @Description(描述):根据CommentId删除
	 */
	Integer deleteByCommentId(@Param("commentId") Integer commentId);

	/**
	 * 修改点赞
	 */
	void updateCommentGoodCount(@Param("changeCount")Integer changeCount,@Param("commentId") String commentId);

	/**
	 * 取消置顶
	 */
	void updateTopTypeByArticle(@Param("articleId")String articleId);
}