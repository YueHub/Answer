package cn.lcy.answer.service;

import java.util.List;

import cn.lcy.answer.vo.KnowledgeGraphVO;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;

public interface KnowledgeGraphServiceI {
	
	/**
	 * 获取命名实体的知识图谱
	 * @param namedEntity
	 * @return
	 */
	public List<KnowledgeGraphVO> getKnowledgeGraphVO(List<PolysemantNamedEntity> polysemantEntities);
	
	
}
