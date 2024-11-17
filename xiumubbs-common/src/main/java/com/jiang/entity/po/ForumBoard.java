package com.jiang.entity.po;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Description(描述):文章板块信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class ForumBoard implements Serializable {
	/**
     * @Description(描述):板块ID
	 */
	private Integer boardId;

	/**
     * @Description(描述):父级板块ID
	 */


	private Integer pBoardId;

	/**
     * @Description(描述):板块名
	 */
	private String boardName;

	/**
     * @Description(描述):封面
	 */
	private String cover;

	/**
     * @Description(描述):描述
	 */
	private String boardDesc;

	/**
     * @Description(描述):排序
	 */
	private Integer sort;

	/**
     * @Description(描述):0:只允许管理员发帖 1:任何人可以发帖
	 */
	private Integer postType;

	/**
	 * 转化为树形列表用的，装下一级标签
	 */
	List<ForumBoard> children;

	public List<ForumBoard> getChildren() {
		return children;
	}

	public void setChildren(List<ForumBoard> children) {
		this.children = children;
	}

	public void setBoardId(Integer boardId) {
		this.boardId = boardId;
	}


	public Integer getBoardId() {
		return this.boardId;
	}

	public void setPBoardId(Integer pBoardId) {
		this.pBoardId = pBoardId;
	}

	@JsonProperty("pBoardId")
	public Integer getPBoardId() {
		return this.pBoardId;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public String getBoardName() {
		return this.boardName;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCover() {
		return this.cover;
	}

	public void setBoardDesc(String boardDesc) {
		this.boardDesc = boardDesc;
	}

	public String getBoardDesc() {
		return this.boardDesc;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setPostType(Integer postType) {
		this.postType = postType;
	}

	public Integer getPostType() {
		return this.postType;
	}

	@Override
	public String toString() {
		return "板块ID:" + (boardId == null ? "空" : boardId) + ",父级板块ID:" + (pBoardId == null ? "空" : pBoardId) + ",板块名:" + (boardName == null ? "空" : boardName) + ",封面:" + (cover == null ? "空" : cover) + ",描述:" + (boardDesc == null ? "空" : boardDesc) + ",排序:" + (sort == null ? "空" : sort) + ",0:只允许管理员发帖 1:任何人可以发帖:" + (postType == null ? "空" : postType) ;
	}
}