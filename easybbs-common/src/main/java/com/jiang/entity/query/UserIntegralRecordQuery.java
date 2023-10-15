package com.jiang.entity.query;

import java.time.LocalDateTime;


/**
 * @Description(描述):用户积分记录表
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class UserIntegralRecordQuery extends BaseQuery {
	/**
     * @Description(描述):记录ID
	 */
	private Integer recordId;

	/**
     * @Description(描述):用户ID
	 */
	private String userId;

	private String userIdFuzzy;

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
	private LocalDateTime createTime;

	private String createTimeStart;

	private String createTimeEnd;

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart() {
		return this.createTimeStart;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd() {
		return this.createTimeEnd;
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


}