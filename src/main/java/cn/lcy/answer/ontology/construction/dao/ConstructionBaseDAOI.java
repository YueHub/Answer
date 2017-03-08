package cn.lcy.answer.ontology.construction.dao;

import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Resource;

import cn.lcy.answer.ontology.base.dao.BaseDAOI;

public interface ConstructionBaseDAOI extends BaseDAOI {
	
	/**
	 * 根据名称创建一个类
	 * @param className
	 * @return
	 */
	public OntClass createOntClass(String className);
	
	/**
	 * 根据名称获取一个类、概念
	 * @param ontClassName
	 * @return
	 */
	public OntClass getOntClass(String ontClassName);
	
	/**
	 * 添加一个等价类
	 * @param individual
	 * @param aliasIndividual
	 */
	public void addSameAs(Individual individual, Individual aliasIndividual);
	
	/**
	 * 创建一个子类
	 * @param subClass
	 * @param ontClass
	 */
	public void addSubClass(OntClass ontClass, OntClass subClass);
	
	/**
	 * 将实体分配到某个二级类中
	 * @param individual
	 * @param ontClass
	 */
	public void addSubClass(Individual individual, OntClass ontClass);
	
	// 创建实体
	/**
	 * 根据UUID和所属类 创建一个实体
	 * @param individualId
	 * @param genusClass
	 * @return
	 */
	public Individual createIndividual(String individualId, OntClass genusClass);
	
	// 4.添加实体数据属性

	// 获取实体
	
	/**
	 * 根据实体ID获取实体
	 * @param individualId
	 * @return
	 */
	public Individual getIndividual(String individualId);
	
	public boolean addComment(Individual individual, String comment);
	
	/**
	 * 给实体添加单个数据属性
	 * @param ontologyClass
	 * @param individualId
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public boolean addDataProperty(Individual individual, String propertyName, String propertyValue);
	
	/**
	 * 给实体添加一组数据属性
	 * @param ontologyClass
	 * @param individualId
	 * @param propertyNames
	 * @param propertyValues
	 * @return
	 */
	public boolean addDataProperties(Individual individual, List<String> propertyNames, List<String> propertyValues);
	
	// 5.添加实体对象属性
	
	/**
	 * 添加单个对象属性
	 * @param individualBegin
	 * @param objectPropertyName
	 * @param individualEnd
	 * @return
	 */
	public Resource addObjectProperty(Individual individualStart, String objectPropertyName, Individual individualEnd);
	
	/**
	 * 添加多个对象属性
	 * @param individualBegin
	 * @param objectPropertyName
	 * @param individualEnd
	 * @return
	 */
	public List<Resource> addObjectProperties(Individual individualStart, List<String> objectPropertyNames, List<Individual> individualEnds);
	// 6.添加类
	
	
	
}
