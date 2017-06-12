package cn.lcy.answer.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;

import cn.lcy.answer.vo.KnowledgeGraphNodeVO;
import cn.lcy.answer.vo.KnowledgeGraphStatementVO;
import cn.lcy.answer.vo.KnowledgeGraphVO;
import cn.lcy.knowledge.analysis.ontology.query.dao.QueryDAOI;
import cn.lcy.knowledge.analysis.ontology.query.dao.QueryDAOImpl;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;
import cn.lcy.knowledge.analysis.util.StringHandle;

public class KnowledgeGraphServiceImpl implements KnowledgeGraphServiceI {

	private volatile static KnowledgeGraphServiceI singleInstance;
	
	private static QueryDAOI queryDAO;
	
	static {
		queryDAO = QueryDAOImpl.getInstance();
	}
	
	/**
	 * 私有化构造方法，实现单例模式
	 */
	private KnowledgeGraphServiceImpl() {}
	
	public static KnowledgeGraphServiceI getInstance() {
		if (singleInstance == null) {
			synchronized (KnowledgeGraphServiceImpl.class) {
				if (singleInstance == null) {
					singleInstance = new KnowledgeGraphServiceImpl();
				}
			}
		}
		return singleInstance;
	}
	
	

	@Override
	public List<KnowledgeGraphVO> getKnowledgeGraphVO(List<PolysemantNamedEntity> polysemantNamedEntities) {
		List<KnowledgeGraphVO> knowledgeGraphVOs = new ArrayList<KnowledgeGraphVO>();
		int subjectID = 0;
		int objectID = 0;
		for (PolysemantNamedEntity polysemantNameEntity : polysemantNamedEntities) {
			KnowledgeGraphVO knowledgeGraphVO = new KnowledgeGraphVO();
			// 先查询其等价实体 避免搜索星爷时 无法正确返回其属性
			String sameEntityUUID = null;
			if (polysemantNameEntity.getIsAliases().equals("0")) { // 如果该实体为实体别名
				sameEntityUUID = queryDAO.querySameIndividual(polysemantNameEntity.getUUID());
			}
			
			// 得到等价实体名
			String entityUUID = sameEntityUUID == null ? polysemantNameEntity.getUUID() : sameEntityUUID;
			
			//StmtIterator propertyStatements = queryDAO.queryIndividualProperties(polysemantNameEntity.getUUID());
			List<Statement> propertyStatements = queryDAO.queryIndividualMainProperties(entityUUID);
			List<PolysemantNamedEntity> subjectPolysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
			int count = 0;	// 控制前端的结点个数 20
			for (Statement propertyStatement : propertyStatements) {
				if (count < 10) {
					KnowledgeGraphStatementVO knowledgeGraphStatementVO = new KnowledgeGraphStatementVO();
					KnowledgeGraphNodeVO subject = new KnowledgeGraphNodeVO();
					KnowledgeGraphNodeVO predicate = new KnowledgeGraphNodeVO();
					KnowledgeGraphNodeVO object = new KnowledgeGraphNodeVO();
					subject.setID(subjectID);
					subject.setName(polysemantNameEntity.getOntClass() + ":" + polysemantNameEntity.getEntityName());
					subject.setShape("dot");
					subject.setColor("red");
					subject.setAlpha(1);
					subjectPolysemantNamedEntities.add(polysemantNameEntity);
					subject.setPolysemantNamedEntities(subjectPolysemantNamedEntities);
					RDFNode objectNode = propertyStatement.getObject();
					String[] name = propertyStatement.getPredicate().getURI().split("#");
					String predicateName = null;
					if (name.length > 1) {
						predicateName = name[1];
					} else {
						predicateName = name[0];
					}
					predicate.setName(predicateName);
					predicate.setColor("green");
					predicate.setSize(15);
					predicate.setAlpha(1);
					
					// 是否为文本（以此标识区分对象属性和数据属性）
					if (!predicateName.equals("type") && !predicateName.equals("有picSrc")) {
						if (objectNode.isLiteral()) {
							object.setID(objectID);
							object.setName(objectNode.toString());
							// 数据属性为矩形
							object.setShape("rect");
							object.setColor("#a7af00");
							// TODO alpha为0表示不可见 1表示可见
							object.setAlpha(1);
						} else {
							object.setID(objectID);
							String[] objectNodeValueArr = objectNode.toString().split("#");
							String objectNodeValue = null;
							if (objectNodeValueArr.length > 1) {
								objectNodeValue = objectNodeValueArr[1];
							} else {
								objectNodeValue = objectNodeValueArr[0];
							}
							String objectName = null;
							if (!StringHandle.isIncludeChinese(objectNodeValue) && objectNodeValue.length() == 32) // 该属性值不是中文且字符长度为32  则表示UUID
								objectName = queryDAO.queryIndividualComment(objectNodeValue);
							else 
								objectName = objectNodeValue;
							object.setName(objectName);
							// 对象属性为圆形
							object.setShape("dot");
							for (PolysemantNamedEntity polysemantEntityForColor : polysemantNamedEntities) {
								if (objectNode.toString().split("#")[1].equals(polysemantEntityForColor.getEntityName())) {
									object.setColor("red");
								}
							}
							if (object.getColor() == null) {
								object.setColor("#b2b19d");
							}
							object.setAlpha(1);
						}
						knowledgeGraphStatementVO.setSubject(subject);
						knowledgeGraphStatementVO.setPredicate(predicate);
						knowledgeGraphStatementVO.setObject(object);
						knowledgeGraphVO.getKnowledgeGraphStatements().add(knowledgeGraphStatementVO);
						++subjectID;
						++objectID;
					}
				}
				++count;
			}
			knowledgeGraphVOs.add(knowledgeGraphVO);
		}
		return knowledgeGraphVOs;
	}
}