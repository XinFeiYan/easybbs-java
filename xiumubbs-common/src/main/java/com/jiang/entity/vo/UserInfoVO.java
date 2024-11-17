package com.jiang.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiang.Enums.TimeFormatEnums;
import com.jiang.Utils.TimeUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description(描述):用户信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class UserInfoVO implements Serializable {
	/**
     * @Description(描述):用户ID
	 */
	private String userId;

	/**
     * @Description(描述):昵称
	 */
	private String nickName;


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
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private LocalDateTime joinTime;

	/**
     * @Description(描述):最后登录时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private LocalDateTime lastLoginTime;


	/**
     * @Description(描述):当前积分
	 */
	private Integer currentIntegral;

	private Integer postCount;

	private Integer likeCount;


	public Integer getPostCount() {
		return postCount;
	}

	public void setPostCount(Integer postCount) {
		this.postCount = postCount;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
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

	public void setCurrentIntegral(Integer currentIntegral) {
		this.currentIntegral = currentIntegral;
	}

	public Integer getCurrentIntegral() {
		return this.currentIntegral;
	}

	@Override
	public String toString() {
		return "UserInfoVO{" +
				"userId='" + userId + '\'' +
				", nickName='" + nickName + '\'' +
				", sex=" + sex +
				", personDescription='" + personDescription + '\'' +
				", joinTime=" + joinTime +
				", lastLoginTime=" + lastLoginTime +
				", currentIntegral=" + currentIntegral +
				", postCount=" + postCount +
				", likeCount=" + likeCount +
				'}';
	}
}