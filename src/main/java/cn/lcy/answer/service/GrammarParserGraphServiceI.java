package cn.lcy.answer.service;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;

import cn.lcy.answer.vo.DependencyVO;

public interface GrammarParserGraphServiceI {
	
	public DependencyVO getDependencyGraphVO(CoNLLSentence coNLLsentence);
	
}
