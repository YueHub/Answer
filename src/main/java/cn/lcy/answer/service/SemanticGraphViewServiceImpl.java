package cn.lcy.answer.service;

import java.util.ArrayList;
import java.util.List;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

import cn.lcy.answer.vo.SemanticGraphNodeVO;
import cn.lcy.answer.vo.SemanticGraphStatementVO;
import cn.lcy.answer.vo.SemanticGraphVO;
import cn.lcy.knowledge.analysis.sem.model.AnswerStatement;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;
import cn.lcy.knowledge.analysis.sem.model.Word;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public class SemanticGraphViewServiceImpl implements SemanticGraphViewService {

    private volatile static SemanticGraphViewServiceImpl singleInstance;

    /**
     * 私有化构造方法，实现单例模式
     */
    private SemanticGraphViewServiceImpl() {
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static SemanticGraphViewService getInstance() {
        if (singleInstance == null) {
            synchronized (SemanticGraphViewServiceImpl.class) {
                if (singleInstance == null) {
                    singleInstance = new SemanticGraphViewServiceImpl();
                }
            }
        }
        return singleInstance;
    }

    /**
     * 获取语义图VO对象
     */
    @Override
    public SemanticGraphVO getSemanticGraphVO(List<AnswerStatement> queryStatements) {
        // 断言集合
        SemanticGraphVO semanticGraphVO = new SemanticGraphVO();
        // 解析语义图  构造断言
        for (AnswerStatement queryStatement : queryStatements) {
            SemanticGraphStatementVO semanticGraphStatementVO = new SemanticGraphStatementVO();
            SemanticGraphNodeVO subject = new SemanticGraphNodeVO();
            SemanticGraphNodeVO predicate = new SemanticGraphNodeVO();
            SemanticGraphNodeVO object = new SemanticGraphNodeVO();

            subject.setId(queryStatement.getSubject().getPosition());
            if (queryStatement.getSubject().getName().split(":").length > 1) {
                subject.setName(queryStatement.getSubject().getName().split(":")[1]);
            } else {
                subject.setName(queryStatement.getSubject().getName().split(":")[0]);
            }

            subject.setColor("red");
            // dot 表示圆形
            subject.setShape("dot");
            subject.setAlpha(1);

            predicate.setId(queryStatement.getPredicate().getPosition());
            predicate.setName(queryStatement.getPredicate().getName());
            predicate.setColor("#b2b19d");
            predicate.setSize(Double.valueOf(15));
            // 默认为矩形
            // predicate.setShape("rect")
            predicate.setAlpha(1);

            object.setId(queryStatement.getObject().getPosition());
            object.setName(queryStatement.getObject().getName());
            object.setColor("#b2b19d");
            object.setShape("dot");
            object.setAlpha(1);

            semanticGraphStatementVO.setSubject(subject);
            semanticGraphStatementVO.setPredicate(predicate);
            semanticGraphStatementVO.setObject(object);
            semanticGraphVO.getSemanticGraphStatements().add(semanticGraphStatementVO);
        }
        return semanticGraphVO;
    }

    /**
     * 将CoNLLWord类型转换成Word类型
     *
     * @param coNllWord
     * @return
     */
    public Word convertedIntoWord(CoNLLWord coNllWord, List<PolysemantNamedEntity> polysemantNamedEntities) {
        List<PolysemantNamedEntity> wordPolysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
        Word word = new Word();
        word.setPosition(coNllWord.ID);
        word.setName(coNllWord.LEMMA);
        word.setCpostag(coNllWord.CPOSTAG);
        word.setPostag(coNllWord.POSTAG);
        for (PolysemantNamedEntity polysemantNamedEntity : polysemantNamedEntities) {
            if (polysemantNamedEntity.getPosition() == word.getPosition()) {
                wordPolysemantNamedEntities.add(polysemantNamedEntity);
            }
        }
        word.setPolysemantNamedEntities(wordPolysemantNamedEntities);
        return word;
    }

}
