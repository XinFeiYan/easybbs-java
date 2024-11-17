package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):文件信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface ForumArticleAttachmentDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据FileId查询
	 */
	T selectByFileId(@Param("fileId") String fileId);

	/**
     * @Description(描述):根据FileId更新
	 */
	Integer updateByFileId(@Param("bean") T t, @Param("fileId") String fileId);

	/**
     * @Description(描述):根据FileId删除
	 */
	Integer deleteByFileId(@Param("fileId") String fileId);
	
	void updateDownloadCount(@Param("fileId") String fileId);


}