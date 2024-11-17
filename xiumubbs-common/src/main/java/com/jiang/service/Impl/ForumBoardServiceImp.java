package com.jiang.service.Impl;


import com.jiang.Exception.BusinessException;
import com.jiang.entity.po.ForumArticle;
import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.query.ForumBoardQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.entity.vo.ForumArticleDetailVO;
import com.jiang.mapper.ForumArticleDao;
import com.jiang.service.ForumBoardService;
import com.jiang.mapper.ForumBoardDao;
import com.jiang.entity.po.ForumBoard;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
	@Resource
	private ForumArticleDao<ForumArticle, ForumArticleQuery> forumArticleDao;
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

	@Override
	public List<ForumBoard> getBoardTree(Integer postType){
		ForumBoardQuery boardQuery = new ForumBoardQuery();
		boardQuery.setOrderBy("sort asc");//排序
		boardQuery.setPostType(postType);
		List<ForumBoard> forumBoardList = forumBoardDao.selectList(boardQuery);
		return  convertLine2Tree(forumBoardList,0);
	}

	//将线性集合，转化为树形集合
	private List<ForumBoard> convertLine2Tree(List<ForumBoard> dataList,Integer pid){
		List<ForumBoard> children = new ArrayList<>();
		for(ForumBoard m : dataList){
			//如果当前对象的pid等于传进来的id,表示该标签是同级别，将他装入同一级
			if(m.getPBoardId().equals(pid)){
				//对下一级标签装入，把当前id当成下一个的父id
				m.setChildren(convertLine2Tree(dataList,m.getBoardId()));
				children.add(m);
			}
		}
		return children;
	}

	public void saveForumBoard(ForumBoard forumBoard){
		if(forumBoard.getBoardId()==null){
			//新增
			ForumBoardQuery query = new ForumBoardQuery();
			query.setPBoardId(forumBoard.getPBoardId());
			Integer count = this.forumBoardDao.selectCount(query);
			//排序名称加1
			forumBoard.setSort(count+1);
			this.forumBoardDao.insert(forumBoard);
		}else {
			//修改
			ForumBoard dbInfo = this.forumBoardDao.selectByBoardId(forumBoard.getBoardId());
			if(dbInfo==null){
				throw new BusinessException("版块信息不存在");
			}
			this.forumBoardDao.updateByBoardId(forumBoard,forumBoard.getBoardId());
			if(!dbInfo.getBoardName().equals(forumBoard.getBoardName())){
				this.forumArticleDao.updateBoardNameBatch(dbInfo.getPBoardId()==0?0:1,forumBoard.getBoardName(),forumBoard.getBoardId());
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void changeSort(String boardIds){
			String[] boardIdArray = boardIds.split(",");
			Integer index = 1;
			for(String boardIdStr:boardIdArray){
				Integer boardId = Integer.parseInt(boardIdStr);
				ForumBoard forumBoard = new ForumBoard();
				forumBoard.setSort(index);
				forumBoardDao.updateByBoardId(forumBoard,boardId);
				index++;
			}
	}










}