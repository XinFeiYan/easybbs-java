package com.jiang.service.Impl;


import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.ForumArticleAttachment;
import com.jiang.entity.query.ForumArticleAttachmentDownloadQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.ForumArticleAttachmentDownloadService;
import com.jiang.mapper.ForumArticleAttachmentDownloadDao;
import com.jiang.entity.po.ForumArticleAttachmentDownload;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):ForumArticleAttachmentDownloadServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("forumArticleAttachmentDownloadService")
public class ForumArticleAttachmentDownloadServiceImp implements ForumArticleAttachmentDownloadService {


	@Resource
	private ForumArticleAttachmentDownloadDao<ForumArticleAttachmentDownload,ForumArticleAttachmentDownloadQuery> forumArticleAttachmentDownloadDao;

	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<ForumArticleAttachmentDownload> findListByParam(ForumArticleAttachmentDownloadQuery query){
		return this.forumArticleAttachmentDownloadDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(ForumArticleAttachmentDownloadQuery query){
		return this.forumArticleAttachmentDownloadDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<ForumArticleAttachmentDownload> findListByPage(ForumArticleAttachmentDownloadQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<ForumArticleAttachmentDownload> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<ForumArticleAttachmentDownload> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(ForumArticleAttachmentDownload bean){
		return this.forumArticleAttachmentDownloadDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<ForumArticleAttachmentDownload> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumArticleAttachmentDownloadDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<ForumArticleAttachmentDownload> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.forumArticleAttachmentDownloadDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据FileIdAndUserId查询
	 */
	@Override
	public ForumArticleAttachmentDownload getByFileIdAndUserId(String fileId, String userId){
		return this.forumArticleAttachmentDownloadDao.selectByFileIdAndUserId(fileId,userId);
	}

	/**
     * @Description(描述):根据FileIdAndUserId更新
	 */
	@Override
	public Integer updateByFileIdAndUserId(ForumArticleAttachmentDownload bean, String fileId, String userId){
		return this.forumArticleAttachmentDownloadDao.updateByFileIdAndUserId(bean,fileId,userId);
	}


	/**
     * @Description(描述):根据FileIdAndUserId删除
	 */
	@Override
	public Integer deleteByFileIdAndUserId(String fileId, String userId){
		return this.forumArticleAttachmentDownloadDao.deleteByFileIdAndUserId(fileId,userId);
	}



}