package cn.lcy.answer.vo;

import java.util.ArrayList;
import java.util.List;

public class SemanticGraphVO implements java.io.Serializable {

	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SemanticGraphStatementVO> semanticGraphStatements = new ArrayList<SemanticGraphStatementVO>();

	public List<SemanticGraphStatementVO> getSemanticGraphStatements() {
		return semanticGraphStatements;
	}

	public void setSemanticGraphStatements(
			List<SemanticGraphStatementVO> semanticGraphStatements) {
		this.semanticGraphStatements = semanticGraphStatements;
	}
}