package cn.lcy.answer.dictionary.dao;

import java.util.List;

import cn.lcy.answer.ontology.base.dao.BaseDAOI;

public interface DictionaryDAOI extends BaseDAOI {

	/**
	 * 查询等价实体 查询周星驰的所有等价实体们
	 * @param individualName
	 * @return
	 */
	public List<String> querySameIndividuals(String individualName);

}
