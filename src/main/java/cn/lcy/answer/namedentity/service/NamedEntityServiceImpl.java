package cn.lcy.answer.namedentity.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lcy.answer.ontology.query.dao.QueryDAOI;
import cn.lcy.answer.sem.model.PolysemantNamedEntity;
import cn.lcy.answer.util.StringHandle;

@Service("namedEntityService")
public class NamedEntityServiceImpl implements NamedEntityServiceI {

	private QueryDAOI queryDAO;

	@Override
	public List<PolysemantNamedEntity> fillNamedEntities(List<PolysemantNamedEntity> polysemantNamedEntities) {
		for(PolysemantNamedEntity polysemantNameEntity : polysemantNamedEntities) {
			// 先查询其等价实体 避免搜索星爷时 无法正确返回其属性
			String sameEntityUUID = queryDAO.querySameIndividual(polysemantNameEntity.getUUID());
			// 得到等价实体名
			String entityUUID = sameEntityUUID == null ? polysemantNameEntity.getUUID() : sameEntityUUID;
			// 查询该实体的所有属性
			StmtIterator propertyStatements = queryDAO.queryIndividualProperties(entityUUID);
			
			Map<String, String> dataProperties = new LinkedHashMap<String, String>();
			Map<String, String> objectProperties =  new LinkedHashMap<String, String>();
			if(propertyStatements != null) {
				while(propertyStatements.hasNext()) {
					Statement propertyStatement = propertyStatements.next();
					String predicateNodeName = propertyStatement.getPredicate().getLocalName();	// 属性名
					RDFNode objectNode = propertyStatement.getObject();	// 属性值
					if(predicateNodeName.equals("有picSrc")) {
						polysemantNameEntity.setPicSrc(objectNode.toString());	// 设置图片Src
					}
					if(predicateNodeName.equals("有描述")) {
						polysemantNameEntity.setLemmaSummary(objectNode.toString());	// 设置描述
					}
					if(objectNode.isLiteral()) { // 数据属性
						dataProperties.put(predicateNodeName, objectNode.toString());
					} else {
						String objectNodeValue = objectNode.toString().split("#")[1];
						String objectName = null;
						if(!StringHandle.isIncludeChinese(objectNodeValue) && objectNodeValue.length() == 32) // 该属性值不是中文且字符长度为32  则表示UUID
							objectName = queryDAO.queryIndividualComment(objectNodeValue);
						else 
							objectName = objectNodeValue;
						objectProperties.put(predicateNodeName, objectName);
					}
				}
			}
			polysemantNameEntity.setDataProperties(dataProperties);
			polysemantNameEntity.setObjectProperties(objectProperties);
		}
		return polysemantNamedEntities;
	}

	public QueryDAOI getQueryDAO() {
		return queryDAO;
	}

	@Autowired
	public void setQueryDAO(QueryDAOI queryDAO) {
		this.queryDAO = queryDAO;
	}
	
}