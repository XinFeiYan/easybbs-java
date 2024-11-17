package com.jiang.service.Impl;


import com.jiang.Enums.SysSettingCodeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.JsonUtils;
import com.jiang.Utils.StringTools;
import com.jiang.Utils.SysCacheUtils;
import com.jiang.entity.dto.SysSettingDto;
import com.jiang.entity.query.SysSettingQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.SysSettingService;
import com.jiang.mapper.SysSettingDao;
import com.jiang.entity.po.SysSetting;
import com.jiang.entity.vo.PaginationResultVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description(描述):SysSettingServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("sysSettingService")
public class SysSettingServiceImp implements SysSettingService {

	private static final Logger logger = LoggerFactory.getLogger(SysSettingServiceImp.class);
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

	public SysSettingDto refreshCache(){
		try{
			SysSettingDto sysSettingDto = new SysSettingDto();
			List<SysSetting> list = this.sysSettingDao.selectList(new SysSettingQuery());
			for(SysSetting sysSetting:list){
				String json = sysSetting.getJsonContent();
				if(StringTools.isEmpty(json)){
					continue;
				}
				String code = sysSetting.getCode();
				//获取枚举
				SysSettingCodeEnum sysSettingCodeEnum = SysSettingCodeEnum.getByCode(code);
				/**
				 * 这段代码使用Java语言中的反射机制，主要实现了以下功能：
				 * 通过 PropertyDescriptor 类来获取指定类（在此处为 SysSettingDto.class）中指定属性（在此处为 sysSettingCodeEnum.getPropName()）的描述符。
				 * 通过调用 getPropertyType() 方法获取该属性的数据类型。
				 * 反射是通过class文件从而创建java对象
				 */
				PropertyDescriptor pd = new PropertyDescriptor(sysSettingCodeEnum.getPropName(), SysSettingDto.class);
				//得到属性的set方法
				Method method = pd.getWriteMethod();
				//实例化对象
				Class subCalssz = Class.forName(sysSettingCodeEnum.getClassz());
				//通过反射将值输入进去,这就是获取propName对应的set方法后写值
				method.invoke(sysSettingDto, JsonUtils.convertJson2Obj(json,subCalssz));

			}
			//存入缓存
			SysCacheUtils.refresh(sysSettingDto);
			return sysSettingDto;
		}catch (Exception e){
			logger.error("刷新缓存失败",e);
			throw new BusinessException("刷新缓存失败");
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveSetting(SysSettingDto sysSettingDto) throws BusinessException {
		try {
			Class classz= SysSettingDto.class;
			for (SysSettingCodeEnum codeEnum:SysSettingCodeEnum.values()){
				PropertyDescriptor pd = new PropertyDescriptor(codeEnum.getPropName(),classz);
				//获取到这里里面的get方法
				Method method= pd.getReadMethod();
				Object obj=method.invoke(sysSettingDto);
				SysSetting sysSetting=new SysSetting();
				sysSetting.setCode(codeEnum.getCode());
				sysSetting.setJsonContent(JsonUtils.convertObj2Json(obj));
				this.sysSettingDao.insertOrUpdate(sysSetting);
			}
		}catch (Exception e){
			logger.error("保存设置失败",e);
			throw new BusinessException("保存设置失败");
		}
	}


}