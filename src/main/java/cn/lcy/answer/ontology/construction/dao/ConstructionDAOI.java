package cn.lcy.answer.ontology.construction.dao;

import java.util.List;

import org.apache.jena.ontology.Individual;


public interface ConstructionDAOI extends ConstructionBaseDAOI {
	
	/**
	 * 添加单个地区类对象属性
	 * @param individualStart
	 * @param relationName
	 * @param individualEnd
	 * @return
	 */
	public boolean addAreaObjectProperty(Individual individualStart, String relationName, Individual individualEnd);
	
	/**
	 * 添加一组地区类对象属性
	 * @param individualStart
	 * @param relationName
	 * @param individualEnd
	 * @return
	 */
	public boolean addAreaObjectProperties(Individual individualStart, List<String> relationName, List<Individual> individualEnd);
	
	/**
	 * 添加单个院校类对象属性
	 * @param individualStart
	 * @param relationName
	 * @param individualEnd
	 * @return
	 */
	public boolean addAcademyProperty(Individual individualStart, String relationName, Individual individualEnd);
	
	/**
	 * 添加一组院校类对象属性
	 * @param individualStart
	 * @param relationName
	 * @param individualEnd
	 * @return
	 */
	public boolean addAcademyProperties(Individual individualStart, List<String> relationName, List<Individual> individualEnd);
	
	/**
	 * 添加单个公司类对象属性
	 * @param individualStart
	 * @param relationName
	 * @param individualEnd
	 * @return
	 */
	public boolean addCompanyProperty(Individual individualStart, String relationName, Individual individualEnd);
	
	/**
	 * 添加一组公司类对象属性
	 * @param individualStart
	 * @param relationName
	 * @param individualEnd
	 * @return
	 */
	public boolean addCompanyProperties(Individual individualStart, List<String> relationName, List<Individual> individualEnd);
}
