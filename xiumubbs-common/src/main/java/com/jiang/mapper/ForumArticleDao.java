package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):文章信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface ForumArticleDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据ArticleId查询
	 */
	T selectByArticleId(@Param("articleId") String articleId);

	/**
     * @Description(描述):根据ArticleId更新
	 */
	Integer updateByArticleId(@Param("bean") T t, @Param("articleId") String articleId);

	/**
     * @Description(描述):根据ArticleId删除
	 */
	Integer deleteByArticleId(@Param("articleId") String articleId);

	void updateArticleCount(@Param("updateType") Integer updateType,@Param("changeCount")Integer changeCount,
							@Param("articleId")String articleId);

	void updateBoardNameBatch(@Param("boardType") Integer boardType,@Param("boardName")String boardName,
							  @Param("boardId")Integer boardId);


}