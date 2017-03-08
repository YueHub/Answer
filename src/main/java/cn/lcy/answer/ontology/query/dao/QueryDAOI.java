package cn.lcy.answer.ontology.query.dao;

import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;

import cn.lcy.answer.ontology.base.dao.BaseDAOI;
import cn.lcy.answer.sem.model.QueryResult;

public interface QueryDAOI extends BaseDAOI {

	/**
	 * 查询实体是否存在
	 * @param individualName
	 * @return
	 */
	public boolean individualExist(String individualName);
	
	/**
	 * 查询等价实体 查询星爷的等价实体
	 * @param individualName
	 * @return
	 */
	public String querySameIndividual(String individualName);
	
	/**
	 * 查询等价实体 查询周星驰的所有等价实体们
	 * @param individualName
	 * @return
	 */
	public List<String> querySameIndividuals(String individualName);
	
	/**
	 * 根据subject名称获取所有以Subject为主语的本体断言
	 * @return
	 */
	public List<Statement> getStatementsBySubject(String subject);
	
	/**
	 * 根据object名称获取所有以Object为宾语的本体断言
	 * @return
	 */
	public List<Statement> getStatementsByObject(String object);
	
	/**
	 * 查询答案
	 * @param sparql
	 * @return
	 */
	public QueryResult queryOntology(String sparql);
	
	/**
	 * 查询某一个实体的所有属性
	 * @param individualName
	 */
	public StmtIterator queryIndividualProperties(String individualName);
	
	/**
	 * 查询某一个实体的主要属性（TODO 只是简单的进行了字符限定）
	 * @param individualName
	 * @return
	 */
	public List<Statement> queryIndividualMainProperties(String individualName);
	
	/**
	 * 查询某一个实体的注释
	 * @param individualName
	 * @return
	 */
	public String queryIndividualComment(String individualName);
	
	/**
	 * 获取所有的实体
	 * @return
	 */
	public ExtendedIterator<Individual> getAllIndividuals();

}
