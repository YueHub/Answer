package cn.lcy.answer.sem.model;

import java.util.List;

public class QueryResult implements java.io.Serializable {

	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	private List<Answer> answers;

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
}
