package com.jiang.entity.query;



/**
 * @Description(描述):系统设置信息
 * @author:这玩意真没必要
 * @date(日期):2023/10/14
 */
public class SysSettingQuery extends BaseQuery {
	/**
     * @Description(描述):编号
	 */
	private String code;

	private String codeFuzzy;

	/**
     * @Description(描述):设置信息
	 */
	private String jsonContent;

	private String jsonContentFuzzy;

	public void setCodeFuzzy(String codeFuzzy) {
		this.codeFuzzy = codeFuzzy;
	}

	public String getCodeFuzzy() {
		return this.codeFuzzy;
	}

	public void setJsonContentFuzzy(String jsonContentFuzzy) {
		this.jsonContentFuzzy = jsonContentFuzzy;
	}

	public String getJsonContentFuzzy() {
		return this.jsonContentFuzzy;
	}

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


}