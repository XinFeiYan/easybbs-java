package com.jiang.entity.po;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description(描述):系统设置信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class SysSetting implements Serializable {
	/**
     * @Description(描述):编号
	 */
	private String code;

	/**
     * @Description(描述):设置信息
	 */
	private String jsonContent;

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setJsonContent(String jsonContent) {
		this.jsonContent = jsonContent;
	}

	public String getJsonContent() {
		return this.jsonContent;
	}

	@Override
	public String toString() {
		return "编号:" + (code == null ? "空" : code) + ",设置信息:" + (jsonContent == null ? "空" : jsonContent) ;
	}
}