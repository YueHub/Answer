package cn.lcy.answer.sem.model;

import cn.lcy.answer.enums.AnswerTypeEnum;

public class Answer implements java.io.Serializable {

	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 答案类型
	 */
	private AnswerTypeEnum type;
	
	/**
	 * 答案内容
	 */
	private String content;

	public AnswerTypeEnum getType() {
		return type;
	}

	public void setType(AnswerTypeEnum type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
