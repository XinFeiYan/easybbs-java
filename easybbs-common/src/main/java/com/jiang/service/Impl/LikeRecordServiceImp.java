package com.jiang.service.Impl;


import com.jiang.entity.query.LikeRecordQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.LikeRecordService;
import com.jiang.mapper.LikeRecordDao;
import com.jiang.entity.po.LikeRecord;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):LikeRecordServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("likeRecordService")
public class LikeRecordServiceImp implements LikeRecordService {


	@Resource
	private LikeRecordDao<LikeRecord,LikeRecordQuery> likeRecordDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<LikeRecord> findListByParam(LikeRecordQuery query){
		return this.likeRecordDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(LikeRecordQuery query){
		return this.likeRecordDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<LikeRecord> findListByPage(LikeRecordQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<LikeRecord> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<LikeRecord> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(LikeRecord bean){
		return this.likeRecordDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<LikeRecord> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.likeRecordDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<LikeRecord> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.likeRecordDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据OpId查询
	 */
	@Override
	public LikeRecord getByOpId(Integer opId){
		return this.likeRecordDao.selectByOpId(opId);
	}

	/**
     * @Description(描述):根据OpId更新
	 */
	@Override
	public Integer updateByOpId(LikeRecord bean, Integer opId){
		return this.likeRecordDao.updateByOpId(bean,opId);
	}


	/**
     * @Description(描述):根据OpId删除
	 */
	@Override
	public Integer deleteByOpId(Integer opId){
		return this.likeRecordDao.deleteByOpId(opId);
	}


	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType查询
	 */
	@Override
	public LikeRecord getByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType){
		return this.likeRecordDao.selectByObjectIdAndUserIdAndOpType(objectId,userId,opType);
	}

	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType更新
	 */
	@Override
	public Integer updateByObjectIdAndUserIdAndOpType(LikeRecord bean, String objectId, String userId, Integer opType){
		return this.likeRecordDao.updateByObjectIdAndUserIdAndOpType(bean,objectId,userId,opType);
	}


	/**
     * @Description(描述):根据ObjectIdAndUserIdAndOpType删除
	 */
	@Override
	public Integer deleteByObjectIdAndUserIdAndOpType(String objectId, String userId, Integer opType){
		return this.likeRecordDao.deleteByObjectIdAndUserIdAndOpType(objectId,userId,opType);
	}


}