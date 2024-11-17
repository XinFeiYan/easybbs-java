package com.jiang.service.Impl;


import com.jiang.Enums.*;
import com.jiang.Exception.BusinessException;
import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.po.*;
import com.jiang.entity.query.ForumArticleAttachmentDownloadQuery;
import com.jiang.entity.query.ForumArticleAttachmentQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.entity.query.UserMessageQuery;
import com.jiang.mapper.ForumArticleAttachmentDownloadDao;
import com.jiang.mapper.ForumArticleDao;
import com.jiang.mapper.UserMessageDao;
import com.jiang.service.ForumArticleAttachmentService;
import com.jiang.mapper.ForumArticleAttachmentDao;
import com.jiang.entity.vo.PaginationResultVO;

import com.jiang.service.ForumArticleService;
import com.jiang.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
	@Resource
	private ForumArticleAttachmentDownloadDao<ForumArticleAttachmentDownload, ForumArticleAttachmentDownloadQuery> forumArticleAttachmentDownloadDao;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private ForumArticleService forumArticleService;
	@Resource
	private UserMessageDao<UserMessage, UserMessageQuery> userMessageDao;
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ForumArticleAttachment attachmentDownLoad(String fileId, SessionWebUserDto sessionWebUserDto){
		//查询附件
		ForumArticleAttachment forumArticleAttachment = this.forumArticleAttachmentDao.selectByFileId(fileId);
		if(forumArticleAttachment==null){
			throw new BusinessException("附件不存在");
		}

		//判断是否下载过,附件积分必须大于0，且不能是作者
		ForumArticleAttachmentDownload download = null;
		if(forumArticleAttachment.getIntegral()>0&& !sessionWebUserDto.getUserId().equals(forumArticleAttachment.getUserId())){
			download = this.forumArticleAttachmentDownloadDao.selectByFileIdAndUserId(fileId,sessionWebUserDto.getUserId());
			if(download==null){
				UserInfo userInfo = userInfoService.getByUserId(sessionWebUserDto.getUserId());
				//为空，判读积分够不够
				if(userInfo.getCurrentIntegral()-forumArticleAttachment.getIntegral()<0){
					throw new BusinessException("积分不够");
				}
			}
		}

		//添加下载消息
		ForumArticleAttachmentDownload updateDownload = new ForumArticleAttachmentDownload();
		updateDownload.setArticleId(forumArticleAttachment.getArticleId());
		updateDownload.setFileId(fileId);
		updateDownload.setUserId(sessionWebUserDto.getUserId());
		updateDownload.setDownloadCount(1);
		//第二次下载，count加1,sql会自动加1
		this.forumArticleAttachmentDownloadDao.insertOrUpdate(updateDownload);
		//文章下载次数加1
		this.forumArticleAttachmentDao.updateDownloadCount(fileId);

		//判断，不能扣积分情况
		if(sessionWebUserDto.getUserId().equals(forumArticleAttachment.getUserId())||download!=null){
			return forumArticleAttachment;
		}

		//扣除下载人积分
		userInfoService.updateUserIntegral(sessionWebUserDto.getUserId(), UserIntegralOperTypeEnum.USER_DOWNLOAD_ATTACHMENT,
				UserIntegralChangeTypeEnum.REDUCE.getChangeType(),forumArticleAttachment.getIntegral());

		//附件提供者增加积分
		userInfoService.updateUserIntegral(forumArticleAttachment.getUserId(),UserIntegralOperTypeEnum.DOWNLOAD_ATTACHMENT,
				UserIntegralChangeTypeEnum.ADD.getChangeType(),forumArticleAttachment.getIntegral());



		//记录消息,给被下载人发消息
		ForumArticle forumArticle = forumArticleService.getByArticleId(forumArticleAttachment.getArticleId());

		UserMessage userMessage=userMessageDao.selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(forumArticle.getArticleId(),0,sessionWebUserDto.getUserId(),MessageTypeEnum.DOWNLOAD_ATTACHMENT.getType());
		//如果有消息，就不用再添加消息
		if(userMessage!=null){
			return forumArticleAttachment;
		}

		userMessage = new UserMessage();
		userMessage.setCreateTime(LocalDateTime.now());//时间
		userMessage.setMessageType(MessageTypeEnum.DOWNLOAD_ATTACHMENT.getType());//附件下载
		userMessage.setArticleId(forumArticle.getArticleId());
		userMessage.setArticleTitle(forumArticle.getTitle());
		userMessage.setReceivedUserId(forumArticle.getUserId());
		userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
		userMessage.setCommentId(0);//跟评论消息混合，0就表示不是评论消息
		userMessage.setSendUserId(sessionWebUserDto.getUserId());
		userMessage.setSendNickName(sessionWebUserDto.getNickname());

		userMessageDao.insert(userMessage);

		return forumArticleAttachment;
	}

}