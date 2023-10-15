package com.jiang.service.Impl;


import com.jiang.entity.query.ForumArticleAttachmentQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.ForumArticleAttachmentService;
import com.jiang.mapper.ForumArticleAttachmentDao;
import com.jiang.entity.po.ForumArticleAttachment;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):ForumArticleAttachmentServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("forumArticleAttachmentService")
public class ForumArticleAttachmentServiceImp implements ForumArticleAttachmentService {


	@Resource
	private ForumArticleAttachmentDao<ForumArticleAttachment,ForumArticleAttachmentQuery> forumArticleAttachmentDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<ForumArticleAttachment> findListByParam(ForumArticleAttachmentQuery query){
		return this.forumArticleAttachmentDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(ForumArticleAttachmentQuery query){
		return this.forumArticleAttachmentDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<ForumArticleAttachment> findListByPage(ForumArticleAttachmentQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<ForumArticleAttachment> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<ForumArticleAttachment> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(ForumArticleAttachment bean){
		return this.forumArticleAttachmentDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<ForumArticleAttachment> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumArticleAttachmentDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<ForumArticleAttachment> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumArticleAttachmentDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据FileId查询
	 */
	@Override
	public ForumArticleAttachment getByFileId(String fileId){
		return this.forumArticleAttachmentDao.selectByFileId(fileId);
	}

	/**
     * @Description(描述):根据FileId更新
	 */
	@Override
	public Integer updateByFileId(ForumArticleAttachment bean, String fileId){
		return this.forumArticleAttachmentDao.updateByFileId(bean,fileId);
	}


	/**
     * @Description(描述):根据FileId删除
	 */
	@Override
	public Integer deleteByFileId(String fileId){
		return this.forumArticleAttachmentDao.deleteByFileId(fileId);
	}


}