package com.jiang.service;


import com.jiang.entity.po.ForumComment;
import com.jiang.entity.query.ForumCommentQuery;
import com.jiang.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.time.LocalDateTime;

/**
 * @Description(描述):ForumCommentService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface ForumCommentService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<ForumComment> findListByParam(ForumCommentQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(ForumCommentQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<ForumComment> findListByPage(ForumCommentQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(ForumComment bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<ForumComment> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ForumComment> listBean);
	/**
     * @Description(描述):根据CommentId查询
	 */
	ForumComment getByCommentId(Integer commentId);

	/**
     * @Description(描述):根据CommentId更新
	 */
	Integer updateByCommentId(ForumComment bean, Integer commentId);

	/**
     * @Description(描述):根据CommentId删除
	 */
	Integer deleteByCommentId(Integer commentId);

	void changeTopType(String userId,Integer commentId,Integer topType);

	void postComment(ForumComment comment, MultipartFile file);

	void delComment(String commentIds);
	void delCommentSingle(Integer commentId);

	void auditComment(String commentIds);
	void auditCommentSingle(Integer commentId);

}