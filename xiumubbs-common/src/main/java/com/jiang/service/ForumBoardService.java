package com.jiang.service;


import com.jiang.entity.po.ForumBoard;
import com.jiang.entity.query.ForumBoardQuery;
import com.jiang.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * @Description(描述):ForumBoardService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface ForumBoardService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<ForumBoard> findListByParam(ForumBoardQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(ForumBoardQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<ForumBoard> findListByPage(ForumBoardQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(ForumBoard bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<ForumBoard> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ForumBoard> listBean);
	/**
     * @Description(描述):根据BoardId查询
	 */
	ForumBoard getByBoardId(Integer boardId);

	/**
     * @Description(描述):根据BoardId更新
	 */
	Integer updateByBoardId(ForumBoard bean, Integer boardId);

	/**
     * @Description(描述):根据BoardId删除
	 */
	Integer deleteByBoardId(Integer boardId);

	//根据类型获取板块信息
	public List<ForumBoard> getBoardTree(Integer postType);

	public void saveForumBoard(ForumBoard forumBoard);

	public void changeSort(String boardIds);
}