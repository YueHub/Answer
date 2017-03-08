package cn.lcy.answer.sem.model;

import java.util.List;

import com.hankcs.hanlp.seg.common.Term;

public class WordSegmentResult implements java.io.Serializable {

	
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private List<Term> terms;
	
	private List<PolysemantNamedEntity> polysemantEntities;
	
	private List<Word> words;
	
	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	public List<PolysemantNamedEntity> getPolysemantEntities() {
		return polysemantEntities;
	}

	public void setPolysemantEntities(List<PolysemantNamedEntity> polysemantEntities) {
		this.polysemantEntities = polysemantEntities;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}
	
}
