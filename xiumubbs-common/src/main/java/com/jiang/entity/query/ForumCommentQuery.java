package com.jiang.entity.query;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @Description(描述):评论
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class ForumCommentQuery extends BaseQuery {
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

	private String articleIdFuzzy;

	/**
     * @Description(描述):回复内容
	 */
	private String content;

	private String contentFuzzy;

	/**
     * @Description(描述):图片
	 */
	private String imgPath;

	private String imgPathFuzzy;

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
     * @Description(描述):用户ip地址
	 */
	private String userIpAddress;

	private String userIpAddressFuzzy;

	/**
     * @Description(描述):回复人ID
	 */
	private String replyUserId;

	private String replyUserIdFuzzy;

	/**
     * @Description(描述):回复人昵称
	 */
	private String replyNickName;

	private String replyNickNameFuzzy;

	/**
     * @Description(描述):0:未置顶  1:置顶
	 */
	private Integer topType;

	/**
     * @Description(描述):发布时间
	 */
	private LocalDateTime postTime;

	private String postTimeStart;

	private String postTimeEnd;

	/**
     * @Description(描述):good数量
	 */
	private Integer goodCount;

	/**
     * @Description(描述):0:待审核  1:已审核
	 */
	private Integer status;
	/**
	 * 评论可见
	 */
	private String currentUserId;

	/**
	 * 点赞可见自己
	 */
	private Boolean queryLikeType;


	private Boolean loadChildren;

	/**
	 * 父评论id，只查对应父评论下的子评论
	 */
	private List<Integer> pcommentIdList;


	public List<Integer> getPcommentIdList() {
		return pcommentIdList;
	}

	public void setPcommentIdList(List<Integer> pcommentIdList) {
		this.pcommentIdList = pcommentIdList;
	}

	public Boolean getLoadChildren() {
		return loadChildren;
	}

	public void setLoadChildren(Boolean loadChildren) {
		this.loadChildren = loadChildren;
	}

	public Boolean getQueryLikeType() {
		return queryLikeType;
	}

	public void setQueryLikeType(Boolean queryLikeType) {
		this.queryLikeType = queryLikeType;
	}

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

	public void setContentFuzzy(String contentFuzzy) {
		this.contentFuzzy = contentFuzzy;
	}

	public String getContentFuzzy() {
		return this.contentFuzzy;
	}

	public void setImgPathFuzzy(String imgPathFuzzy) {
		this.imgPathFuzzy = imgPathFuzzy;
	}

	public String getImgPathFuzzy() {
		return this.imgPathFuzzy;
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

	public void setReplyUserIdFuzzy(String replyUserIdFuzzy) {
		this.replyUserIdFuzzy = replyUserIdFuzzy;
	}

	public String getReplyUserIdFuzzy() {
		return this.replyUserIdFuzzy;
	}

	public void setReplyNickNameFuzzy(String replyNickNameFuzzy) {
		this.replyNickNameFuzzy = replyNickNameFuzzy;
	}

	public String getReplyNickNameFuzzy() {
		return this.replyNickNameFuzzy;
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


}