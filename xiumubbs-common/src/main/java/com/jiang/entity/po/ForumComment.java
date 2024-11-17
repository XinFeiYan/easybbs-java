package com.jiang.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.jiang.Enums.TimeFormatEnums;
import com.jiang.Utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description(描述):评论
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class ForumComment implements Serializable {
	/**
     * @Description(描述):评论ID
	 */
	private Integer commentId;

	/**
     * @Description(描述):父级评论ID
	 */
	private Integer pCommentId;

	/**
     * @Description(描述):文章ID
	 */
	private String articleId;

	/**
     * @Description(描述):回复内容
	 */
	private String content;

	/**
     * @Description(描述):图片
	 */
	private String imgPath;

	/**
     * @Description(描述):用户ID
	 */

	private String userId;

	/**
     * @Description(描述):昵称
	 */
	private String nickName;

	/**
     * @Description(描述):用户ip地址
	 */
	private String userIpAddress;

	/**
     * @Description(描述):回复人ID
	 */
	private String replyUserId;

	/**
     * @Description(描述):回复人昵称
	 */
	private String replyNickName;

	/**
     * @Description(描述):0:未置顶  1:置顶
	 */
	private Integer topType;

	/**
     * @Description(描述):发布时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime postTime;

	/**
     * @Description(描述):good数量
	 */
	private Integer goodCount;

	/**
     * @Description(描述):0:待审核  1:已审核
	 */
	private Integer status;

	/**
	 * 子评论
	 */
	private List<ForumComment> children;

	/**
	 * 点赞类型：0未点赞，1已点赞
	 */
	private Integer likeType;

	public Integer getLikeType() {
		return likeType;
	}

	public void setLikeType(Integer likeType) {
		this.likeType = likeType;
	}

	public List<ForumComment> getChildren() {
		return children;
	}

	public void setChildren(List<ForumComment> children) {
		this.children = children;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getCommentId() {
		return this.commentId;
	}

	public void setPCommentId(Integer pCommentId) {
		this.pCommentId = pCommentId;
	}

	public Integer getPCommentId() {
		return this.pCommentId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getArticleId() {
		return this.articleId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgPath() {
		return this.imgPath;
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

	public void setUserIpAddress(String userIpAddress) {
		this.userIpAddress = userIpAddress;
	}

	public String getUserIpAddress() {
		return this.userIpAddress;
	}

	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}

	public String getReplyUserId() {
		return this.replyUserId;
	}

	public void setReplyNickName(String replyNickName) {
		this.replyNickName = replyNickName;
	}

	public String getReplyNickName() {
		return this.replyNickName;
	}

	public void setTopType(Integer topType) {
		this.topType = topType;
	}

	public Integer getTopType() {
		return this.topType;
	}

	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}

	public LocalDateTime getPostTime() {
		return this.postTime;
	}

	public void setGoodCount(Integer goodCount) {
		this.goodCount = goodCount;
	}

	public Integer getGoodCount() {
		return this.goodCount;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return "评论ID:" + (commentId == null ? "空" : commentId) + ",父级评论ID:" + (pCommentId == null ? "空" : pCommentId) + ",文章ID:" + (articleId == null ? "空" : articleId) + ",回复内容:" + (content == null ? "空" : content) + ",图片:" + (imgPath == null ? "空" : imgPath) + ",用户ID:" + (userId == null ? "空" : userId) + ",昵称:" + (nickName == null ? "空" : nickName) + ",用户ip地址:" + (userIpAddress == null ? "空" : userIpAddress) + ",回复人ID:" + (replyUserId == null ? "空" : replyUserId) + ",回复人昵称:" + (replyNickName == null ? "空" : replyNickName) + ",0:未置顶  1:置顶:" + (topType == null ? "空" : topType) + ",发布时间:" + (postTime == null ? "空" : TimeUtils.format(postTime, TimeFormatEnums.ISO_LOCAL_DATE_TIME_REPLACET2SPACE.getFormat())) + ",good数量:" + (goodCount == null ? "空" : goodCount) + ",0:待审核  1:已审核:" + (status == null ? "空" : status) ;
	}
}