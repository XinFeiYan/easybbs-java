package com.jiang.service.Impl;


import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.ForumArticleService;
import com.jiang.mapper.ForumArticleDao;
import com.jiang.entity.po.ForumArticle;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):ForumArticleServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("forumArticleService")
public class ForumArticleServiceImp implements ForumArticleService {


	@Resource
	private ForumArticleDao<ForumArticle,ForumArticleQuery> forumArticleDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<ForumArticle> findListByParam(ForumArticleQuery query){
		return this.forumArticleDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(ForumArticleQuery query){
		return this.forumArticleDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<ForumArticle> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<ForumArticle> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(ForumArticle bean){
		return this.forumArticleDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<ForumArticle> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumArticleDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<ForumArticle> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumArticleDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据ArticleId查询
	 */
	@Override
	public ForumArticle getByArticleId(String articleId){
		return this.forumArticleDao.selectByArticleId(articleId);
	}

	/**
     * @Description(描述):根据ArticleId更新
	 */
	@Override
	public Integer updateByArticleId(ForumArticle bean, String articleId){
		return this.forumArticleDao.updateByArticleId(bean,articleId);
	}


	/**
     * @Description(描述):根据ArticleId删除
	 */
	@Override
	public Integer deleteByArticleId(String articleId){
		return this.forumArticleDao.deleteByArticleId(articleId);
	}


}