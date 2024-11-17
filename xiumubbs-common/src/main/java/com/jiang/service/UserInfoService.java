package com.jiang.service;


import com.jiang.Enums.UserIntegralOperTypeEnum;
import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.UserInfo;
import com.jiang.entity.query.UserInfoQuery;
import com.jiang.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.time.LocalDateTime;

/**
 * @Description(描述):UserInfoService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface UserInfoService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<UserInfo> findListByParam(UserInfoQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(UserInfoQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(UserInfo bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);
	/**
     * @Description(描述):根据UserId查询
	 */
	UserInfo getByUserId(String userId);

	/**
     * @Description(描述):根据UserId更新
	 */
	Integer updateByUserId(UserInfo bean, String userId);

	/**
     * @Description(描述):根据UserId删除
	 */
	Integer deleteByUserId(String userId);

	/**
     * @Description(描述):根据Email查询
	 */
	UserInfo getByEmail(String email);

	/**
     * @Description(描述):根据Email更新
	 */
	Integer updateByEmail(UserInfo bean, String email);

	/**
     * @Description(描述):根据Email删除
	 */
	Integer deleteByEmail(String email);

	/**
     * @Description(描述):根据NickName查询
	 */
	UserInfo getByNickName(String nickName);

	/**
     * @Description(描述):根据NickName更新
	 */
	Integer updateByNickName(UserInfo bean, String nickName);

	/**
     * @Description(描述):根据NickName删除
	 */
	Integer deleteByNickName(String nickName);

	void register(String email,String emailCode,String nickName,String password);

	/**
	 * 更新用户积分
	 * @param userId
	 * @param operTypeEnum
	 * @param changType
	 * @param integral
	 */
	void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum, Integer changType, Integer integral);

	SessionWebUserDto login(String email,String password,String ip);

	/**
	 * 重置密码
	 */
	void resetPwd(String email,String password,String emailCode);

	void updateUserInfo(UserInfo userInfo, MultipartFile avatar);

	void updateUserStatus(Integer status,String userId);

}