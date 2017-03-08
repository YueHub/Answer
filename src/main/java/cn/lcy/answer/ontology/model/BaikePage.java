package cn.lcy.answer.ontology.model;

import java.util.List;

public class BaikePage {
	
	/**
	 * 主题URL
	 */
	private String url;
	
	/**
	 * 词条图片地址
	 */
	private String picSrc;
	
	/**
	 * 词条标题
	 */
	private String lemmaTitle;
	
	/**
	 * 词条描述
	 */
	private String lemmaSummary;
	
	/**
	 * 所有的同名实体
	 */
	private List<String> polysemants;
	
	/**
	 * 歧义解释
	 */
	private String polysemantExplain;
	
	/**
	 * 所有人物关系名 如搭档
	 */
	private List<String> relationNames;
	
	/**
	 * 所有人物关系值 如吴孟达
	 */
	private List<String> relationValues;
	
	/**
	 * 所有人物关系相关人物URL值
	 */
	private List<String> relationUrls;
	
	/**
	 * 所有属性名
	 */
	private List<String> parameterNames;
	
	/**
	 * 所有属性值
	 */
	private List<String> parameterValues;
	
	/**
	 * 所有有链接的属性值
	 */
	private List<String> parameterHasUrlValues;
	
	/**
	 * 所有链接
	 */
	private List<String> parameterHasUrl;
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getPicSrc() {
		return picSrc;
	}

	public void setPicSrc(String picSrc) {
		this.picSrc = picSrc;
	}

	public String getLemmaTitle() {
		return lemmaTitle;
	}

	public void setLemmaTitle(String lemmaTitle) {
		this.lemmaTitle = lemmaTitle;
	}

	public String getLemmaSummary() {
		return lemmaSummary;
	}

	public void setLemmaSummary(String lemmaSummary) {
		this.lemmaSummary = lemmaSummary;
	}
	
	public List<String> getPolysemants() {
		return polysemants;
	}

	public void setPolysemants(List<String> polysemants) {
		this.polysemants = polysemants;
	}

	public String getPolysemantExplain() {
		return polysemantExplain;
	}

	public void setPolysemantExplain(String polysemantExplain) {
		this.polysemantExplain = polysemantExplain;
	}
	
	public List<String> getRelationNames() {
		return relationNames;
	}

	public void setRelationNames(List<String> relationNames) {
		this.relationNames = relationNames;
	}

	public List<String> getRelationValues() {
		return relationValues;
	}

	public void setRelationValues(List<String> relationValues) {
		this.relationValues = relationValues;
	}

	public List<String> getRelationUrls() {
		return relationUrls;
	}

	public void setRelationUrls(List<String> relationUrls) {
		this.relationUrls = relationUrls;
	}

	public List<String> getParameterNames() {
		return parameterNames;
	}
	
	public void setParameterNames(List<String> parameterNames) {
		this.parameterNames = parameterNames;
	}	

	public List<String> getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(List<String> parameterValues) {
		this.parameterValues = parameterValues;
	}
	
	public List<String> getParameterHasUrlValues() {
		return parameterHasUrlValues;
	}

	public void setParameterHasUrlValues(List<String> parameterHasUrlValues) {
		this.parameterHasUrlValues = parameterHasUrlValues;
	}

	public List<String> getParameterHasUrl() {
		return parameterHasUrl;
	}

	public void setParameterHasUrl(List<String> parameterHasUrl) {
		this.parameterHasUrl = parameterHasUrl;
	}

	@Override
	public String toString() {
		String row = null;
		row = "词条URL:" + url + "\n"
			 +"词条图片地址:" + picSrc + "\n"
			 +"词条主题:" + lemmaTitle + "\n"
			 +"词条概括:" + lemmaSummary + "\n"
			 +"同名实体:" + polysemants + "\n"
			 +"实体歧义说明:" + polysemantExplain + "\n"
			 +"人物关系:" + relationNames + "\n"
			 +"关系取值:" + relationValues + "\n"
			 +"属性名:" + parameterNames + "\n"
			 +"属性值:" + parameterValues + "\n"
			 +"链接字段:" + parameterHasUrlValues + "\n"
			 +"超链接:" + parameterHasUrl + "\n";
		return row;
	}
	
}
