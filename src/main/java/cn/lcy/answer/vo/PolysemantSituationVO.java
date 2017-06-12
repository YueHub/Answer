package cn.lcy.answer.vo;

import java.util.List;

import cn.lcy.knowledge.analysis.sem.model.AnswerStatement;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;
import cn.lcy.knowledge.analysis.sem.model.PolysemantStatement;
import cn.lcy.knowledge.analysis.sem.model.QueryResult;

public class PolysemantSituationVO implements java.io.Serializable {
	
	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	private PolysemantStatement polysemantStatement;
	
	private List<AnswerStatement> semanticStatements;
	
	private List<PolysemantNamedEntity> activePolysemantNamedEntities;
	
	private List<AnswerStatement> individualsDisambiguationStatements;
	
	private List<AnswerStatement> predicateDisambiguationStatements;
	
	private List<AnswerStatement> queryStatements;
	
	private List<String> SPARQLS;
	
	private List<QueryResult> queryResults;
	
	public PolysemantStatement getPolysemantStatement() {
		return polysemantStatement;
	}

	public void setPolysemantStatement(PolysemantStatement polysemantStatement) {
		this.polysemantStatement = polysemantStatement;
	}

	public List<AnswerStatement> getSemanticStatements() {
		return semanticStatements;
	}

	public void setSemanticStatements(List<AnswerStatement> semanticStatements) {
		this.semanticStatements = semanticStatements;
	}
	
	public List<PolysemantNamedEntity> getActivePolysemantNamedEntities() {
		return activePolysemantNamedEntities;
	}

	public void setActivePolysemantNamedEntities(
			List<PolysemantNamedEntity> activePolysemantNamedEntities) {
		this.activePolysemantNamedEntities = activePolysemantNamedEntities;
	}

	public List<AnswerStatement> getIndividualsDisambiguationStatements() {
		return individualsDisambiguationStatements;
	}

	public void setIndividualsDisambiguationStatements(
			List<AnswerStatement> individualsDisambiguationStatements) {
		this.individualsDisambiguationStatements = individualsDisambiguationStatements;
	}

	public List<AnswerStatement> getPredicateDisambiguationStatements() {
		return predicateDisambiguationStatements;
	}

	public void setPredicateDisambiguationStatements(
			List<AnswerStatement> predicateDisambiguationStatements) {
		this.predicateDisambiguationStatements = predicateDisambiguationStatements;
	}

	public List<AnswerStatement> getQueryStatements() {
		return queryStatements;
	}

	public void setQueryStatements(List<AnswerStatement> queryStatements) {
		this.queryStatements = queryStatements;
	}

	public List<String> getSPARQLS() {
		return SPARQLS;
	}

	public void setSPARQLS(List<String> sPARQLS) {
		SPARQLS = sPARQLS;
	}

	public List<QueryResult> getQueryResults() {
		return queryResults;
	}

	public void setQueryResults(List<QueryResult> queryResults) {
		this.queryResults = queryResults;
	}
}
