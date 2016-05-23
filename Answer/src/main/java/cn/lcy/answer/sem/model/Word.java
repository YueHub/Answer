package cn.lcy.answer.sem.model;

import java.util.List;

public class Word implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 单词所在位置
	 */
	private int position;
	
	/**
	 * 单词名称
	 */
	private String name;
	
	/**
	 * 单词词性（粗粒度）
	 */
	private String cpostag;
	
	/**
	 * 单词词性（细粒度）
	 */
	private String postag;
	
	/**
	 * 该单词对应的同名实体
	 */
	private List<PolysemantNamedEntity> polysemantNamedEntities;
	
	/**
	 * 消岐后的UUID
	 */
	private String disambiguationUUID;
	
	/**
	 * 消岐后名称
	 */
	private String disambiguationName;

	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpostag() {
		return cpostag;
	}

	public void setCpostag(String cpostag) {
		this.cpostag = cpostag;
	}

	public String getPostag() {
		return postag;
	}

	public void setPostag(String postag) {
		this.postag = postag;
	}

	public List<PolysemantNamedEntity> getPolysemantNamedEntities() {
		return polysemantNamedEntities;
	}

	public void setPolysemantNamedEntities(
			List<PolysemantNamedEntity> polysemantNamedEntities) {
		this.polysemantNamedEntities = polysemantNamedEntities;
	}
	
	public String getDisambiguationUUID() {
		return disambiguationUUID;
	}

	public void setDisambiguationUUID(String disambiguationUUID) {
		this.disambiguationUUID = disambiguationUUID;
	}

	public String getDisambiguationName() {
		return disambiguationName;
	}

	public void setDisambiguationName(String disambiguationName) {
		this.disambiguationName = disambiguationName;
	}
	
	/**
	 * 激活同名实体
	 */
	public void active(PolysemantNamedEntity polysemantNamedEntity) {
		for(PolysemantNamedEntity polysemantNamedEntityIter : polysemantNamedEntities) {
			if(polysemantNamedEntityIter.getUUID().equals(polysemantNamedEntity.getUUID())) {
				// 找到该同名实体并激活
				polysemantNamedEntityIter.setActive(true);
			} else {
				// 其他同名实体设置为未激活状态
				polysemantNamedEntityIter.setActive(false);
			}
		}
	}
	
	/**
	 * 获取激活的等价实体
	 */
	public PolysemantNamedEntity getActiveEntity() {
		PolysemantNamedEntity polysemantNamedEntityRet = null;
		for(PolysemantNamedEntity polysemantNamedEntity : polysemantNamedEntities) {
			if(polysemantNamedEntity.isActive()) {
				polysemantNamedEntityRet = polysemantNamedEntity;
			}
		}
		return polysemantNamedEntityRet;
	}
	
}
