package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):点赞记录
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface LikeRecordDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据OpId查询
	 */
	T selectByOpId(@Param("opId") Integer opId);

	/**
     * @Description(描述):根据OpId更新
	 */
	Integer updateByOpId(@Param("bean") T t, @Param("opId") Integer opId);

	/**
     * @Description(描述):根据OpId删除
	 */
	Integer deleteByOpId(@Param("opId") Integer opId);

	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType查询
	 */
	T selectByObjectIdAndUserIdAndOpType(@Param("objectId") String objectId, @Param("userId") String userId, @Param("opType") Integer opType);

	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType更新
	 */
	Integer updateByObjectIdAndUserIdAndOpType(@Param("bean") T t, @Param("objectId") String objectId, @Param("userId") String userId, @Param("opType") Integer opType);

	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType删除
	 */
	Integer deleteByObjectIdAndUserIdAndOpType(@Param("objectId") String objectId, @Param("userId") String userId, @Param("opType") Integer opType);


}