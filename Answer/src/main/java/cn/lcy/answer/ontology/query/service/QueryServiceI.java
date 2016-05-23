package cn.lcy.answer.ontology.query.service;

import java.util.List;

import cn.lcy.answer.sem.model.AnswerStatement;
import cn.lcy.answer.sem.model.PolysemantStatement;
import cn.lcy.answer.sem.model.QueryResult;
import cn.lcy.answer.sem.model.SemanticGraph;

public interface QueryServiceI {

	/**
	 * 断言集合
	 * @return
	 */
	public List<AnswerStatement> createStatement(SemanticGraph semanticGraph);
	
	/**
	 * 歧义断言集合（一个歧义断言包含断言集合）
	 * @param myStatements
	 * @return
	 */
	public List<PolysemantStatement> createPolysemantStatements(List<AnswerStatement> answerStatements);
	
	/**
	 * 实体消岐
	 * @param statements
	 */
	public List<AnswerStatement> individualsDisambiguation(List<AnswerStatement> myStatements);
	
	/**
	 * 谓语消岐
	 * @param statements
	 */
	public List<AnswerStatement> predicateDisambiguation(List<AnswerStatement> myStatements);
	
	/**
	 * 构造查询断言
	 * @param myStatements
	 * @return
	 */
	public List<AnswerStatement> createQueryStatements(List<AnswerStatement> myStatements);
	
	/**
	 * 根据断言构造查询语句
	 * @param semanticGraph
	 * @return
	 */
	public String createSparql(List<AnswerStatement> myStatements);
	
	/**
	 * 根据断言构造多个查询语句
	 * @param myStatements
	 * @return
	 */
	public List<String> createSparqls(List<AnswerStatement> myStatements);
	
	/**
	 * 执行查询语句 查询本体
	 * @param sparql
	 * @return
	 */
	public QueryResult queryOntology(String SPARQL);
	
	public String queryIndividualComment(String individualName);
}