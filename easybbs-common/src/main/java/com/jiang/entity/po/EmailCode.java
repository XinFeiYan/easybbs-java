package com.jiang.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.jiang.Enums.TimeFormatEnums;
import com.jiang.Utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description(描述):邮箱验证码
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class EmailCode implements Serializable {
	/**
     * @Description(描述):邮箱
	 */
	@JsonIgnore
	private String email;

	/**
     * @Description(描述):编号
	 */
	private String code;

	/**
     * @Description(描述):创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	/**
     * @Description(描述):0:未使用  1:已使用
	 */
	private Integer status;

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getCreateTime() {
		return this.createTime;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return "邮箱:" + (email == null ? "空" : email) + ",编号:" + (code == null ? "空" : code) + ",创建时间:" + (createTime == null ? "空" : TimeUtils.format(createTime, TimeFormatEnums.ISO_LOCAL_DATE_TIME_REPLACET2SPACE.getFormat())) + ",0:未使用  1:已使用:" + (status == null ? "空" : status) ;
	}
}