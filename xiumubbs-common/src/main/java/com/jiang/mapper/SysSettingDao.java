package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):系统设置信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface SysSettingDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据Code查询
	 */
	T selectByCode(@Param("code") String code);

	/**
     * @Description(描述):根据Code更新
	 */
	Integer updateByCode(@Param("bean") T t, @Param("code") String code);

	/**
     * @Description(描述):根据Code删除
	 */
	Integer deleteByCode(@Param("code") String code);


}