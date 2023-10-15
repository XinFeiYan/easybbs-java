package com.jiang.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jiang.Enums.TimeFormatEnums;
import com.jiang.Utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description(描述):点赞记录
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class LikeRecord implements Serializable {
	/**
     * @Description(描述):自增ID
	 */
	private Integer opId;

	/**
     * @Description(描述):操作类型0:文章点赞 1:评论点赞
	 */
	private Integer opType;

	/**
     * @Description(描述):主体ID
	 */
	private String objectId;

	/**
     * @Description(描述):用户ID
	 */
	@JsonIgnore
	private String userId;

	/**
     * @Description(描述):发布时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	/**
     * @Description(描述):主体作者ID
	 */
	private String authorUserId;

	public void setOpId(Integer opId) {
		this.opId = opId;
	}

	public Integer getOpId() {
		return this.opId;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	public Integer getOpType() {
		return this.opType;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectId() {
		return this.objectId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getCreateTime() {
		return this.createTime;
	}

	public void setAuthorUserId(String authorUserId) {
		this.authorUserId = authorUserId;
	}

	public String getAuthorUserId() {
		return this.authorUserId;
	}

	@Override
	public String toString() {
		return "自增ID:" + (opId == null ? "空" : opId) + ",操作类型0:文章点赞 1:评论点赞:" + (opType == null ? "空" : opType) + ",主体ID:" + (objectId == null ? "空" : objectId) + ",用户ID:" + (userId == null ? "空" : userId) + ",发布时间:" + (createTime == null ? "空" : TimeUtils.format(createTime, TimeFormatEnums.ISO_LOCAL_DATE_TIME_REPLACET2SPACE.getFormat())) + ",主体作者ID:" + (authorUserId == null ? "空" : authorUserId) ;
	}
}