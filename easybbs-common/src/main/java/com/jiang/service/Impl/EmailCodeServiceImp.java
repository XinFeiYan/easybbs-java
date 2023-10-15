package com.jiang.service.Impl;


import com.jiang.entity.query.EmailCodeQuery;
import com.jiang.entity.query.SimplePage;
import com.jiang.Enums.PageSizeEnum;
import com.jiang.service.EmailCodeService;
import com.jiang.mapper.EmailCodeDao;
import com.jiang.entity.po.EmailCode;
import com.jiang.entity.vo.PaginationResultVO;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description(描述):EmailCodeServiceImp
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
@Service("emailCodeService")
public class EmailCodeServiceImp implements EmailCodeService {


	@Resource
	private EmailCodeDao<EmailCode,EmailCodeQuery> emailCodeDao;
	/**
     * @Description(描述):根据条件查询列表
	 */
	@Override
	public List<EmailCode> findListByParam(EmailCodeQuery query){
		return this.emailCodeDao.selectList(query);
	}


	/**
     * @Description(描述):根据条件查询数量
	 */
	@Override
	public Integer findCountByParam(EmailCodeQuery query){
		return this.emailCodeDao.selectCount(query);
	}

	/**
     * @Description(描述):分页查询
	 */
	@Override
	public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query){

		Integer count = this.findCountByParam(query) ;
		Integer pageSize = query.getPageSize()==null?PageSizeEnum.SIZE15.getSize():query.getPageSize();
		SimplePage page=new SimplePage(query.getPageNo(),count,pageSize);
		List<EmailCode> list=this.findListByParam(query);
		query.setSimplePage(page);
		PaginationResultVO<EmailCode> result=new PaginationResultVO<>(count,page.getPageSize(), page.getPageNo(),page.getPageTotal(),list);
		return result;
	}

	/**
     * @Description(描述):新增
	 */
	@Override
	public Integer add(EmailCode bean){
		return this.emailCodeDao.insert(bean);
	}
	/**
     * @Description(描述):批量新增
	 */
	@Override
	public Integer addBatch(List<EmailCode> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.emailCodeDao.insertBatch(listBean);
	}

	/**
     * @Description(描述):批量新增或修改
	 */

	public Integer addOrUpdateBatch(List<EmailCode> listBean){
		if (listBean ==null||listBean.isEmpty()){
			return 0;
		}
		return this.emailCodeDao.insertOrUpdateBatch(listBean);
	}

	/**
     * @Description(描述):根据EmailAndCode查询
	 */
	@Override
	public EmailCode getByEmailAndCode(String email, String code){
		return this.emailCodeDao.selectByEmailAndCode(email,code);
	}

	/**
     * @Description(描述):根据EmailAndCode更新
	 */
	@Override
	public Integer updateByEmailAndCode(EmailCode bean, String email, String code){
		return this.emailCodeDao.updateByEmailAndCode(bean,email,code);
	}


	/**
     * @Description(描述):根据EmailAndCode删除
	 */
	@Override
	public Integer deleteByEmailAndCode(String email, String code){
		return this.emailCodeDao.deleteByEmailAndCode(email,code);
	}


}