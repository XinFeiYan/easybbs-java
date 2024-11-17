package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):用户积分记录表
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface UserIntegralRecordDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据RecordId查询
	 */
	T selectByRecordId(@Param("recordId") Integer recordId);

	/**
     * @Description(描述):根据RecordId更新
	 */
	Integer updateByRecordId(@Param("bean") T t, @Param("recordId") Integer recordId);

	/**
     * @Description(描述):根据RecordId删除
	 */
	Integer deleteByRecordId(@Param("recordId") Integer recordId);


}