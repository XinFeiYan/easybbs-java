package com.jiang.service.Impl;


import com.jiang.entity.query.ForumBoardQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.ForumBoardService;
import com.jiang.mapper.ForumBoardDao;
import com.jiang.entity.po.ForumBoard;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):ForumBoardServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("forumBoardService")
public class ForumBoardServiceImp implements ForumBoardService {


	@Resource
	private ForumBoardDao<ForumBoard,ForumBoardQuery> forumBoardDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<ForumBoard> findListByParam(ForumBoardQuery query){
		return this.forumBoardDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(ForumBoardQuery query){
		return this.forumBoardDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<ForumBoard> findListByPage(ForumBoardQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<ForumBoard> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<ForumBoard> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(ForumBoard bean){
		return this.forumBoardDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<ForumBoard> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumBoardDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<ForumBoard> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumBoardDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据BoardId查询
	 */
	@Override
	public ForumBoard getByBoardId(Integer boardId){
		return this.forumBoardDao.selectByBoardId(boardId);
	}

	/**
     * @Description(描述):根据BoardId更新
	 */
	@Override
	public Integer updateByBoardId(ForumBoard bean, Integer boardId){
		return this.forumBoardDao.updateByBoardId(bean,boardId);
	}


	/**
     * @Description(描述):根据BoardId删除
	 */
	@Override
	public Integer deleteByBoardId(Integer boardId){
		return this.forumBoardDao.deleteByBoardId(boardId);
	}


}