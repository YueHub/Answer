package cn.lcy.answer.service;

import java.util.List;

import cn.lcy.answer.vo.SemanticGraphVO;
import cn.lcy.knowledge.analysis.sem.model.AnswerStatement;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public interface SemanticGraphViewService {

    /**
     * 获取语义图 便于前端显示
     *
     * @param queryStatements
     * @return
     */
    SemanticGraphVO getSemanticGraphVO(List<AnswerStatement> queryStatements);


}
