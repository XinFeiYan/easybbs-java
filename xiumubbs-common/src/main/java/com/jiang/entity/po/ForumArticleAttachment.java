package com.jiang.entity.po;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description(描述):文件信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class ForumArticleAttachment implements Serializable {
	/**
     * @Description(描述):文件ID
	 */
	private String fileId;

	/**
     * @Description(描述):文章ID
	 */
	private String articleId;

	/**
     * @Description(描述):用户id
	 */
	@JsonIgnore
	private String userId;

	/**
     * @Description(描述):文件大小
	 */
	private Long fileSize;

	/**
     * @Description(描述):文件名称
	 */
	private String fileName;

	/**
     * @Description(描述):下载次数
	 */
	private Integer downloadCount;

	/**
     * @Description(描述):文件路径
	 */
	private String filePath;

	/**
     * @Description(描述):文件类型
	 */
	private Integer fileType;

	/**
     * @Description(描述):下载所需积分
	 */
	private Integer integral;

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

	@Override
	public String toString() {
		return "文件ID:" + (fileId == null ? "空" : fileId) + ",文章ID:" + (articleId == null ? "空" : articleId) + ",用户id:" + (userId == null ? "空" : userId) + ",文件大小:" + (fileSize == null ? "空" : fileSize) + ",文件名称:" + (fileName == null ? "空" : fileName) + ",下载次数:" + (downloadCount == null ? "空" : downloadCount) + ",文件路径:" + (filePath == null ? "空" : filePath) + ",文件类型:" + (fileType == null ? "空" : fileType) + ",下载所需积分:" + (integral == null ? "空" : integral) ;
	}
}