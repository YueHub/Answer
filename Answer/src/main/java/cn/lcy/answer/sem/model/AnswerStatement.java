package cn.lcy.answer.sem.model;


public class AnswerStatement implements Comparable<Object> , java.io.Serializable{
	
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private String statementStr;
	
	private Word subject;
	
	private Word predicate;
	
	private Word object;
	
	public String getStatementStr() {
		return statementStr;
	}

	public void setStatementStr(String statementStr) {
		this.statementStr = statementStr;
	}
	
	public Word getSubject() {
		return subject;
	}

	public void setSubject(Word subject) {
		this.subject = subject;
	}

	public Word getPredicate() {
		return predicate;
	}

	public void setPredicate(Word predicate) {
		this.predicate = predicate;
	}

	public Word getObject() {
		return object;
	}

	public void setObject(Word object) {
		this.object = object;
	}

	@Override
	public int compareTo(Object o) {
		AnswerStatement statement = (AnswerStatement)o;
		int id = this.getSubject().getPosition();
		int otherID = statement.getSubject().getPosition();
		return new Integer(id).compareTo(otherID);
	}
	
}
