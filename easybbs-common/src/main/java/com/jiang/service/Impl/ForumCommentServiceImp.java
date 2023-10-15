package com.jiang.service.Impl;


import com.jiang.entity.query.ForumCommentQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.ForumCommentService;
import com.jiang.mapper.ForumCommentDao;
import com.jiang.entity.po.ForumComment;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):ForumCommentServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("forumCommentService")
public class ForumCommentServiceImp implements ForumCommentService {


	@Resource
	private ForumCommentDao<ForumComment,ForumCommentQuery> forumCommentDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<ForumComment> findListByParam(ForumCommentQuery query){
		return this.forumCommentDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(ForumCommentQuery query){
		return this.forumCommentDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<ForumComment> findListByPage(ForumCommentQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<ForumComment> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<ForumComment> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(ForumComment bean){
		return this.forumCommentDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<ForumComment> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumCommentDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<ForumComment> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumCommentDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据CommentId查询
	 */
	@Override
	public ForumComment getByCommentId(Integer commentId){
		return this.forumCommentDao.selectByCommentId(commentId);
	}

	/**
     * @Description(描述):根据CommentId更新
	 */
	@Override
	public Integer updateByCommentId(ForumComment bean, Integer commentId){
		return this.forumCommentDao.updateByCommentId(bean,commentId);
	}


	/**
     * @Description(描述):根据CommentId删除
	 */
	@Override
	public Integer deleteByCommentId(Integer commentId){
		return this.forumCommentDao.deleteByCommentId(commentId);
	}


}