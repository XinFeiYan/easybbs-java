package com.jiang.service.Impl;


import com.jiang.Exception.BusinessException;
import com.jiang.Utils.StringTools;
import com.jiang.entity.config.WebConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.po.UserInfo;
import com.jiang.entity.query.EmailCodeQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.entity.query.UserInfoQuery;
import com.jiang.mapper.UserInfoDao;
import com.jiang.service.EmailCodeService;
import com.jiang.mapper.EmailCodeDao;
import com.jiang.entity.po.EmailCode;
import com.jiang.entity.vo.PaginationResultVO;

import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description(描述):EmailCodeServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("emailCodeService")
public class EmailCodeServiceImp implements EmailCodeService {

	private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImp.class);
	@Resource
	private EmailCodeDao<EmailCode,EmailCodeQuery> emailCodeDao;

	@Resource
	private UserInfoDao<UserInfo, UserInfoQuery> userInfoDao;

	@Resource
	private JavaMailSender javaMailSender;

	@Resource
	private WebConfig webConfig;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<EmailCode> findListByParam(EmailCodeQuery query){
		return this.emailCodeDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(EmailCodeQuery query){
		return this.emailCodeDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<EmailCode> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<EmailCode> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(EmailCode bean){
		return this.emailCodeDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<EmailCode> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.emailCodeDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<EmailCode> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.emailCodeDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据EmailAndCode查询
	 */
	@Override
	public EmailCode getByEmailAndCode(String email, String code){
		return this.emailCodeDao.selectByEmailAndCode(email,code);
	}

	/**
     * @Description(描述):根据EmailAndCode更新
	 */
	@Override
	public Integer updateByEmailAndCode(EmailCode bean, String email, String code){
		return this.emailCodeDao.updateByEmailAndCode(bean,email,code);
	}


	/**
     * @Description(描述):根据EmailAndCode删除
	 */
	@Override
	public Integer deleteByEmailAndCode(String email, String code){
		return this.emailCodeDao.deleteByEmailAndCode(email,code);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void sendEmailCode(String email, Integer type) {
		if(type == Constants.ZERO){
			//0表示注册，1表示找回密码
			UserInfo userInfo = userInfoDao.selectByEmail(email);
			if(userInfo!=null){
				throw new BusinessException("邮箱已存在");
			}
		}

		emailCodeDao.disableEmailCode(email);
		//重置状态
		//生成随机数
		String code = StringTools.getRandomString(Constants.LENGTH_5);
		sendEmailCodeDo(email,code);

		EmailCode emailCode = new EmailCode();
		emailCode.setCode(code);
		emailCode.setEmail(email);
		emailCode.setStatus(Constants.ZERO);
		emailCode.setCreateTime(LocalDateTime.now());

		emailCodeDao.insert(emailCode);

	}

	private void sendEmailCodeDo(String toEmail,String code){
		try{
			//spring发邮件
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,true);

			//邮件发送人
			helper.setFrom(webConfig.getSendUserName());
			//收件人
			helper.setTo(toEmail);

			helper.setSubject("注册邮箱验证码");
			helper.setText("邮箱验证码："+code);
			helper.setSentDate(new Date());

			javaMailSender.send(message);
		}catch(Exception e){
			logger.error("发送邮件失败",e);
			throw new BusinessException("邮件发送失败");
		}
	}

	@Override
	public void checkCode(String email, String emailCode) {
		EmailCode info = this.emailCodeDao.selectByEmailAndCode(email,emailCode);
		if(null==info){
			throw new BusinessException("邮箱验证码不正确");
		}
		//比较结果为秒
		if(info.getStatus()!=0||Duration.between(info.getCreateTime(),LocalDateTime.now()).getSeconds()>60*Constants.LENGTH_15){
			throw new BusinessException("验证码已失效");
		}
		emailCodeDao.disableEmailCode(email);
	}
}