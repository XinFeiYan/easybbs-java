package com.jiang.entity.query;



/**
 * @Description(描述):文章板块信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class ForumBoardQuery extends BaseQuery {
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

	private String boardNameFuzzy;

	/**
     * @Description(描述):封面
	 */
	private String cover;

	private String coverFuzzy;

	/**
     * @Description(描述):描述
	 */
	private String boardDesc;

	private String boardDescFuzzy;

	/**
     * @Description(描述):排序
	 */
	private Integer sort;

	/**
     * @Description(描述):0:只允许管理员发帖 1:任何人可以发帖
	 */
	private Integer postType;

	public void setBoardNameFuzzy(String boardNameFuzzy) {
		this.boardNameFuzzy = boardNameFuzzy;
	}

	public String getBoardNameFuzzy() {
		return this.boardNameFuzzy;
	}

	public void setCoverFuzzy(String coverFuzzy) {
		this.coverFuzzy = coverFuzzy;
	}

	public String getCoverFuzzy() {
		return this.coverFuzzy;
	}

	public void setBoardDescFuzzy(String boardDescFuzzy) {
		this.boardDescFuzzy = boardDescFuzzy;
	}

	public String getBoardDescFuzzy() {
		return this.boardDescFuzzy;
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


}