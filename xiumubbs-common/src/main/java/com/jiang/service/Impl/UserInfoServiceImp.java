package com.jiang.service.Impl;


import com.jiang.Enums.*;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.*;

import com.jiang.entity.config.WebConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.SessionWebUserDto;

import com.jiang.entity.po.*;
import com.jiang.entity.query.*;

import com.jiang.mapper.*;
import com.jiang.service.EmailCodeService;
import com.jiang.service.UserInfoService;
import com.jiang.entity.vo.PaginationResultVO;


import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description(描述):UserInfoServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("userInfoService")
public class UserInfoServiceImp implements UserInfoService {

	public static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImp.class);
	@Resource
	private UserInfoDao<UserInfo,UserInfoQuery> userInfoDao;

	@Resource
	private EmailCodeService emailCodeService;

	@Resource
	private UserMessageDao<UserMessage, UserMessageQuery> userMessageDao;

	@Resource
	private UserIntegralRecordDao<UserIntegralRecord, UserIntegralRecordQuery> userIntegralRecordDao;

	@Resource
	private ForumArticleDao<ForumArticle,ForumArticleQuery> forumArticleDao;

	@Resource
	private ForumCommentDao<ForumComment,ForumArticleQuery> forumCommentDao;
	@Resource
	private WebConfig webConfig;

	@Resource
	private FileUtils fileUtils;
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
		/**为什么积分不够还要给他减，不够就返回啊*/
		//如果减成负数，就减当前数，不够减到0
		if(UserIntegralChangeTypeEnum.REDUCE.getChangeType().equals(changType)&&
		userInfo.getCurrentIntegral()+integral<0){
			integral = changType*userInfo.getCurrentIntegral();
		}

		//记录条数，积分变化信息
		UserIntegralRecord record = new UserIntegralRecord();
		record.setUserId(userId);
		record.setOperType(operTypeEnum.getOperType());
		record.setCreateTime(LocalDateTime.now());
		record.setIntegral(integral);
		this.userIntegralRecordDao.insert(record);

		//更新用户积分，sql语句加了一个条件，要添加上去大于0才能修改，否则返回不成功
		Integer count = this.userInfoDao.updateIntegral(userId,integral);
		if(count==0){
			throw new BusinessException("更新用户积分失败");
		}
	}

	@Override
	public SessionWebUserDto login(String email, String password, String ip) {
		//调用外部接口
		UserInfo userInfo = userInfoDao.selectByEmail(email);
		//数据库密码进行加密过
		if(userInfo==null||!userInfo.getPassword().equals(password)){
			throw new BusinessException("账号或密码错误");
		}
		if(UserStatusEnum.DISABLE.equals(userInfo.getStatus())){
			throw new BusinessException("账号已禁用");
		}
		String ipAddress = getIpAddress(ip);

		UserInfo upUserInfo = new UserInfo();
		upUserInfo.setLastLoginTime(LocalDateTime.now());
		upUserInfo.setLastLoginIp(ip);
		upUserInfo.setLastLoginIpAddress(ipAddress);
		this.userInfoDao.updateByUserId(upUserInfo,userInfo.getUserId());

		//登录信息
		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setNickname(userInfo.getNickName());
		sessionWebUserDto.setUserId(userInfo.getUserId());
		sessionWebUserDto.setProvince(ipAddress);
		if(!StringTools.isEmpty(webConfig.getAdminEmails())&& ArrayUtils.contains(webConfig.getAdminEmails().split(","),userInfo.getEmail())){
			sessionWebUserDto.setAdmin(true);
		}else{
			sessionWebUserDto.setAdmin(false);
		}
		return sessionWebUserDto;
	}

	//获取ip地址
	public String getIpAddress(String ip){
		try{
			String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip"+ip;
			String responseJson = OKHttpUtils.getRequest(url);
			if(null==responseJson){
				return Constants.NO_ADDRESS;
			}
			Map<String,String> addressInfo = JsonUtils.convertJson2Obj(responseJson,Map.class);
			return addressInfo.get("pro");
		}catch(Exception e){
			logger.error("获取ip地址失败");
		}
		return Constants.NO_ADDRESS;
	}

	@Transactional(rollbackFor = Exception.class)
	public void resetPwd(String email,String password,String emailCode){
		//验证邮箱是否存在
		if(userInfoDao.selectByEmail(email)==null){
			throw new BusinessException("邮箱不存在");
		}
		//验证邮箱
		emailCodeService.checkCode(email,emailCode);


		UserInfo userInfo = new UserInfo();
		userInfo.setPassword(password);
		userInfoDao.updateByEmail(userInfo,email);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserInfo(UserInfo userInfo, MultipartFile avatar) {
		userInfoDao.updateByUserId(userInfo,userInfo.getUserId());
		if(avatar!=null){
			fileUtils.uploadFile2local(avatar,userInfo.getUserId(),FileUploadTypeEnum.AVATAR);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserStatus(Integer status, String userId) {
		//如果是禁用，将文章全部禁用
		if(UserStatusEnum.DISABLE.getStatus().equals(status)){

		}
		UserInfo userInfo = new UserInfo();
		userInfo.setStatus(status);
		userInfoDao.updateByUserId(userInfo,userId);
	}
}