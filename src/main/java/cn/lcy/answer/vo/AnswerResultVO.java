package cn.lcy.answer.vo;

import java.util.List;

import cn.lcy.knowledge.analysis.sem.model.Word;

public class AnswerResultVO implements java.io.Serializable {

	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 提问时间 (后台接收到提问的时间点)
	 */
	private long askTime;
	
	/**
	 * 回答时间（后台给出回答的时间点）
	 */
	private long answerTime;
	
	/**
	 * 问题
	 */
	private String question;
	
	/**
	 * 分词 / 命名实体识别
	 */
	private List<Word> words;
	
	/**
	 * 简短答案
	 */
	private ShortAnswerVO shortAnswer;
	
	private List<KnowledgeGraphVO> knowledgeGraphVOs;

	public long getAskTime() {
		return askTime;
	}

	public void setAskTime(long askTime) {
		this.askTime = askTime;
	}

	public long getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(long answerTime) {
		this.answerTime = answerTime;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public ShortAnswerVO getShortAnswer() {
		return shortAnswer;
	}

	public void setShortAnswer(ShortAnswerVO shortAnswer) {
		this.shortAnswer = shortAnswer;
	}

	public List<KnowledgeGraphVO> getKnowledgeGraphVOs() {
		return knowledgeGraphVOs;
	}

	public void setKnowledgeGraphVOs(List<KnowledgeGraphVO> knowledgeGraphVOs) {
		this.knowledgeGraphVOs = knowledgeGraphVOs;
	}
}
