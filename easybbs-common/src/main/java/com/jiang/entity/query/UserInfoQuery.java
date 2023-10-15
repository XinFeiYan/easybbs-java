package com.jiang.entity.query;

import java.time.LocalDateTime;


/**
 * @Description(描述):用户信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class UserInfoQuery extends BaseQuery {
	/**
     * @Description(描述):用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
     * @Description(描述):昵称
	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
     * @Description(描述):邮箱
	 */
	private String email;

	private String emailFuzzy;

	/**
     * @Description(描述):密码
	 */
	private String password;

	private String passwordFuzzy;

	/**
     * @Description(描述):0:女 1:男
	 */
	private Integer sex;

	/**
     * @Description(描述):个人描述
	 */
	private String personDescription;

	private String personDescriptionFuzzy;

	/**
     * @Description(描述):加入时间
	 */
	private LocalDateTime joinTime;

	private String joinTimeStart;

	private String joinTimeEnd;

	/**
     * @Description(描述):最后登录时间
	 */
	private LocalDateTime lastLoginTime;

	private String lastLoginTimeStart;

	private String lastLoginTimeEnd;

	/**
     * @Description(描述):最后登录IP
	 */
	private String lastLoginIp;

	private String lastLoginIpFuzzy;

	/**
     * @Description(描述):最后登录ip地址
	 */
	private String lastLoginIpAddress;

	private String lastLoginIpAddressFuzzy;

	/**
     * @Description(描述):积分
	 */
	private Integer totalIntegral;

	/**
     * @Description(描述):当前积分
	 */
	private Integer currentIntegral;

	/**
     * @Description(描述):0:禁用 1:正常
	 */
	private Integer status;

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setNickNameFuzzy(String nickNameFuzzy) {
		this.nickNameFuzzy = nickNameFuzzy;
	}

	public String getNickNameFuzzy() {
		return this.nickNameFuzzy;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return this.emailFuzzy;
	}

	public void setPasswordFuzzy(String passwordFuzzy) {
		this.passwordFuzzy = passwordFuzzy;
	}

	public String getPasswordFuzzy() {
		return this.passwordFuzzy;
	}

	public void setPersonDescriptionFuzzy(String personDescriptionFuzzy) {
		this.personDescriptionFuzzy = personDescriptionFuzzy;
	}

	public String getPersonDescriptionFuzzy() {
		return this.personDescriptionFuzzy;
	}

	public void setJoinTimeStart(String joinTimeStart) {
		this.joinTimeStart = joinTimeStart;
	}

	public String getJoinTimeStart() {
		return this.joinTimeStart;
	}

	public void setJoinTimeEnd(String joinTimeEnd) {
		this.joinTimeEnd = joinTimeEnd;
	}

	public String getJoinTimeEnd() {
		return this.joinTimeEnd;
	}

	public void setLastLoginTimeStart(String lastLoginTimeStart) {
		this.lastLoginTimeStart = lastLoginTimeStart;
	}

	public String getLastLoginTimeStart() {
		return this.lastLoginTimeStart;
	}

	public void setLastLoginTimeEnd(String lastLoginTimeEnd) {
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}

	public String getLastLoginTimeEnd() {
		return this.lastLoginTimeEnd;
	}

	public void setLastLoginIpFuzzy(String lastLoginIpFuzzy) {
		this.lastLoginIpFuzzy = lastLoginIpFuzzy;
	}

	public String getLastLoginIpFuzzy() {
		return this.lastLoginIpFuzzy;
	}

	public void setLastLoginIpAddressFuzzy(String lastLoginIpAddressFuzzy) {
		this.lastLoginIpAddressFuzzy = lastLoginIpAddressFuzzy;
	}

	public String getLastLoginIpAddressFuzzy() {
		return this.lastLoginIpAddressFuzzy;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getSex() {
		return this.sex;
	}

	public void setPersonDescription(String personDescription) {
		this.personDescription = personDescription;
	}

	public String getPersonDescription() {
		return this.personDescription;
	}

	public void setJoinTime(LocalDateTime joinTime) {
		this.joinTime = joinTime;
	}

	public LocalDateTime getJoinTime() {
		return this.joinTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public LocalDateTime getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIpAddress(String lastLoginIpAddress) {
		this.lastLoginIpAddress = lastLoginIpAddress;
	}

	public String getLastLoginIpAddress() {
		return this.lastLoginIpAddress;
	}

	public void setTotalIntegral(Integer totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

	public Integer getTotalIntegral() {
		return this.totalIntegral;
	}

	public void setCurrentIntegral(Integer currentIntegral) {
		this.currentIntegral = currentIntegral;
	}

	public Integer getCurrentIntegral() {
		return this.currentIntegral;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}


}