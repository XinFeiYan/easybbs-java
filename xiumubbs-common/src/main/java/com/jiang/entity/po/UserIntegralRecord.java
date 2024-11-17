package com.jiang.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jiang.Enums.TimeFormatEnums;
import com.jiang.Enums.UserIntegralOperTypeEnum;
import com.jiang.Utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description(描述):用户积分记录表
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class UserIntegralRecord implements Serializable {
	/**
     * @Description(描述):记录ID
	 */
	private Integer recordId;

	/**
     * @Description(描述):用户ID
	 */
	@JsonIgnore
	private String userId;

	/**
     * @Description(描述):操作类型
	 */
	private Integer operType;

	/**
     * @Description(描述):积分
	 */
	private Integer integral;

	/**
     * @Description(描述):创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	private String operTypeName;

	public String getOperTypeName() {
		UserIntegralOperTypeEnum typeEnum = UserIntegralOperTypeEnum.getByType(operType);
		return typeEnum==null? "" : typeEnum.getDesc();
	}

	public void setOperTypeName(String operTypeName) {
		this.operTypeName = operTypeName;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getRecordId() {
		return this.recordId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public Integer getOperType() {
		return this.operType;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getIntegral() {
		return this.integral;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getCreateTime() {
		return this.createTime;
	}

	@Override
	public String toString() {
		return "记录ID:" + (recordId == null ? "空" : recordId) + ",用户ID:" + (userId == null ? "空" : userId) + ",操作类型:" + (operType == null ? "空" : operType) + ",积分:" + (integral == null ? "空" : integral) + ",创建时间:" + (createTime == null ? "空" : TimeUtils.format(createTime, TimeFormatEnums.ISO_LOCAL_DATE_TIME_REPLACET2SPACE.getFormat())) ;
	}
}