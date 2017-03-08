package cn.lcy.answer.sem.graph.service;

import java.util.List;

import cn.lcy.answer.sem.model.PolysemantNamedEntity;
import cn.lcy.answer.sem.model.AnswerStatement;
import cn.lcy.answer.sem.model.SemanticGraph;
import cn.lcy.answer.sem.model.Word;
import cn.lcy.answer.vo.SemanticGraphVO;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;

public interface SemanticGraphServiceI {
	
	/**
	 * 创建语义图
	 * @param coNLLsentence
	 * @param polysemantNamedEntities
	 * @return
	 */
	public SemanticGraph buildSemanticGraph(CoNLLSentence coNLLsentence, List<PolysemantNamedEntity> polysemantNamedEntities);
	
	/**
	 * 创建备用语义图
	 * @param polysemantNamedEntities
	 * @return
	 */
	public SemanticGraph buildBackUpSemanticGraph(List<Word> words);
	
	/**
	 * 获取语义图 便于前端显示
	 * @param queryStatements
	 * @return
	 */
	public SemanticGraphVO getSemanticGraphVO(List<AnswerStatement> queryStatements);
	
}
