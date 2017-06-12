package cn.lcy.answer.service;

import java.util.List;

import cn.lcy.answer.vo.SemanticGraphVO;
import cn.lcy.knowledge.analysis.sem.model.AnswerStatement;

public interface SemanticGraphViewServiceI {
	
	/**
	 * 获取语义图 便于前端显示
	 * @param queryStatements
	 * @return
	 */
	public SemanticGraphVO getSemanticGraphVO(List<AnswerStatement> queryStatements);
	
	
}
