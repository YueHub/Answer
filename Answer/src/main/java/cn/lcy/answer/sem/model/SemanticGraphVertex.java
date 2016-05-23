package cn.lcy.answer.sem.model;

public class SemanticGraphVertex implements java.io.Serializable {
	
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private Word word;

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

}