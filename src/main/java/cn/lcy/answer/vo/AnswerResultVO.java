package cn.lcy.answer.vo;

import java.util.List;

import cn.lcy.knowledge.analysis.sem.model.Word;

public class AnswerResultVO implements java.io.Serializable {

	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private List<Word> words;
	
	private List<PolysemantSituationVO> polysemantSituationVOs;

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public List<PolysemantSituationVO> getPolysemantSituationVOs() {
		return polysemantSituationVOs;
	}

	public void setPolysemantSituationVOs(
			List<PolysemantSituationVO> polysemantSituationVOs) {
		this.polysemantSituationVOs = polysemantSituationVOs;
	}
	
}
