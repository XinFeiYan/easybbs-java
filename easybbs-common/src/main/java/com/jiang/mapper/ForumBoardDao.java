package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):文章板块信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface ForumBoardDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据BoardId查询
	 */
	T selectByBoardId(@Param("boardId") Integer boardId);

	/**
     * @Description(描述):根据BoardId更新
	 */
	Integer updateByBoardId(@Param("bean") T t, @Param("boardId") Integer boardId);

	/**
     * @Description(描述):根据BoardId删除
	 */
	Integer deleteByBoardId(@Param("boardId") Integer boardId);


}