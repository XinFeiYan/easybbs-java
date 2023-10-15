package com.jiang.service;


import com.jiang.entity.po.UserIntegralRecord;
import com.jiang.entity.query.UserIntegralRecordQuery;
import com.jiang.entity.vo.PaginationResultVO;

import java.util.List;
import java.time.LocalDateTime;

/**
 * @Description(描述):UserIntegralRecordService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface UserIntegralRecordService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<UserIntegralRecord> findListByParam(UserIntegralRecordQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(UserIntegralRecordQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<UserIntegralRecord> findListByPage(UserIntegralRecordQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(UserIntegralRecord bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<UserIntegralRecord> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserIntegralRecord> listBean);
	/**
     * @Description(描述):根据RecordId查询
	 */
	UserIntegralRecord getByRecordId(Integer recordId);

	/**
     * @Description(描述):根据RecordId更新
	 */
	Integer updateByRecordId(UserIntegralRecord bean, Integer recordId);

	/**
     * @Description(描述):根据RecordId删除
	 */
	Integer deleteByRecordId(Integer recordId);

}