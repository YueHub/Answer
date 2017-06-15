package cn.lcy.answer.naction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.BeanUtils;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;

import cn.lcy.answer.log.UserOperationLog;
import cn.lcy.answer.service.GrammarParserGraphServiceI;
import cn.lcy.answer.service.GrammarParserGraphServiceImpl;
import cn.lcy.answer.service.KnowledgeGraphServiceI;
import cn.lcy.answer.service.KnowledgeGraphServiceImpl;
import cn.lcy.answer.service.SemanticGraphViewServiceI;
import cn.lcy.answer.service.SemanticGraphViewServiceImpl;
import cn.lcy.answer.vo.AnswerResultVO;
import cn.lcy.answer.vo.DependencyVO;
import cn.lcy.answer.vo.KnowledgeGraphVO;
import cn.lcy.answer.vo.PolysemantSituationVO;
import cn.lcy.answer.vo.SemanticGraphVO;
import cn.lcy.knowledge.analysis.grammar.service.GrammarParserServiceI;
import cn.lcy.knowledge.analysis.grammar.service.GrammarParserServiceImpl;
import cn.lcy.knowledge.analysis.namedentity.service.NamedEntityServiceI;
import cn.lcy.knowledge.analysis.namedentity.service.NamedEntityServiceImpl;
import cn.lcy.knowledge.analysis.ontology.query.service.QueryServiceI;
import cn.lcy.knowledge.analysis.ontology.query.service.QueryServiceImpl;
import cn.lcy.knowledge.analysis.seg.service.WordSegmentationServiceI;
import cn.lcy.knowledge.analysis.seg.service.WordSegmentationServiceImpl;
import cn.lcy.knowledge.analysis.sem.graph.service.SemanticGraphServiceI;
import cn.lcy.knowledge.analysis.sem.graph.service.SemanticGraphServiceImpl;
import cn.lcy.knowledge.analysis.sem.model.Answer;
import cn.lcy.knowledge.analysis.sem.model.AnswerStatement;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;
import cn.lcy.knowledge.analysis.sem.model.PolysemantStatement;
import cn.lcy.knowledge.analysis.sem.model.QueryResult;
import cn.lcy.knowledge.analysis.sem.model.SemanticGraph;
import cn.lcy.knowledge.analysis.sem.model.Word;
import cn.lcy.knowledge.analysis.sem.model.WordSegmentResult;

@ParentPackage("basePackage")
@Namespace("/front")
@Action(value = "developerAction")
@Results({
	@Result(name = "answer_result", location = "/front/answer_result.jsp")
	}) 
public class DeveloperAction extends BaseAction implements SessionAware {

	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户日志对象
	 */
	private static Logger logger = Logger.getLogger("UserLog");
	
	private Map<String,Object> session;
	private WordSegmentationServiceI wordSegmentationService = WordSegmentationServiceImpl.getInstance();
	private NamedEntityServiceI namedEntityService = NamedEntityServiceImpl.getInstance();
	private GrammarParserServiceI grammarParserService = GrammarParserServiceImpl.getInstance();
	private SemanticGraphServiceI semanticGraphService = SemanticGraphServiceImpl.getInstance();
	private QueryServiceI queryService = QueryServiceImpl.getInstance();
	private KnowledgeGraphServiceI knowledgeGraphService = KnowledgeGraphServiceImpl.getInstance();
	
	private String question;
	
	public String answer() throws Exception {
		// 第一步：HanLP分词
		WordSegmentResult wordSegmentResult = wordSegmentationService.wordSegmentation(question);
		List<Term> terms = wordSegmentResult.getTerms();
		List<PolysemantNamedEntity> polysemantNamedEntities = wordSegmentResult.getPolysemantEntities();
		List<Word> words = wordSegmentResult.getWords();
		System.out.println("HanLP分词的结果为:" + terms);
		
		// :查询本体库、取出命名实体的相关数据属性和对象属性
		polysemantNamedEntities = namedEntityService.fillNamedEntities(polysemantNamedEntities);
		
		// 第二步：使用HanLP进行依存句法分析
		CoNLLSentence coNLLsentence = grammarParserService.dependencyParser(terms);
		session.put("coNLLsentence", coNLLsentence);	// 前端进行文法依存关系展示
		System.out.println("HanLP依存语法解析结果：\n" + coNLLsentence);
		
		// 第三步：语义图构建
		SemanticGraph semanticGraph = semanticGraphService.buildSemanticGraph(coNLLsentence, polysemantNamedEntities);
		if(semanticGraph.getAllVertices().size() == 0) { // 说明没有语义图算法无法解析该问句
			semanticGraph = semanticGraphService.buildBackUpSemanticGraph(words);
		}
		
		// 第四步：语义图断言构建
		List<AnswerStatement> semanticStatements = queryService.createStatement(semanticGraph);
		
		// 第五步：获取歧义断言
		List<PolysemantStatement> polysemantStatements = queryService.createPolysemantStatements(semanticStatements);
		
		List<PolysemantSituationVO> polysemantSituationVOs = new ArrayList<PolysemantSituationVO>();
		
		for(PolysemantStatement polysemantStatement : polysemantStatements) {
			// 第六步：实体消岐
			List<AnswerStatement> individualsDisambiguationStatements = queryService.individualsDisambiguation(polysemantStatement.getAnswerStatements());
			List<AnswerStatement> individualsDisambiguationStatementsNew = new ArrayList<AnswerStatement>();
			for(AnswerStatement answerStatement : individualsDisambiguationStatements) {
				Word subject = answerStatement.getSubject();
				Word predicate = answerStatement.getPredicate();
				Word object = answerStatement.getObject();
				Word subjectNew = new Word();
				Word predicateNew = new Word();
				Word objectNew = new Word();
				BeanUtils.copyProperties(subject, subjectNew);
				BeanUtils.copyProperties(predicate, predicateNew);
				BeanUtils.copyProperties(object, objectNew);
				AnswerStatement answerStatementNew = new AnswerStatement();
				answerStatementNew.setSubject(subjectNew);
				answerStatementNew.setPredicate(predicateNew);
				answerStatementNew.setObject(objectNew);
				individualsDisambiguationStatementsNew.add(answerStatementNew);
			}
			
			// 第七步：谓语消岐
			List<AnswerStatement> predicateDisambiguationStatements = queryService.predicateDisambiguation(individualsDisambiguationStatementsNew);
			List<AnswerStatement> predicateDisambiguationStatementsNew = new ArrayList<AnswerStatement>();
			for(AnswerStatement answerStatement : predicateDisambiguationStatements) {
				Word subject = answerStatement.getSubject();
				Word predicate = answerStatement.getPredicate();
				Word object = answerStatement.getObject();
				Word subjectNew = new Word();
				Word predicateNew = new Word();
				Word objectNew = new Word();
				BeanUtils.copyProperties(subject, subjectNew);
				BeanUtils.copyProperties(predicate, predicateNew);
				BeanUtils.copyProperties(object, objectNew);
				AnswerStatement answerStatementNew = new AnswerStatement();
				answerStatementNew.setSubject(subjectNew);
				answerStatementNew.setPredicate(predicateNew);
				answerStatementNew.setObject(objectNew);
				predicateDisambiguationStatementsNew.add(answerStatementNew);
			}
			
			// 第八步：构造用于Jena查询的断言
			List<AnswerStatement> queryStatements = queryService.createQueryStatements(predicateDisambiguationStatementsNew);
			session.put("queryStatements", queryStatements);
			List<AnswerStatement> queryStatementsNew = new ArrayList<AnswerStatement>();
			for(AnswerStatement answerStatement : queryStatements) {
				Word subject = answerStatement.getSubject();
				Word predicate = answerStatement.getPredicate();
				Word object = answerStatement.getObject();
				Word subjectNew = new Word();
				Word predicateNew = new Word();
				Word objectNew = new Word();
				BeanUtils.copyProperties(subject, subjectNew);
				BeanUtils.copyProperties(predicate, predicateNew);
				BeanUtils.copyProperties(object, objectNew);
				AnswerStatement answerStatementNew = new AnswerStatement();
				answerStatementNew.setSubject(subjectNew);
				answerStatementNew.setPredicate(predicateNew);
				answerStatementNew.setObject(objectNew);
				queryStatementsNew.add(answerStatementNew);
			}
			
			// 第九步：根据查询断言构建查询语句
			List<String> SPARQLS = queryService.createSparqls(queryStatementsNew);
			List<QueryResult> queryResults = new ArrayList<QueryResult>();
			for(String SPARQL : SPARQLS) {
				// 执行查询语句
				QueryResult queryResult = queryService.queryOntology(SPARQL);
				List<Answer> answersNew = new ArrayList<Answer>();
				for(Answer answer : queryResult.getAnswers()) {
					String[] uuidArr = answer.getContent().split(":");
					String uuid = null;
					if(uuidArr.length > 1) {
						uuid = uuidArr[1];
					} else {
						uuid = uuidArr[0];
					}
					if(uuidArr.length <= 1 || answer.getContent().length() != 33) {
						answersNew.add(answer);
					} else {
						String comment = queryService.queryIndividualComment(uuid);
						Answer answerNew = new Answer();
						answerNew.setContent(comment);
						answersNew.add(answerNew);
					}
				}
				queryResult.setAnswers(answersNew);
				queryResults.add(queryResult);
			}
			
			PolysemantSituationVO polysemantSituationVO = new PolysemantSituationVO();
			polysemantSituationVO.setPolysemantStatement(polysemantStatement);
			polysemantSituationVO.setSemanticStatements(semanticStatements);
			List<PolysemantNamedEntity> activePolysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
			int index = 0;
			for(AnswerStatement answerStatementNew : polysemantStatement.getAnswerStatements()) {
				PolysemantNamedEntity subjectActivePolysemantNamedEntity = answerStatementNew.getSubject().getActiveEntity();
				PolysemantNamedEntity objectActivePolysemantNamedEntity = answerStatementNew.getObject().getActiveEntity();
				if(index == 0) {
					activePolysemantNamedEntities.add(subjectActivePolysemantNamedEntity);
					activePolysemantNamedEntities.add(objectActivePolysemantNamedEntity);
				} else {
					activePolysemantNamedEntities.add(objectActivePolysemantNamedEntity);
				}
			}
			polysemantSituationVO.setActivePolysemantNamedEntities(activePolysemantNamedEntities);// 激活的命名实体
			polysemantSituationVO.setIndividualsDisambiguationStatements(individualsDisambiguationStatements);
			polysemantSituationVO.setPredicateDisambiguationStatements(predicateDisambiguationStatements);
			polysemantSituationVO.setQueryStatements(queryStatements);
			polysemantSituationVO.setSPARQLS(SPARQLS);
			polysemantSituationVO.setQueryResults(queryResults);
			polysemantSituationVOs.add(polysemantSituationVO);
		}
		
		AnswerResultVO answerResultVO = new AnswerResultVO(); // 结果的封装
		answerResultVO.setWords(words);
		answerResultVO.setPolysemantSituationVOs(polysemantSituationVOs);
		session.put("answerResultVO", answerResultVO);	// 传到前端
		session.put("polysemantNamedEntities", polysemantNamedEntities); // 用于获取知识图谱
		session.put("question", question);
		
		UserOperationLog userOperationLog = new UserOperationLog();
		this.initLog(userOperationLog); // 初始化日志
		logger.info(userOperationLog);
		this.writeJson(answerResultVO);
		return "answer_result";
	}
	
	/**
	 * 前端获取依赖关系图
	 * @return
	 */
	public String getDependencyGraph() {
		GrammarParserGraphServiceI grammarParserGraphService = GrammarParserGraphServiceImpl.getInstance();
		CoNLLSentence coNLLsentence = (CoNLLSentence)session.get("coNLLsentence");
		DependencyVO dependencyVO = grammarParserGraphService.getDependencyGraphVO(coNLLsentence); 	// 获取依存语法图
		this.writeJson(dependencyVO); // 将依赖关系VO对象以JSON格式写入到前端
		return "answer_result";
	}
	
	/**
	 * 前端获取语义图图形（用查询断言表示）
	 * @return
	 */
	public String getSemanticGraph() {
		SemanticGraphViewServiceI semanticGraphViewService = SemanticGraphViewServiceImpl.getInstance();
		
		@SuppressWarnings("unchecked")
		List<AnswerStatement> queryStatements = (List<AnswerStatement>)session.get("queryStatements");
		SemanticGraphVO semanticGraphVO = new SemanticGraphVO();
		if(queryStatements != null) {
			semanticGraphVO = semanticGraphViewService.getSemanticGraphVO(queryStatements); // 将语义图解析成前端能够处理的json格式
		}
		this.writeJson(semanticGraphVO); 	// 将语义图以JSON格式写到前端
		return "answer_result";
	}
	
	/**
	 * 前端获取知识图谱
	 * @return
	 */
	public String getKnowledgeGraph() {
		@SuppressWarnings("unchecked")
		List<PolysemantNamedEntity> polysemantNamedEntities = (List<PolysemantNamedEntity>) session.get("polysemantNamedEntities");
		List<KnowledgeGraphVO> knowledgeGraphVOs = new ArrayList<KnowledgeGraphVO>();
		if(polysemantNamedEntities != null) {
			knowledgeGraphVOs = knowledgeGraphService.getKnowledgeGraphVO(polysemantNamedEntities);
		}
		session.put("knowledgeGraphVOs", knowledgeGraphVOs);
		this.writeJson(knowledgeGraphVOs);
		return "answer_result";
	}
	
	public Map<String, Object> getSession() {
		return session;
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
}