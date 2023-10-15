package com.jiang.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description(描述):用户附件下载
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Repository
public interface ForumArticleAttachmentDownloadDao<T,P> extends BaseMapper {
	/**
     * @Description(描述):根据FileIdAndUserId查询
	 */
	T selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);

	/**
     * @Description(描述):根据FileIdAndUserId更新
	 */
	Integer updateByFileIdAndUserId(@Param("bean") T t, @Param("fileId") String fileId, @Param("userId") String userId);

	/**
     * @Description(描述):根据FileIdAndUserId删除
	 */
	Integer deleteByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);


}