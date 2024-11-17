package com.jiang.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jiang.Enums.TimeFormatEnums;
import com.jiang.Utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description(描述):用户信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class UserInfo implements Serializable {
	/**
     * @Description(描述):用户ID
	 */
	private String userId;

	/**
     * @Description(描述):昵称
	 */
	private String nickName;

	/**
     * @Description(描述):邮箱
	 */
	@JsonIgnore
	private String email;

	/**
     * @Description(描述):密码
	 */
	private String password;

	/**
     * @Description(描述):0:女 1:男
	 */
	private Integer sex;

	/**
     * @Description(描述):个人描述
	 */
	private String personDescription;

	/**
     * @Description(描述):加入时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime joinTime;

	/**
     * @Description(描述):最后登录时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastLoginTime;

	/**
     * @Description(描述):最后登录IP
	 */
	private String lastLoginIp;

	/**
     * @Description(描述):最后登录ip地址
	 */
	private String lastLoginIpAddress;

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

	@Override
	public String toString() {
		return "用户ID:" + (userId == null ? "空" : userId) + ",昵称:" + (nickName == null ? "空" : nickName) + ",邮箱:" + (email == null ? "空" : email) + ",密码:" + (password == null ? "空" : password) + ",0:女 1:男:" + (sex == null ? "空" : sex) + ",个人描述:" + (personDescription == null ? "空" : personDescription) + ",加入时间:" + (joinTime == null ? "空" : TimeUtils.format(joinTime, TimeFormatEnums.ISO_LOCAL_DATE_TIME_REPLACET2SPACE.getFormat())) + ",最后登录时间:" + (lastLoginTime == null ? "空" : TimeUtils.format(lastLoginTime, TimeFormatEnums.ISO_LOCAL_DATE_TIME_REPLACET2SPACE.getFormat())) + ",最后登录IP:" + (lastLoginIp == null ? "空" : lastLoginIp) + ",最后登录ip地址:" + (lastLoginIpAddress == null ? "空" : lastLoginIpAddress) + ",积分:" + (totalIntegral == null ? "空" : totalIntegral) + ",当前积分:" + (currentIntegral == null ? "空" : currentIntegral) + ",0:禁用 1:正常:" + (status == null ? "空" : status) ;
	}
}