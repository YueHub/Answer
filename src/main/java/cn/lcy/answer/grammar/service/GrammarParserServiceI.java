package cn.lcy.answer.grammar.service;

import java.util.List;

import cn.lcy.answer.vo.DependencyVO;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;

public interface GrammarParserServiceI {

	public CoNLLSentence dependencyParser(List<Term> terms);
	
	public DependencyVO getDependencyGraphVO(CoNLLSentence coNLLsentence);
	
}
