package cn.lcy.answer.service;

import java.util.List;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dependency.IDependencyParser;
import com.hankcs.hanlp.dependency.nnparser.NeuralNetworkDependencyParser;
import com.hankcs.hanlp.seg.common.Term;

import cn.lcy.answer.vo.Arg;
import cn.lcy.answer.vo.DependencyNode;
import cn.lcy.answer.vo.DependencyVO;

public class GrammarParserGraphServiceImpl implements GrammarParserGraphServiceI {
	
	private volatile static GrammarParserGraphServiceI singleInstance;
	
	/**
	 * 私有化构造方法，实现单例
	 */
	private GrammarParserGraphServiceImpl() {}
	
	/**
	 * 获取单例
	 * @return
	 */
	public static GrammarParserGraphServiceI getInstance() {
		if (singleInstance == null) {
			synchronized (GrammarParserGraphServiceImpl.class) {
				if (singleInstance == null) {
					singleInstance = new GrammarParserGraphServiceImpl();
				}
			}
		}
		return singleInstance;
	}
	
	/**
	 * 依存句法分析
	 */
	public CoNLLSentence dependencyParser(List<Term> terms) {
	    // 基于神经网络的高性能依存句法分析器
	    IDependencyParser parser = new NeuralNetworkDependencyParser().enableDeprelTranslator(false);
    	CoNLLSentence coNLLsentence = parser.parse(terms);
    	return coNLLsentence;
	}
	
	/**
	 * 获取依存语法图
	 */
	@Override
	public DependencyVO getDependencyGraphVO(CoNLLSentence coNLLsentence) {
		DependencyVO dependencyVO = new DependencyVO();
		if (coNLLsentence != null) {
			for (CoNLLWord dependency : coNLLsentence) {
				DependencyNode dependencyNode = new DependencyNode();
				Arg arg = new Arg();
				arg.setLength(0);
				dependencyNode.setId(dependency.ID - 1);
				dependencyNode.setCont(dependency.LEMMA);
				dependencyNode.setPos(dependency.POSTAG);
				dependencyNode.setNe("0");
				dependencyNode.setParent(dependency.HEAD.ID - 1);
				dependencyNode.setRelate(dependency.DEPREL);
				dependencyNode.setArg(arg);
				dependencyVO.getDependencyNodes().add(dependencyNode);
			}
		}
		return dependencyVO;
	}
}