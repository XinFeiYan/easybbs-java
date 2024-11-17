package com.jiang.entity.query;

import java.time.LocalDateTime;


/**
 * @Description(描述):邮箱验证码
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class EmailCodeQuery extends BaseQuery {
	/**
     * @Description(描述):邮箱
	 */
	private String email;

	private String emailFuzzy;

	/**
     * @Description(描述):编号
	 */
	private String code;

	private String codeFuzzy;

	/**
     * @Description(描述):创建时间
	 */
	private LocalDateTime createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
     * @Description(描述):0:未使用  1:已使用
	 */
	private Integer status;

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy() {
		return this.emailFuzzy;
	}

	public void setCodeFuzzy(String codeFuzzy) {
		this.codeFuzzy = codeFuzzy;
	}

	public String getCodeFuzzy() {
		return this.codeFuzzy;
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


}