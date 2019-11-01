package cn.lcy.answer.vo;

import java.util.List;

import cn.lcy.knowledge.analysis.sem.model.Word;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public class AnswerResultVO implements java.io.Serializable {

    /**
     * default serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 提问时间 (后台接收到提问的时间点)
     */
    private Long askTime;

    /**
     * 回答时间（后台给出回答的时间点）
     */
    private Long answerTime;

    /**
     * 问题
     */
    private String question;

    /**
     * 分词 / 命名实体识别
     */
    private List<Word> words;

    /**
     * 简短答案
     */
    private ShortAnswerVO shortAnswer;

    private List<KnowledgeGraphVO> knowledgeGraphVos;

    public Long getAskTime() {
        return askTime;
    }

    public void setAskTime(Long askTime) {
        this.askTime = askTime;
    }

    public Long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Long answerTime) {
        this.answerTime = answerTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public ShortAnswerVO getShortAnswer() {
        return shortAnswer;
    }

    public void setShortAnswer(ShortAnswerVO shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public List<KnowledgeGraphVO> getKnowledgeGraphVos() {
        return knowledgeGraphVos;
    }

    public void setKnowledgeGraphVos(List<KnowledgeGraphVO> knowledgeGraphVos) {
        this.knowledgeGraphVos = knowledgeGraphVos;
    }

    @Override
    public String toString() {
        return "AnswerResultVO{" +
                "askTime=" + askTime +
                ", answerTime=" + answerTime +
                ", question='" + question + '\'' +
                ", words=" + words +
                ", shortAnswer=" + shortAnswer +
                ", knowledgeGraphVos=" + knowledgeGraphVos +
                '}';
    }
}
