package cn.lcy.answer.vo;

public class SemanticGraphStatementVO implements java.io.Serializable {

	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主语
	 */
	private SemanticGraphNodeVO subject;
	
	/**
	 * 谓语
	 */
	private SemanticGraphNodeVO predicate;
	
	/**
	 * 宾语
	 */
	private SemanticGraphNodeVO object;

	public SemanticGraphNodeVO getSubject() {
		return subject;
	}

	public void setSubject(SemanticGraphNodeVO subject) {
		this.subject = subject;
	}

	public SemanticGraphNodeVO getPredicate() {
		return predicate;
	}

	public void setPredicate(SemanticGraphNodeVO predicate) {
		this.predicate = predicate;
	}

	public SemanticGraphNodeVO getObject() {
		return object;
	}

	public void setObject(SemanticGraphNodeVO object) {
		this.object = object;
	}
}