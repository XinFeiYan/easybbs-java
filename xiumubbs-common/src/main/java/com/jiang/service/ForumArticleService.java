package com.jiang.service;


import com.jiang.entity.po.ForumArticle;
import com.jiang.entity.po.ForumArticleAttachment;
import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.vo.ForumArticleVO;
import com.jiang.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.time.LocalDateTime;

/**
 * @Description(描述):ForumArticleService
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public interface ForumArticleService {

	/**
     * @Description(描述):根据条件查询列表
	 */
	List<ForumArticle> findListByParam(ForumArticleQuery query);


	/**
     * @Description(描述):根据条件查询数量
	 */
	Integer findCountByParam(ForumArticleQuery query);

	/**
     * @Description(描述):分页查询
	 */
	PaginationResultVO<ForumArticle> findListByPage(ForumArticleQuery query);

	/**
     * @Description(描述):新增
	 */
	Integer add(ForumArticle bean);
	/**
     * @Description(描述):批量新增
	 */
	Integer addBatch(List<ForumArticle> listBean);
	/**
     * @Description(描述):批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ForumArticle> listBean);
	/**
     * @Description(描述):根据ArticleId查询
	 */
	ForumArticle getByArticleId(String articleId);

	/**
     * @Description(描述):根据ArticleId更新
	 */
	Integer updateByArticleId(ForumArticle bean, String articleId);

	/**
     * @Description(描述):根据ArticleId删除
	 */
	Integer deleteByArticleId(String articleId);

	/**
	 * @Description(描述):阅读文章，先增加后查询
	 */
	ForumArticle readArticle(String articleId);

	//文章发表
	void postArticle(Boolean isAdmin, ForumArticle forumArticle, ForumArticleAttachment articleAttachment, MultipartFile cover,MultipartFile attachment);

	//更像文章
	void updateArticle(Boolean isAdmin, ForumArticle forumArticle, ForumArticleAttachment articleAttachment, MultipartFile cover,MultipartFile attachment);

	//管理员删除文章
	void delArticle(String articleIds);
	void delArticleSignle(String articleId);

	//重置文章板块
	void updateBoard(String articleId,Integer pBoardId,Integer boardId);

	//文章审核
	void auditArticle(String articleIds);
	void auditArticleSignle(String articleId);

	 void updateArticleLikeCount(ForumArticleVO forumArticleVO);
}