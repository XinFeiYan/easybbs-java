package com.jiang.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.jiang.Enums.TimeFormatEnums;
import com.jiang.Utils.TimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ForumArticle implements Serializable {
	/**
     * @Description(描述):文章ID
	 */
	private String articleId;

	/**
     * @Description(描述):板块ID
	 */
	private Integer boardId;

	/**
     * @Description(描述):板块名称
	 */
	private String boardName;

	/**
     * @Description(描述):父级板块ID
	 */
	private Integer pBoardId;

	/**
     * @Description(描述):父板块名称
	 */
	private String pBoardName;

	/**
     * @Description(描述):用户ID
	 */
	//有问题注意一下
	//@JsonIgnore
	private String userId;

	/**
     * @Description(描述):昵称
	 */
	private String nickName;

	/**
     * @Description(描述):最后登录ip地址
	 */
	private String userIpAddress;

	/**
     * @Description(描述):标题
	 */
	private String title;

	/**
     * @Description(描述):封面
	 */
	private String cover;

	/**
     * @Description(描述):内容
	 */
	private String content;

	/**
     * @Description(描述):markdown内容
	 */
	private String markdownContent;

	/**
     * @Description(描述):0:富文本编辑器 1:markdown编辑器
	 */
	private Integer editorType;

	/**
     * @Description(描述):摘要
	 */
	private String summary;

	/**
     * @Description(描述):发布时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime postTime;

	/**
     * @Description(描述):最后更新时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdateTime;

	/**
     * @Description(描述):阅读数量
	 */
	private Integer readCount;

	/**
     * @Description(描述):点赞数
	 */
	private Integer goodCount;

	/**
     * @Description(描述):评论数
	 */
	private Integer commentCount;

	/**
     * @Description(描述):0未置顶  1:已置顶
	 */
	private Integer topType;

	/**
     * @Description(描述):0:没有附件  1:有附件
	 */
	private Integer attachmentType;

	/**
     * @Description(描述):-1已删除 0:待审核  1:已审核 
	 */
	private Integer status;

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getArticleId() {
		return this.articleId;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}

	public Integer getBoardId() {
		return this.boardId;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public String getBoardName() {
		return this.boardName;
	}

	public void setPBoardId(Integer pBoardId) {
		this.pBoardId = pBoardId;
	}

	public Integer getPBoardId() {
		return this.pBoardId;
	}

	public void setPBoardName(String pBoardName) {
		this.pBoardName = pBoardName;
	}

	public String getPBoardName() {
		return this.pBoardName;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCover() {
		return this.cover;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setMarkdownContent(String markdownContent) {
		this.markdownContent = markdownContent;
	}

	public String getMarkdownContent() {
		return this.markdownContent;
	}

	public void setEditorType(Integer editorType) {
		this.editorType = editorType;
	}

	public Integer getEditorType() {
		return this.editorType;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}

	public LocalDateTime getPostTime() {
		return this.postTime;
	}

	public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public LocalDateTime getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public Integer getReadCount() {
		return this.readCount;
	}

	public void setGoodCount(Integer goodCount) {
		this.goodCount = goodCount;
	}

	public Integer getGoodCount() {
		return this.goodCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getCommentCount() {
		return this.commentCount;
	}

	public void setTopType(Integer topType) {
		this.topType = topType;
	}

	public Integer getTopType() {
		return this.topType;
	}

	public void setAttachmentType(Integer attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Integer getAttachmentType() {
		return this.attachmentType;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return "文章ID:" + (articleId == null ? "空" : articleId) + ",板块ID:" + (boardId == null ? "空" : boardId) + ",板块名称:" + (boardName == null ? "空" : boardName) + ",父级板块ID:" + (pBoardId == null ? "空" : pBoardId) + ",父板块名称:" + (pBoardName == null ? "空" : pBoardName) + ",用户ID:" + (userId == null ? "空" : userId) + ",昵称:" + (nickName == null ? "空" : nickName) + ",最后登录ip地址:" + (userIpAddress == null ? "空" : userIpAddress) + ",标题:" + (title == null ? "空" : title) + ",封面:" + (cover == null ? "空" : cover) + ",内容:" + (content == null ? "空" : content) + ",markdown内容:" + (markdownContent == null ? "空" : markdownContent) + ",0:富文本编辑器 1:markdown编辑器:" + (editorType == null ? "空" : editorType) + ",摘要:" + (summary == null ? "空" : summary) + ",发布时间:" + (postTime == null ? "空" : TimeUtils.format(postTime, TimeFormatEnums.ISO_LOCAL_DATE_TIME_REPLACET2SPACE.getFormat())) + ",最后更新时间:" + (lastUpdateTime == null ? "空" : TimeUtils.format(lastUpdateTime, TimeFormatEnums.ISO_LOCAL_DATE_TIME_REPLACET2SPACE.getFormat())) + ",阅读数量:" + (readCount == null ? "空" : readCount) + ",点赞数:" + (goodCount == null ? "空" : goodCount) + ",评论数:" + (commentCount == null ? "空" : commentCount) + ",0未置顶  1:已置顶:" + (topType == null ? "空" : topType) + ",0:没有附件  1:有附件:" + (attachmentType == null ? "空" : attachmentType) + ",-1已删除 0:待审核  1:已审核 :" + (status == null ? "空" : status) ;
	}
}