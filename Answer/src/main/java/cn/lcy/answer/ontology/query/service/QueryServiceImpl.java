package cn.lcy.answer.ontology.query.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lcy.answer.enums.NounsTagEnum;
import cn.lcy.answer.ontology.query.dao.QueryDAOI;
import cn.lcy.answer.ontology.query.dao.QueryDAOImpl;
import cn.lcy.answer.sem.model.AnswerStatement;
import cn.lcy.answer.sem.model.PolysemantNamedEntity;
import cn.lcy.answer.sem.model.PolysemantStatement;
import cn.lcy.answer.sem.model.QueryResult;
import cn.lcy.answer.sem.model.SemanticGraph;
import cn.lcy.answer.sem.model.SemanticGraphEdge;
import cn.lcy.answer.sem.model.SemanticGraphVertex;
import cn.lcy.answer.sem.model.Word;

import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;

@Service("queryService")
public class QueryServiceImpl implements QueryServiceI {
	
	private QueryDAOI queryDAO;
	
	/**
	 * 根据语义图构造陈述
	 */
	@Override
	public List<AnswerStatement> createStatement(SemanticGraph semanticGraph) {
		// 陈述集合
		List<AnswerStatement> answerStatements = new ArrayList<AnswerStatement>();
		// 解析语义图  构造陈述
		for(SemanticGraphVertex sourceVertex : semanticGraph.getAllVertices()) {
	    	for(SemanticGraphEdge edge : semanticGraph.getOutgoingEdges(sourceVertex)) {
	    		SemanticGraphVertex destVertex = new SemanticGraphVertex();
	    		  for(SemanticGraphVertex v : semanticGraph.getAllVertices()) {
	    			  for(SemanticGraphEdge e: semanticGraph.getIncomingEdges(v)) {
	    				  if(e.equals(edge)) {
	    					  destVertex = v;
	    				  }
	    			  }
	    		  }
	    		 AnswerStatement answerStatement = new AnswerStatement();
	    		/* for(PolysemantEntity polysemantEntity : polysemantEntities) {
	    			 if(sourceVertex.getCoNLLWord().ID == polysemantEntity.getPosition()) {
	    				 sourceVertex.getCoNLLWord().LEMMA = polysemantEntity.getUUID();
	    			 }
	    			 if(destVertex.getCoNLLWord().ID == polysemantEntity.getPosition()) {
	    				 destVertex.getCoNLLWord().LEMMA = polysemantEntity.getUUID();
	    			 }
	    		 }*/
	    		 answerStatement.setSubject(sourceVertex.getWord());
	    		 answerStatement.setPredicate(edge.getWord());
	    		 answerStatement.setObject(destVertex.getWord());
	    		 answerStatements.add(answerStatement);
	    	}
	    }
		Collections.sort(answerStatements);
		return answerStatements;
	}
	
	@Override
	public List<PolysemantStatement> createPolysemantStatements(List<AnswerStatement> answerStatements) {
		List<PolysemantStatement> polysemantStatements = new ArrayList<PolysemantStatement>();
		// key为实体的位置 value为该位置同名实体的个数
		Map<Integer, Integer> nums = new LinkedHashMap<Integer, Integer>();
		// 第一步：记录每个实体的同名实体的个数
		int index = 0;
		for(AnswerStatement answerStatement : answerStatements) {
			if(index == 0) {
				Word subject = answerStatement.getSubject();
				Word object = answerStatement.getObject();
				int subjectPolysemantNamedEntitiesSize = subject.getPolysemantNamedEntities().size();
				int objectPolysemantNamedEntitiesSize = object.getPolysemantNamedEntities().size();
				if(subjectPolysemantNamedEntitiesSize != 0) {
					nums.put(subject.getPosition(), subjectPolysemantNamedEntitiesSize);
				}
				if(objectPolysemantNamedEntitiesSize != 0) {
					nums.put(object.getPosition(), objectPolysemantNamedEntitiesSize);
				}
			} else {
				Word object = answerStatement.getObject();
				int objectPolysemantNamedEntitiesSize = object.getPolysemantNamedEntities().size();
				if(objectPolysemantNamedEntitiesSize != 0) {
					nums.put(object.getPosition(), objectPolysemantNamedEntitiesSize);
				}
			}
			index++;
		}
		
		int counter[] = new int[nums.size()];
		int loopLength = 1;
		for(Entry<Integer, Integer> num : nums.entrySet()) {
			loopLength *= num.getValue();
		}
		
		
		int counterIndex = nums.size() - 1;
		for(int i = 0; i < loopLength; i++) {
			PolysemantStatement polysemantStatement = new PolysemantStatement();
			List<AnswerStatement> newAnswerStatements = new ArrayList<AnswerStatement>();
			for(AnswerStatement answerStatement : answerStatements) {
				Word subject = answerStatement.getSubject();
				Word predicate = answerStatement.getPredicate();
				Word object = answerStatement.getObject();
				Word subjectNew = new Word();
				Word predicateNew = new Word();
				Word objectNew = new Word();
				BeanUtils.copyProperties(subject, subjectNew);
				BeanUtils.copyProperties(predicate, predicateNew);
				BeanUtils.copyProperties(object, objectNew);
				List<PolysemantNamedEntity> subjectPolysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
				for(PolysemantNamedEntity polysemantNamedEntity : subject.getPolysemantNamedEntities()) {
					PolysemantNamedEntity polysemantNamedEntityNew  = new PolysemantNamedEntity();
					BeanUtils.copyProperties(polysemantNamedEntity, polysemantNamedEntityNew);
					subjectPolysemantNamedEntities.add(polysemantNamedEntityNew);
				}
				subjectNew.setPolysemantNamedEntities(subjectPolysemantNamedEntities);
				
				/*List<PolysemantNamedEntity> predicatePolysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
				for(PolysemantNamedEntity polysemantNamedEntity : predicate.getPolysemantNamedEntities()) {
					PolysemantNamedEntity polysemantNamedEntityNew  = new PolysemantNamedEntity();
					BeanUtils.copyProperties(polysemantNamedEntity, polysemantNamedEntityNew);
					predicatePolysemantNamedEntities.add(polysemantNamedEntityNew);
				}
				predicateNew.setPolysemantNamedEntities(predicatePolysemantNamedEntities);*/
				
				List<PolysemantNamedEntity> objectPolysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
				for(PolysemantNamedEntity polysemantNamedEntity : object.getPolysemantNamedEntities()) {
					PolysemantNamedEntity polysemantNamedEntityNew  = new PolysemantNamedEntity();
					BeanUtils.copyProperties(polysemantNamedEntity, polysemantNamedEntityNew);
					objectPolysemantNamedEntities.add(polysemantNamedEntityNew);
				}
				objectNew.setPolysemantNamedEntities(objectPolysemantNamedEntities);
				
				AnswerStatement answerStatementNew = new AnswerStatement();
				answerStatementNew.setSubject(subjectNew);
				answerStatementNew.setPredicate(predicateNew);
				answerStatementNew.setObject(objectNew);
				/*int subjectPolysemantNamedEntitiesSize = subject.getPolysemantNamedEntities().size();
				int objectPolysemantNamedEntitiesSize = object.getPolysemantNamedEntities().size();*/
				
				
				int subscript = 0;
				for(Entry<Integer, Integer> num : nums.entrySet()) {
					if(num.getKey() == subjectNew.getPosition()) {
						subjectNew.active(subjectNew.getPolysemantNamedEntities().get(counter[subscript]));
					}
					if(num.getKey() == objectNew.getPosition()) {
						objectNew.active(objectNew.getPolysemantNamedEntities().get(counter[subscript]));
					}
					++subscript;
				}
				newAnswerStatements.add(answerStatementNew);
			}
			this.handle(nums, counter, counterIndex);
			polysemantStatement.setAnswerStatements(newAnswerStatements);
			polysemantStatements.add(polysemantStatement);
		}
		return polysemantStatements;
	}
	
	public void handle(Map<Integer, Integer> nums,int counter[], int counterIndex) {  
        counter[counterIndex]++;
        int index = 0;
        int length = 0;
        for(Entry<Integer, Integer> num : nums.entrySet()) {
        	if(index == counterIndex) {
        		length = num.getValue();
        	}
        	++index;
        }
        if (counter[counterIndex] >= length) {  
            counter[counterIndex] = 0;
            counterIndex--;
            if (counterIndex >= 0) {
            	this.handle(nums, counter, counterIndex); 
            }  
            counterIndex = nums.size() - 1;  
        }  
    }  
	
	/**
	 * 实体消岐
	 */
    public List<AnswerStatement> individualsDisambiguation(List<AnswerStatement> answerStatements) {
    	List<AnswerStatement> individualsDisambiguationStatements = new ArrayList<AnswerStatement>();
    	
    	for(AnswerStatement individualsDisambiguationStatement : answerStatements) {
    		
    		
    		if(individualsDisambiguationStatement.getSubject().getActiveEntity() != null) {
    			// 如果主语是问号的话 就不需要进行实体消岐了
        		if(individualsDisambiguationStatement.getSubject().getPosition() < 1024) {
    	    		// 查询本体库中是否有该实体
    	    		// TODO 改为注入 这里是为测试方便
    	    		// boolean individualExist = new JenaDAOImpl().individualExist(myStatement.getSubject().LEMMA);
    	    		// 设置实体消岐前的实体名 方便前端读取
    				System.out.println("进入的实体：" + individualsDisambiguationStatement.getSubject().getName());
    				// TODO 改为注入 这里是为测试方便 individualDisambiguation为UUID
    				
    				String individualDisambiguationName = null;
    				String sameEntityUUID = null;
    				if(individualsDisambiguationStatement.getSubject().getActiveEntity() != null) {
    					if(individualsDisambiguationStatement.getSubject().getActiveEntity().getIsAliases().equals("0")) { // 如果该实体为实体别名
    						sameEntityUUID = new QueryDAOImpl().querySameIndividual(individualsDisambiguationStatement.getSubject().getActiveEntity().getUUID());
    					}
    					
    				}
    				String entityUUID = sameEntityUUID == null ? individualsDisambiguationStatement.getSubject().getActiveEntity().getUUID() : sameEntityUUID;
    				
    				individualDisambiguationName = new QueryDAOImpl().queryIndividualComment(entityUUID);	// TODO 修改为注入 本体库中的comment表示该实体的实体名
    				individualsDisambiguationStatement.getSubject().setDisambiguationName(individualDisambiguationName);
    				individualsDisambiguationStatement.getSubject().setDisambiguationUUID(entityUUID);
    				System.out.println("消岐后的实体:" + individualsDisambiguationStatement.getSubject().getDisambiguationName());
    				// 设置实体消岐后的实体名 方便前端读取
    				//myStatement.setSubjectDisambiguation(myStatement.getSubject().LEMMA);
        		}
    		}
    		individualsDisambiguationStatements.add(individualsDisambiguationStatement);
    	}
    	return individualsDisambiguationStatements;
    }
    
    /**
     * 谓语消岐
     */
    public List<AnswerStatement> predicateDisambiguation(List<AnswerStatement> answerStatements) {
    	List<AnswerStatement> predicateDisambiguationStatements = new ArrayList<AnswerStatement>();
    	int index = 0;
    	for(AnswerStatement predicateDisambiguationStatement : answerStatements) {
    		Long minDistance = 9223372036854775807L;
    		 // 问句中的谓语
	        String predicateOld = predicateDisambiguationStatement.getPredicate().getName();
	        String mostSimilarPredicate = predicateOld;
	        
    		//String object = answerStatement.getObject().getName();
    		// 设置消岐前的谓语 方便前端读取
    		//answerStatement.setPredicateName(predicateOld);
    		// TODO 改为注入 这里是为测试方便 
    		// 如果主语不是问号 则进行获取主语的相关断言
    		List<Statement> statements = null;
    		if(predicateDisambiguationStatement.getSubject().getPosition() < 1024) {
    			String subjectUUID = predicateDisambiguationStatement.getSubject().getDisambiguationUUID();
    			statements = new QueryDAOImpl().getStatementsBySubject(subjectUUID);
    		} else {
    			// 如果主语是问号 则需要进行一次前断言的查询 然后消岐
    			List<AnswerStatement> preStatements = new ArrayList<AnswerStatement>();
    			AnswerStatement preStatement = answerStatements.get(index - 1);
    			AnswerStatement preStatementNew = new AnswerStatement();
    			BeanUtils.copyProperties(preStatement, preStatementNew);
    			Word subject = preStatement.getSubject();
				Word predicate = preStatement.getPredicate();
				Word object = preStatement.getObject();
				Word subjectNew = new Word();
				Word predicateNew = new Word();
				Word objectNew = new Word();
				BeanUtils.copyProperties(subject, subjectNew);
				BeanUtils.copyProperties(predicate, predicateNew);
				BeanUtils.copyProperties(object, objectNew);
				preStatementNew.setSubject(subjectNew);
				preStatementNew.setPredicate(predicateNew);
				preStatementNew.setObject(objectNew);
    			preStatements.add(preStatementNew);
    			
    			List<AnswerStatement> preQueryStatements = new QueryServiceImpl().createQueryStatements(preStatements);
    			String SPARQL = this.createSparql(preQueryStatements);
    			QueryResult result = new QueryDAOImpl().queryOntology(SPARQL);
    			Word subjectUpdate = predicateDisambiguationStatement.getSubject();
    			// 更新主语
    			subjectUpdate.setName(result.getAnswers().get(0).getContent().split(":")[1]);
    			predicateDisambiguationStatement.setSubject(subjectUpdate);
    			String subjectUpdateStr = predicateDisambiguationStatement.getSubject().getName();
    			statements = new QueryDAOImpl().getStatementsBySubject(subjectUpdateStr);
    			
    			
    			/*statements = new JenaDAOImpl().getStatementsByObject(object);*/
    		}
	        
	        // 迭代所有以Subject为主语的短语 寻找最相似的谓语
    		for(Statement statement : statements) {
    			// 本体库中的谓语
    			String predicate = statement.getPredicate().getURI().split("#")[1];
        		// 计算句子中谓语和本体库中谓语的语义相似度
        		String[] predicateArray = predicate.split("有");
        		if(predicateArray.length >= 2) {
        			Long distance = CoreSynonymDictionary.distance(predicateOld, predicateArray[1]);
        			if(distance < minDistance) {
        				minDistance = distance;
        				mostSimilarPredicate = predicateArray[1];
        			}
        			System.out.println(predicateOld + " 和  " + predicateArray[1] + ":" + distance);
        		} else {
        			Long distance = CoreSynonymDictionary.distance(predicateOld, predicateArray[0]);
        			if(distance < minDistance) {
        				minDistance = distance;
        				mostSimilarPredicate = predicateArray[0];
        			}
        			System.out.println(predicateOld + " 和  " + predicateArray[0] + ":" + distance);
        		}
        		System.out.println();
    		}
    		predicateDisambiguationStatement.getPredicate().setDisambiguationName(mostSimilarPredicate);
    		// 设置谓语消岐的结果 方便前端读取
    		//answerStatement.setPredicateDisambiguation(mostSimilarPredicate);
    		predicateDisambiguationStatements.add(predicateDisambiguationStatement);
    		System.out.println("谓语消岐距离：" + minDistance);
    		System.out.println("谓语消岐为：" + mostSimilarPredicate);
    		++index;
    	}
    	return predicateDisambiguationStatements;
    }
    
    /**
     * 构造查询断言
     */
    @Override
	public List<AnswerStatement> createQueryStatements(List<AnswerStatement> answerStatements) {
    	List<AnswerStatement> queryStatements = new ArrayList<AnswerStatement>();
    	int i = 0;
    	for(AnswerStatement answerStatement : answerStatements) {
    		answerStatement.getSubject().setDisambiguationName("mymo:" + answerStatement.getSubject().getDisambiguationUUID());
    		
    		if(NounsTagEnum.isIncludeEnumElement(answerStatement.getPredicate().getCpostag())) {
    			answerStatement.getPredicate().setDisambiguationName("mymo:" + "有" + answerStatement.getPredicate().getDisambiguationName());
    		} else {
    			answerStatement.getPredicate().setDisambiguationName("mymo:" + answerStatement.getPredicate().getDisambiguationName());
    		}
    		
    		int ch = 'a' + i;
    		String preMask = "?" + (char)(ch - 1);
    		String mask = "?" + (char)ch;
    		answerStatement.getObject().setDisambiguationName(mask);
    		if(i > 0) {
    			answerStatement.getSubject().setDisambiguationName(preMask);
    		}
    		queryStatements.add(answerStatement);
    		++i;
    	}
    	return queryStatements;
	}
    
	// TODO 根据断言集合构建查询语句
	@Override
	public String createSparql(List<AnswerStatement> answerStatements) {
		// 断言集合
		int size = answerStatements.size();
		// 取出最后一个断言
		AnswerStatement lastStatement = answerStatements.get(size - 1);
		String queryMask = lastStatement.getObject().getDisambiguationName();
		String SPARQL = "SELECT " + queryMask + "  WHERE {\n";
		for(AnswerStatement answerStatement : answerStatements) {
			// 如果谓语是名词
			if(NounsTagEnum.isIncludeEnumElement(answerStatement.getPredicate().getCpostag())) {
				SPARQL += answerStatement.getSubject().getDisambiguationName() + "  " + answerStatement.getPredicate().getDisambiguationName()  + "  " + answerStatement.getObject().getDisambiguationName() + ".\n";
			} else {
				SPARQL += answerStatement.getSubject().getDisambiguationName() + "  " + answerStatement.getPredicate().getDisambiguationName()  + "  " + answerStatement.getObject().getDisambiguationName() + ".\n";
			}
		}
		SPARQL += "}";
		return SPARQL;
	}
	
	 /**
     * 根据断言集合多次构建查询语句   多次构造 多次调用 每次调用的时候传入一个 二个 ...断言
     * @param myStatements
     * @return
     */
    public List<String> createSparqls(List<AnswerStatement> answerStatements) {
    	List<String> SPARQLS = new ArrayList<String>();
    	
    	int size = answerStatements.size();
    	for(int num = 0; num < size; num++) {
    		List<AnswerStatement> statements = new ArrayList<AnswerStatement>();
    		for(int i = 0; i <= num; i++) {
    			statements.add(answerStatements.get(i));
    		}
    		String SPARQL = this.createSparql(statements);
			SPARQLS.add(SPARQL);
    	}
    	return SPARQLS;
    }
	
	// TODO 改为注入 这里是为测试方便
	@Override
	public QueryResult queryOntology(String sparql) {
		return new QueryDAOImpl().queryOntology(sparql);
	}
	
	@Override
	public String queryIndividualComment(String individualName) {
		return new QueryDAOImpl().queryIndividualComment(individualName);
	}
	
	public QueryDAOI getQueryDAO() {
		return queryDAO;
	}
	
	@Autowired
	public void setQueryDAO(QueryDAOI queryDAO) {
		this.queryDAO = queryDAO;
	}
	
}