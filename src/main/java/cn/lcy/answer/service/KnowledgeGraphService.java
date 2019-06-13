package cn.lcy.answer.service;

import java.util.List;

import cn.lcy.answer.vo.KnowledgeGraphVO;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public interface KnowledgeGraphService {

	/**
	 * 获取命名实体的知识图谱
	 * @param polysemantEntities
	 * @return
	 */
	List<KnowledgeGraphVO> getKnowledgeGraphVO(List<PolysemantNamedEntity> polysemantEntities);
	
	
}
