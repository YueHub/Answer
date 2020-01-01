package cn.lcy.answer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;

import cn.lcy.answer.service.KnowledgeGraphService;
import cn.lcy.answer.service.KnowledgeGraphServiceImpl;
import cn.lcy.answer.vo.AnswerResultVO;
import cn.lcy.answer.vo.KnowledgeGraphVO;
import cn.lcy.answer.vo.PolysemantSituationVO;
import cn.lcy.answer.vo.ShortAnswerVO;
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

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
@RestController
public class AnswerController {

    private WordSegmentationServiceI wordSegmentationService = WordSegmentationServiceImpl.getInstance();
    private NamedEntityServiceI namedEntityService = NamedEntityServiceImpl.getInstance();
    private GrammarParserServiceI grammarParserService = GrammarParserServiceImpl.getInstance();
    private SemanticGraphServiceI semanticGraphService = SemanticGraphServiceImpl.getInstance();
    private QueryServiceI queryService = QueryServiceImpl.getInstance();
    private KnowledgeGraphService knowledgeGraphService = KnowledgeGraphServiceImpl.getInstance();

    @GetMapping("/")
    public String sayHello() {
        return "Welcome! Developer!";
    }

    @RequestMapping(value = "/answer", method = RequestMethod.GET)
    @ResponseBody
    public AnswerResultVO answer(@RequestParam(value = "q", required = false, defaultValue = "周杰伦") String q) {
        long askTime = System.currentTimeMillis();
        // 返回的结果对象
        AnswerResultVO answerResultVO = new AnswerResultVO();
        // 后台接收到提问的时间点
        answerResultVO.setAskTime(askTime);

        /* 第一步：HanLP分词 */
        WordSegmentResult wordSegmentResult = wordSegmentationService.wordSegmentation(q);
        List<Term> terms = wordSegmentResult.getTerms();
        List<PolysemantNamedEntity> polysemantNamedEntities = wordSegmentResult.getPolysemantEntities();
        List<Word> words = wordSegmentResult.getWords();
        System.out.println("HanLP分词的结果为:" + terms);

        /* 第二步：使用HanLP进行依存句法分析 */
        CoNLLSentence coNLLsentence = grammarParserService.dependencyParser(terms);
        System.out.println("HanLP依存语法解析结果：\n" + coNLLsentence);

        /* 第三步：语义图构建 */
        SemanticGraph semanticGraph = semanticGraphService.buildSemanticGraph(coNLLsentence, polysemantNamedEntities);
        /* 说明没有语义图算法无法解析该问句 */
        if (semanticGraph.getAllVertices().size() == 0) {
            semanticGraph = semanticGraphService.buildBackUpSemanticGraph(words);
        }

        // 第四步：语义图断言构建
        List<AnswerStatement> semanticStatements = queryService.createStatement(semanticGraph);

        // 第五步：获取歧义断言 - 解决同名实体
        List<PolysemantStatement> polysemantStatements = queryService.createPolysemantStatements(semanticStatements);

        List<PolysemantSituationVO> polysemantSituationVOs = new ArrayList<PolysemantSituationVO>();

        for (PolysemantStatement polysemantStatement : polysemantStatements) {
            // 第六步：实体消岐 - 实体别名
            List<AnswerStatement> individualsDisambiguationStatements = queryService
                    .individualsDisambiguation(polysemantStatement.getAnswerStatements());

            // 第七步：谓语消岐
            List<AnswerStatement> predicateDisambiguationStatements = queryService
                    .predicateDisambiguation(individualsDisambiguationStatements);

            // 第八步：构造用于 Jena 查询的断言
            List<AnswerStatement> queryStatements = queryService
                    .createQueryStatements(predicateDisambiguationStatements);

            // 第九步：根据查询断言构建查询语句
            List<String> sparqls = queryService.createSparqls(queryStatements);
            List<QueryResult> queryResults = new ArrayList<QueryResult>();
            for (String sparql : sparqls) {
                // 执行查询语句
                QueryResult queryResult = queryService.queryOntology(sparql);
                List<Answer> answersNew = new ArrayList<Answer>();
                for (Answer answer : queryResult.getAnswers()) {
                    String[] uuidArr = answer.getContent().split(":");
                    String uuid = null;
                    if (uuidArr.length > 1) {
                        uuid = uuidArr[1];
                    } else {
                        uuid = uuidArr[0];
                    }
                    if (uuidArr.length <= 1 || answer.getContent().length() != 33) {
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
            for (AnswerStatement answerStatementNew : polysemantStatement.getAnswerStatements()) {
                PolysemantNamedEntity subjectActivePolysemantNamedEntity = answerStatementNew.getSubject().acquireActiveEntity();
                PolysemantNamedEntity objectActivePolysemantNamedEntity = answerStatementNew.getObject().acquireActiveEntity();
                if (index == 0) {
                    activePolysemantNamedEntities.add(subjectActivePolysemantNamedEntity);
                    activePolysemantNamedEntities.add(objectActivePolysemantNamedEntity);
                } else {
                    activePolysemantNamedEntities.add(objectActivePolysemantNamedEntity);
                }
                ++index;
            }
            /* 激活的命名实体 */
            polysemantSituationVO.setActivePolysemantNamedEntities(activePolysemantNamedEntities);
            polysemantSituationVO.setIndividualsDisambiguationStatements(individualsDisambiguationStatements);
            polysemantSituationVO.setPredicateDisambiguationStatements(predicateDisambiguationStatements);
            polysemantSituationVO.setQueryStatements(queryStatements);
            polysemantSituationVO.setSparqls(sparqls);
            polysemantSituationVO.setQueryResults(queryResults);
            polysemantSituationVOs.add(polysemantSituationVO);
        }

        // :查询本体库、取出命名实体的相关数据属性和对象属性 - 此时再填充数据，以免前端获取太多冗余数据
        namedEntityService.fillNamedEntities(polysemantNamedEntities);

        /* 封装结果 */
        // 设置问题
        answerResultVO.setQuestion(q);
        long answerTime = System.currentTimeMillis();
        // 后台完成回答的时间点
        answerResultVO.setAnswerTime(answerTime);

        // 包含命名实体等信息
        answerResultVO.setWords(words);

        ShortAnswerVO shortAnswer = new ShortAnswerVO();
        shortAnswer.setPolysemantSituationVOs(polysemantSituationVOs);
        answerResultVO.setShortAnswer(shortAnswer);

        List<KnowledgeGraphVO> knowledgeGraphVOs = new ArrayList<KnowledgeGraphVO>();
        if (polysemantNamedEntities != null) {
            knowledgeGraphVOs = knowledgeGraphService.getKnowledgeGraphVO(polysemantNamedEntities);
        }
        answerResultVO.setKnowledgeGraphVos(knowledgeGraphVOs);

        return answerResultVO;
    }
}
