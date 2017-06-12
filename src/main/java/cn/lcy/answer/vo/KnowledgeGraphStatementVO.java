package cn.lcy.answer.vo;

public class KnowledgeGraphStatementVO implements java.io.Serializable {

	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主语
	 */
	private KnowledgeGraphNodeVO subject;
	
	/**
	 * 谓语
	 */
	private KnowledgeGraphNodeVO predicate;
	
	/**
	 * 宾语
	 */
	private KnowledgeGraphNodeVO object;

	public KnowledgeGraphNodeVO getSubject() {
		return subject;
	}

	public void setSubject(KnowledgeGraphNodeVO subject) {
		this.subject = subject;
	}

	public KnowledgeGraphNodeVO getPredicate() {
		return predicate;
	}

	public void setPredicate(KnowledgeGraphNodeVO predicate) {
		this.predicate = predicate;
	}

	public KnowledgeGraphNodeVO getObject() {
		return object;
	}

	public void setObject(KnowledgeGraphNodeVO object) {
		this.object = object;
	}
}