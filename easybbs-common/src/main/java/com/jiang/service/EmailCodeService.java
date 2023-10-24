package com.jiang.service;


import com.jiang.entity.po.EmailCode;
import com.jiang.entity.query.EmailCodeQuery;
import com.jiang.entity.vo.PaginationResultVO;

import java.util.List;
import java.time.LocalDateTime;

/**
 * @Description(描述):EmailCodeService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface EmailCodeService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<EmailCode> findListByParam(EmailCodeQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(EmailCodeQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(EmailCode bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<EmailCode> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<EmailCode> listBean);
	/**
     * @Description(描述):根据EmailAndCode查询
	 */
	EmailCode getByEmailAndCode(String email, String code);

	/**
     * @Description(描述):根据EmailAndCode更新
	 */
	Integer updateByEmailAndCode(EmailCode bean, String email, String code);

	/**
     * @Description(描述):根据EmailAndCode删除
	 */
	Integer deleteByEmailAndCode(String email, String code);

	/**
	 * 发送邮箱验证码
	 * @param email
	 * @param type
	 */
	void sendEmailCode(String email,Integer type);

	/**
	 * 邮箱验证码检测
	 * @param email
	 * @param emailCod
	 */
	void checkCode(String email,String emailCod);

}