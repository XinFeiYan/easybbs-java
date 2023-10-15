package com.jiang.entity.query;



/**
 * @Description(描述):文件信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class ForumArticleAttachmentQuery extends BaseQuery {
	/**
     * @Description(描述):文件ID
	 */
	private String fileId;

	private String fileIdFuzzy;

	/**
     * @Description(描述):文章ID
	 */
	private String articleId;

	private String articleIdFuzzy;

	/**
     * @Description(描述):用户id
	 */
	private String userId;

	private String userIdFuzzy;

	/**
     * @Description(描述):文件大小
	 */
	private Long fileSize;

	/**
     * @Description(描述):文件名称
	 */
	private String fileName;

	private String fileNameFuzzy;

	/**
     * @Description(描述):下载次数
	 */
	private Integer downloadCount;

	/**
     * @Description(描述):文件路径
	 */
	private String filePath;

	private String filePathFuzzy;

	/**
     * @Description(描述):文件类型
	 */
	private Integer fileType;

	/**
     * @Description(描述):下载所需积分
	 */
	private Integer integral;

	public void setFileIdFuzzy(String fileIdFuzzy) {
		this.fileIdFuzzy = fileIdFuzzy;
	}

	public String getFileIdFuzzy() {
		return this.fileIdFuzzy;
	}

	public void setArticleIdFuzzy(String articleIdFuzzy) {
		this.articleIdFuzzy = articleIdFuzzy;
	}

	public String getArticleIdFuzzy() {
		return this.articleIdFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setFileNameFuzzy(String fileNameFuzzy) {
		this.fileNameFuzzy = fileNameFuzzy;
	}

	public String getFileNameFuzzy() {
		return this.fileNameFuzzy;
	}

	public void setFilePathFuzzy(String filePathFuzzy) {
		this.filePathFuzzy = filePathFuzzy;
	}

	public String getFilePathFuzzy() {
		return this.filePathFuzzy;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getArticleId() {
		return this.articleId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	public Integer getDownloadCount() {
		return this.downloadCount;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getIntegral() {
		return this.integral;
	}


}