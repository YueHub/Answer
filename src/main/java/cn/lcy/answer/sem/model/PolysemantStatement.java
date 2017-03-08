package cn.lcy.answer.sem.model;

import java.util.List;

public class PolysemantStatement implements java.io.Serializable {

	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	private List<AnswerStatement> answerStatements;

	public List<AnswerStatement> getAnswerStatements() {
		return answerStatements;
	}

	public void setAnswerStatements(List<AnswerStatement> answerStatements) {
		this.answerStatements = answerStatements;
	}
}