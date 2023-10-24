package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):用户信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface UserInfoDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据UserId查询
	 */
	T selectByUserId(@Param("userId") String userId);

	/**
     * @Description(描述):根据UserId更新
	 */
	Integer updateByUserId(@Param("bean") T t, @Param("userId") String userId);

	/**
     * @Description(描述):根据UserId删除
	 */
	Integer deleteByUserId(@Param("userId") String userId);

	/**
     * @Description(描述):根据Email查询
	 */
	T selectByEmail(@Param("email") String email);

	/**
     * @Description(描述):根据Email更新
	 */
	Integer updateByEmail(@Param("bean") T t, @Param("email") String email);

	/**
     * @Description(描述):根据Email删除
	 */
	Integer deleteByEmail(@Param("email") String email);

	/**
     * @Description(描述):根据NickName查询
	 */
	T selectByNickName(@Param("nickName") String nickName);

	/**
     * @Description(描述):根据NickName更新
	 */
	Integer updateByNickName(@Param("bean") T t, @Param("nickName") String nickName);

	/**
     * @Description(描述):根据NickName删除
	 */
	Integer deleteByNickName(@Param("nickName") String nickName);

	Integer updateIntegral(@Param("userId") String userId,@Param("integral") Integer integer);

}