package com.jiang.entity.query;

import java.time.LocalDateTime;


/**
 * @Description(描述):文章信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class ForumArticleQuery extends BaseQuery {
	/**
     * @Description(描述):文章ID
	 */
	private String articleId;

	private String articleIdFuzzy;

	/**
     * @Description(描述):板块ID
	 */
	private Integer boardId;

	/**
     * @Description(描述):板块名称
	 */
	private String boardName;

	private String boardNameFuzzy;

	/**
     * @Description(描述):父级板块ID
	 */
	private Integer pBoardId;

	/**
     * @Description(描述):父板块名称
	 */
	private String pBoardName;

	private String pBoardNameFuzzy;

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
     * @Description(描述):最后登录ip地址
	 */
	private String userIpAddress;

	private String userIpAddressFuzzy;

	/**
     * @Description(描述):标题
	 */
	private String title;

	private String titleFuzzy;

	/**
     * @Description(描述):封面
	 */
	private String cover;

	private String coverFuzzy;

	/**
     * @Description(描述):内容
	 */
	private String content;

	private String contentFuzzy;

	/**
     * @Description(描述):markdown内容
	 */
	private String markdownContent;

	private String markdownContentFuzzy;

	/**
     * @Description(描述):0:富文本编辑器 1:markdown编辑器
	 */
	private Integer editorType;

	/**
     * @Description(描述):摘要
	 */
	private String summary;

	private String summaryFuzzy;

	/**
     * @Description(描述):发布时间
	 */
	private LocalDateTime postTime;

	private String postTimeStart;

	private String postTimeEnd;

	/**
     * @Description(描述):最后更新时间
	 */
	private LocalDateTime lastUpdateTime;

	private String lastUpdateTimeStart;

	private String lastUpdateTimeEnd;

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

	/**
	 * 当前用户id,用于文章查询
	 */
	private String currentUserId;

	/**
	 * 按照评论排序，或许点赞
	 */
	private String commentUserId;

	private String likeUserId;

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public void setArticleIdFuzzy(String articleIdFuzzy) {
		this.articleIdFuzzy = articleIdFuzzy;
	}

	public String getArticleIdFuzzy() {
		return this.articleIdFuzzy;
	}

	public void setBoardNameFuzzy(String boardNameFuzzy) {
		this.boardNameFuzzy = boardNameFuzzy;
	}

	public String getBoardNameFuzzy() {
		return this.boardNameFuzzy;
	}

	public void setPBoardNameFuzzy(String pBoardNameFuzzy) {
		this.pBoardNameFuzzy = pBoardNameFuzzy;
	}

	public String getPBoardNameFuzzy() {
		return this.pBoardNameFuzzy;
	}

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

	public void setUserIpAddressFuzzy(String userIpAddressFuzzy) {
		this.userIpAddressFuzzy = userIpAddressFuzzy;
	}

	public String getUserIpAddressFuzzy() {
		return this.userIpAddressFuzzy;
	}

	public void setTitleFuzzy(String titleFuzzy) {
		this.titleFuzzy = titleFuzzy;
	}

	public String getTitleFuzzy() {
		return this.titleFuzzy;
	}

	public void setCoverFuzzy(String coverFuzzy) {
		this.coverFuzzy = coverFuzzy;
	}

	public String getCoverFuzzy() {
		return this.coverFuzzy;
	}

	public void setContentFuzzy(String contentFuzzy) {
		this.contentFuzzy = contentFuzzy;
	}

	public String getContentFuzzy() {
		return this.contentFuzzy;
	}

	public void setMarkdownContentFuzzy(String markdownContentFuzzy) {
		this.markdownContentFuzzy = markdownContentFuzzy;
	}

	public String getMarkdownContentFuzzy() {
		return this.markdownContentFuzzy;
	}

	public void setSummaryFuzzy(String summaryFuzzy) {
		this.summaryFuzzy = summaryFuzzy;
	}

	public String getSummaryFuzzy() {
		return this.summaryFuzzy;
	}

	public void setPostTimeStart(String postTimeStart) {
		this.postTimeStart = postTimeStart;
	}

	public String getPostTimeStart() {
		return this.postTimeStart;
	}

	public void setPostTimeEnd(String postTimeEnd) {
		this.postTimeEnd = postTimeEnd;
	}

	public String getPostTimeEnd() {
		return this.postTimeEnd;
	}

	public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
		this.lastUpdateTimeStart = lastUpdateTimeStart;
	}

	public String getLastUpdateTimeStart() {
		return this.lastUpdateTimeStart;
	}

	public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
		this.lastUpdateTimeEnd = lastUpdateTimeEnd;
	}

	public String getLastUpdateTimeEnd() {
		return this.lastUpdateTimeEnd;
	}

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

	public String getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}

	public String getLikeUserId() {
		return likeUserId;
	}

	public void setLikeUserId(String likeUserId) {
		this.likeUserId = likeUserId;
	}
}