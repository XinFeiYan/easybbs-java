package com.jiang.entity.query;



/**
 * @Description(描述):用户附件下载
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class ForumArticleAttachmentDownloadQuery extends BaseQuery {
	/**
     * @Description(描述):文件ID
	 */
	private String fileId;

	private String fileIdFuzzy;

	/**
     * @Description(描述):用户id
	 */
	private String userId;

	private String userIdFuzzy;

	/**
     * @Description(描述):文章ID
	 */
	private String articleId;

	private String articleIdFuzzy;

	/**
     * @Description(描述):文件下载次数
	 */
	private Integer downloadCount;

	public void setFileIdFuzzy(String fileIdFuzzy) {
		this.fileIdFuzzy = fileIdFuzzy;
	}

	public String getFileIdFuzzy() {
		return this.fileIdFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setArticleIdFuzzy(String articleIdFuzzy) {
		this.articleIdFuzzy = articleIdFuzzy;
	}

	public String getArticleIdFuzzy() {
		return this.articleIdFuzzy;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getArticleId() {
		return this.articleId;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	public Integer getDownloadCount() {
		return this.downloadCount;
	}


}