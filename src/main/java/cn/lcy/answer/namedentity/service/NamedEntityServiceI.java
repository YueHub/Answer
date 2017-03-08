package cn.lcy.answer.namedentity.service;

import java.util.List;

import cn.lcy.answer.sem.model.PolysemantNamedEntity;

public interface NamedEntityServiceI {
	
	/**
	 * 填充命名实体的相关属性
	 * @param namedEntity
	 * @return
	 */
	public List<PolysemantNamedEntity> fillNamedEntities(List<PolysemantNamedEntity> namedEntity);
	
	
}
