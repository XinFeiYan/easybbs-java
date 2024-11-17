package com.jiang.service;


import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.ForumArticleAttachment;
import com.jiang.entity.query.ForumArticleAttachmentQuery;
import com.jiang.entity.vo.PaginationResultVO;

import java.util.List;

/**
 * @Description(描述):ForumArticleAttachmentService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface ForumArticleAttachmentService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<ForumArticleAttachment> findListByParam(ForumArticleAttachmentQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(ForumArticleAttachmentQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<ForumArticleAttachment> findListByPage(ForumArticleAttachmentQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(ForumArticleAttachment bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<ForumArticleAttachment> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ForumArticleAttachment> listBean);
	/**
     * @Description(描述):根据FileId查询
	 */
	ForumArticleAttachment getByFileId(String fileId);

	/**
     * @Description(描述):根据FileId更新
	 */
	Integer updateByFileId(ForumArticleAttachment bean, String fileId);

	/**
     * @Description(描述):根据FileId删除
	 */
	Integer deleteByFileId(String fileId);

	/**
	 * 下载附件
	 * @param fileId
	 * @param sessionWebUserDto
	 */
	ForumArticleAttachment attachmentDownLoad(String fileId, SessionWebUserDto sessionWebUserDto);
}