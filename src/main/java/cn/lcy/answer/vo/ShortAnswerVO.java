package cn.lcy.answer.vo;

import java.util.List;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public class ShortAnswerVO {

    private List<PolysemantSituationVO> polysemantSituationVOs;

    public List<PolysemantSituationVO> getPolysemantSituationVOs() {
        return polysemantSituationVOs;
    }

    public void setPolysemantSituationVOs(List<PolysemantSituationVO> polysemantSituationVOs) {
        this.polysemantSituationVOs = polysemantSituationVOs;
    }

    @Override
    public String toString() {
        return "ShortAnswerVO{" +
                "polysemantSituationVOs=" + polysemantSituationVOs +
                '}';
    }
}
