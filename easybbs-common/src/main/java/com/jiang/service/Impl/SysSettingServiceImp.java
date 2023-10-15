package com.jiang.service.Impl;


import com.jiang.entity.query.SysSettingQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.SysSettingService;
import com.jiang.mapper.SysSettingDao;
import com.jiang.entity.po.SysSetting;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):SysSettingServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("sysSettingService")
public class SysSettingServiceImp implements SysSettingService {


	@Resource
	private SysSettingDao<SysSetting,SysSettingQuery> sysSettingDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<SysSetting> findListByParam(SysSettingQuery query){
		return this.sysSettingDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(SysSettingQuery query){
		return this.sysSettingDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<SysSetting> findListByPage(SysSettingQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<SysSetting> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<SysSetting> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(SysSetting bean){
		return this.sysSettingDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<SysSetting> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.sysSettingDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<SysSetting> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.sysSettingDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据Code查询
	 */
	@Override
	public SysSetting getByCode(String code){
		return this.sysSettingDao.selectByCode(code);
	}

	/**
     * @Description(描述):根据Code更新
	 */
	@Override
	public Integer updateByCode(SysSetting bean, String code){
		return this.sysSettingDao.updateByCode(bean,code);
	}


	/**
     * @Description(描述):根据Code删除
	 */
	@Override
	public Integer deleteByCode(String code){
		return this.sysSettingDao.deleteByCode(code);
	}


}