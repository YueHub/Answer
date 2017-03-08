package cn.lcy.answer.sem.model;

import java.util.Map;

public class PolysemantNamedEntity implements java.io.Serializable {

	/**
	 * default serial version ID 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 实体UUID
	 */
	private String UUID;
	
	/**
	 * 实体名称
	 */
	private String entityName; 
	
	/**
	 * 歧义说明
	 */
	private String polysemantExplain;
	
	/**
	 * 实体来源
	 */
	private String entityUrl;
	
	/**
	 * 是否是本名
	 */
	private String isAliases;
	
	/**
	 * 实体所属类
	 */
	private String ontClass;
	
	/**
	 * 实体对应单词在句子中的位置
	 */
	private int position;
	
	/**
	 * 实体图片URL
	 */
	private String picSrc;

	/**
	 * 实体描述
	 */
	private String lemmaSummary;
	
	/**
	 * 实体数据属性
	 */
	private Map<String, String> dataProperties;
	
	/**
	 * 实体对象属性
	 */
	private Map<String, String> objectProperties;
	
	
	/**
	 * 是否激活
	 */
	private boolean active;
	
	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getPolysemantExplain() {
		return polysemantExplain;
	}

	public void setPolysemantExplain(String polysemantExplain) {
		this.polysemantExplain = polysemantExplain;
	}

	public String getEntityUrl() {
		return entityUrl;
	}

	public void setEntityUrl(String entityUrl) {
		this.entityUrl = entityUrl;
	}

	public String getIsAliases() {
		return isAliases;
	}

	public void setIsAliases(String isAliases) {
		this.isAliases = isAliases;
	}

	public String getOntClass() {
		return ontClass;
	}

	public void setOntClass(String ontClass) {
		this.ontClass = ontClass;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public String getPicSrc() {
		return picSrc;
	}

	public void setPicSrc(String picSrc) {
		this.picSrc = picSrc;
	}

	public String getLemmaSummary() {
		return lemmaSummary;
	}

	public void setLemmaSummary(String lemmaSummary) {
		this.lemmaSummary = lemmaSummary;
	}

	public Map<String, String> getDataProperties() {
		return dataProperties;
	}

	public void setDataProperties(Map<String, String> dataProperties) {
		this.dataProperties = dataProperties;
	}
	
	public Map<String, String> getObjectProperties() {
		return objectProperties;
	}

	public void setObjectProperties(Map<String, String> objectProperties) {
		this.objectProperties = objectProperties;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
