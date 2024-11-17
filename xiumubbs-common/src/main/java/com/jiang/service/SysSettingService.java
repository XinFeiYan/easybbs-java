package com.jiang.service;


import com.jiang.entity.dto.SysSettingDto;
import com.jiang.entity.po.SysSetting;
import com.jiang.entity.query.SysSettingQuery;
import com.jiang.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * @Description(描述):SysSettingService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface SysSettingService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<SysSetting> findListByParam(SysSettingQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(SysSettingQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<SysSetting> findListByPage(SysSettingQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(SysSetting bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<SysSetting> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<SysSetting> listBean);
	/**
     * @Description(描述):根据Code查询
	 */
	SysSetting getByCode(String code);

	/**
     * @Description(描述):根据Code更新
	 */
	Integer updateByCode(SysSetting bean, String code);

	/**
     * @Description(描述):根据Code删除
	 */
	Integer deleteByCode(String code);

	/**
	 * 刷新缓存
	 */
	SysSettingDto refreshCache();

	void saveSetting(SysSettingDto sysSettingDto);

}