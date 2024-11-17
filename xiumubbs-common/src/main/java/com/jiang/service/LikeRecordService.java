package com.jiang.service;


import com.jiang.Enums.OperRecordOpTypeEnum;
import com.jiang.entity.po.LikeRecord;
import com.jiang.entity.query.LikeRecordQuery;
import com.jiang.entity.vo.PaginationResultVO;

import java.util.List;
import java.time.LocalDateTime;

/**
 * @Description(描述):LikeRecordService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface LikeRecordService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<LikeRecord> findListByParam(LikeRecordQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(LikeRecordQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<LikeRecord> findListByPage(LikeRecordQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(LikeRecord bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<LikeRecord> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<LikeRecord> listBean);
	/**
     * @Description(描述):根据OpId查询
	 */
	LikeRecord getByOpId(Integer opId);

	/**
     * @Description(描述):根据OpId更新
	 */
	Integer updateByOpId(LikeRecord bean, Integer opId);

	/**
     * @Description(描述):根据OpId删除
	 */
	Integer deleteByOpId(Integer opId);

	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType查询
	 */
	LikeRecord getByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType);

	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType更新
	 */
	Integer updateByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType);

	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType删除
	 */
	Integer deleteByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType);

	/**
	 * 点赞,分文章与评论
	 */
	void doLike(String objectId, String userId, String nickName, OperRecordOpTypeEnum operRecordOpTypeEnum);
}