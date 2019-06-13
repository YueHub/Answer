package cn.lcy.answer.service;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;

import cn.lcy.answer.vo.DependencyVO;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public interface GrammarParserGraphService {

	/**
	 * 获取依赖图
	 * @param coNLLsentence
	 * @return
	 */
	DependencyVO getDependencyGraphVO(CoNLLSentence coNLLsentence);
	
}
