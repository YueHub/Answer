package cn.lcy.answer.knowledgegraph.service;

import java.util.List;

import cn.lcy.answer.sem.model.PolysemantNamedEntity;
import cn.lcy.answer.vo.KnowledgeGraphVO;

public interface KnowledgeGraphServiceI {
	
	/**
	 * 获取命名实体的知识图谱
	 * @param namedEntity
	 * @return
	 */
	public List<KnowledgeGraphVO> getKnowledgeGraphVO(List<PolysemantNamedEntity> polysemantEntities);
	
	
}
