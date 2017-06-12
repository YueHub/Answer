package cn.lcy.answer.vo;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeGraphVO implements java.io.Serializable {
	
	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private List<KnowledgeGraphStatementVO> knowledgeGraphStatements = new ArrayList<KnowledgeGraphStatementVO>();

	public List<KnowledgeGraphStatementVO> getKnowledgeGraphStatements() {
		return knowledgeGraphStatements;
	}

	public void setKnowledgeGraphStatements(
			List<KnowledgeGraphStatementVO> knowledgeGraphStatements) {
		this.knowledgeGraphStatements = knowledgeGraphStatements;
	}
}