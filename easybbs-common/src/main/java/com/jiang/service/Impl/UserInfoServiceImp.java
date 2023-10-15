package com.jiang.service.Impl;


import com.jiang.entity.query.UserInfoQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.UserInfoService;
import com.jiang.mapper.UserInfoDao;
import com.jiang.entity.po.UserInfo;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):UserInfoServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("userInfoService")
public class UserInfoServiceImp implements UserInfoService {


	@Resource
	private UserInfoDao<UserInfo,UserInfoQuery> userInfoDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<UserInfo> findListByParam(UserInfoQuery query){
		return this.userInfoDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(UserInfoQuery query){
		return this.userInfoDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<UserInfo> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<UserInfo> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(UserInfo bean){
		return this.userInfoDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfo> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.userInfoDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<UserInfo> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.userInfoDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据UserId查询
	 */
	@Override
	public UserInfo getByUserId(String userId){
		return this.userInfoDao.selectByUserId(userId);
	}

	/**
     * @Description(描述):根据UserId更新
	 */
	@Override
	public Integer updateByUserId(UserInfo bean, String userId){
		return this.userInfoDao.updateByUserId(bean,userId);
	}


	/**
     * @Description(描述):根据UserId删除
	 */
	@Override
	public Integer deleteByUserId(String userId){
		return this.userInfoDao.deleteByUserId(userId);
	}


	/**
     * @Description(描述):根据Email查询
	 */
	@Override
	public UserInfo getByEmail(String email){
		return this.userInfoDao.selectByEmail(email);
	}

	/**
     * @Description(描述):根据Email更新
	 */
	@Override
	public Integer updateByEmail(UserInfo bean, String email){
		return this.userInfoDao.updateByEmail(bean,email);
	}


	/**
     * @Description(描述):根据Email删除
	 */
	@Override
	public Integer deleteByEmail(String email){
		return this.userInfoDao.deleteByEmail(email);
	}


	/**
     * @Description(描述):根据NickName查询
	 */
	@Override
	public UserInfo getByNickName(String nickName){
		return this.userInfoDao.selectByNickName(nickName);
	}

	/**
     * @Description(描述):根据NickName更新
	 */
	@Override
	public Integer updateByNickName(UserInfo bean, String nickName){
		return this.userInfoDao.updateByNickName(bean,nickName);
	}


	/**
     * @Description(描述):根据NickName删除
	 */
	@Override
	public Integer deleteByNickName(String nickName){
		return this.userInfoDao.deleteByNickName(nickName);
	}


}