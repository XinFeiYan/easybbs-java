package com.jiang.service.Impl;


import com.jiang.Enums.*;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.StringTools;
import com.jiang.Utils.SysCacheUtils;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.po.EmailCode;
import com.jiang.entity.po.UserIntegralRecord;
import com.jiang.entity.po.UserMessage;
import com.jiang.entity.query.*;
import com.jiang.mapper.EmailCodeDao;
import com.jiang.mapper.UserIntegralRecordDao;
import com.jiang.mapper.UserMessageDao;
import com.jiang.service.EmailCodeService;
import com.jiang.service.UserInfoService;
import com.jiang.mapper.UserInfoDao;
import com.jiang.entity.po.UserInfo;
import com.jiang.entity.vo.PaginationResultVO;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

	@Resource
	private EmailCodeService emailCodeService;

	@Resource
	private UserMessageDao<UserMessage, UserMessageQuery> userMessageDao;

	@Resource
	private UserIntegralRecordDao<UserIntegralRecord, UserIntegralRecordQuery> userIntegralRecordDao;
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

	/**
	 * 用户注册
	 *
	 */
	@Transactional(rollbackFor = Exception.class)
	public void register(String email,String emailCode,String nickName,String password){
		UserInfo userInfo = this.userInfoDao.selectByEmail(email);
		if(null!=userInfo){
			throw new BusinessException("邮箱账号已存在");
		}
		userInfo = this.userInfoDao.selectByNickName(nickName);
		if(null!=userInfo){
			throw new BusinessException("昵称已存在");
		}
		//验证邮箱，根据验证码与邮箱验证
		emailCodeService.checkCode(email,emailCode);

		//存储信息
		String userId = StringTools.getRandomNumber(Constants.LENGTH_10);

		UserInfo insertInfo = new UserInfo();
		insertInfo.setEmail(email);
		insertInfo.setNickName(nickName);
		insertInfo.setUserId(userId);
		insertInfo.setPassword(StringTools.encodeMd5(password));
		insertInfo.setJoinTime(LocalDateTime.now());
		insertInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		insertInfo.setTotalIntegral(Constants.ZERO);//总积分
		insertInfo.setCurrentIntegral(Constants.ZERO);//当前积分
		this.userInfoDao.insert(insertInfo);

		//更新积分
		updateUserIntegral(userId,UserIntegralOperTypeEnum.REGISTER,UserIntegralChangeTypeEnum.ADD.getChangeType(),Constants.INTEGRAL_5);

		//记录消息
		UserMessage userMessage = new UserMessage();
		userMessage.setReceivedUserId(userId);
		userMessage.setMessageType(MessageTypeEnum.SYS.getType());
		userMessage.setCreateTime(LocalDateTime.now());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		//欢迎消息
		userMessage.setMessageContent(SysCacheUtils.getSysSetting().getRegisterSetting().getRegisterWelcomeInfo());
		userMessageDao.insert(userMessage);

	}

	/**
	 * 更新用户积分
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum,Integer changType,Integer integral){
		integral = changType*integral;
		if(integral==0){
			return;
		}
		UserInfo userInfo = userInfoDao.selectByUserId(userId);
		//如果减成负数，就减当前数
		if(UserIntegralChangeTypeEnum.REDUCE.getChangeType().equals(changType)&&
		userInfo.getCurrentIntegral()+integral<0){
			integral = changType*userInfo.getCurrentIntegral();
		}

		//记录条数
		UserIntegralRecord record = new UserIntegralRecord();
		record.setUserId(userId);
		record.setOperType(operTypeEnum.getOperType());
		record.setCreateTime(LocalDateTime.now());
		record.setIntegral(integral);
		this.userIntegralRecordDao.insert(record);

		//更新用户积分
		Integer count = this.userInfoDao.updateIntegral(userId,integral);
		if(count==0){
			throw new BusinessException("更新用户积分失败");
		}

	}
}