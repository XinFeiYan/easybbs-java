package com.jiang.service.Impl;


import com.jiang.entity.query.UserIntegralRecordQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.UserIntegralRecordService;
import com.jiang.mapper.UserIntegralRecordDao;
import com.jiang.entity.po.UserIntegralRecord;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):UserIntegralRecordServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("userIntegralRecordService")
public class UserIntegralRecordServiceImp implements UserIntegralRecordService {


	@Resource
	private UserIntegralRecordDao<UserIntegralRecord,UserIntegralRecordQuery> userIntegralRecordDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<UserIntegralRecord> findListByParam(UserIntegralRecordQuery query){
		return this.userIntegralRecordDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(UserIntegralRecordQuery query){
		return this.userIntegralRecordDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<UserIntegralRecord> findListByPage(UserIntegralRecordQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<UserIntegralRecord> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<UserIntegralRecord> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(UserIntegralRecord bean){
		return this.userIntegralRecordDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<UserIntegralRecord> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.userIntegralRecordDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<UserIntegralRecord> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.userIntegralRecordDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据RecordId查询
	 */
	@Override
	public UserIntegralRecord getByRecordId(Integer recordId){
		return this.userIntegralRecordDao.selectByRecordId(recordId);
	}

	/**
     * @Description(描述):根据RecordId更新
	 */
	@Override
	public Integer updateByRecordId(UserIntegralRecord bean, Integer recordId){
		return this.userIntegralRecordDao.updateByRecordId(bean,recordId);
	}


	/**
     * @Description(描述):根据RecordId删除
	 */
	@Override
	public Integer deleteByRecordId(Integer recordId){
		return this.userIntegralRecordDao.deleteByRecordId(recordId);
	}


}