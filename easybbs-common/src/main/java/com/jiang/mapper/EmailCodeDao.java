package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):邮箱验证码
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface EmailCodeDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据EmailAndCode查询
	 */
	T selectByEmailAndCode(@Param("email") String email, @Param("code") String code);

	/**
     * @Description(描述):根据EmailAndCode更新
	 */
	Integer updateByEmailAndCode(@Param("bean") T t, @Param("email") String email, @Param("code") String code);

	/**
     * @Description(描述):根据EmailAndCode删除
	 */
	Integer deleteByEmailAndCode(@Param("email") String email, @Param("code") String code);

	void disableEmailCode(@Param("email") String email);


    void checkCode(String email, String emailCode);
}