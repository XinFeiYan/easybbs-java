package com.jiang.service;


import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.ForumArticleAttachmentDownload;
import com.jiang.entity.query.ForumArticleAttachmentDownloadQuery;
import com.jiang.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * @Description(描述):ForumArticleAttachmentDownloadService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface ForumArticleAttachmentDownloadService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<ForumArticleAttachmentDownload> findListByParam(ForumArticleAttachmentDownloadQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(ForumArticleAttachmentDownloadQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<ForumArticleAttachmentDownload> findListByPage(ForumArticleAttachmentDownloadQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(ForumArticleAttachmentDownload bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<ForumArticleAttachmentDownload> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ForumArticleAttachmentDownload> listBean);
	/**
     * @Description(描述):根据FileIdAndUserId查询
	 */
	ForumArticleAttachmentDownload getByFileIdAndUserId(String fileId, String userId);

	/**
     * @Description(描述):根据FileIdAndUserId更新
	 */
	Integer updateByFileIdAndUserId(ForumArticleAttachmentDownload bean, String fileId, String userId);

	/**
     * @Description(描述):根据FileIdAndUserId删除
	 */
	Integer deleteByFileIdAndUserId(String fileId, String userId);



}