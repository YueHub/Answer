package cn.lcy.answer.vo;

import java.util.List;

import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public class KnowledgeGraphNodeVO implements java.io.Serializable {

    /**
     * default serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 连接线顺序
     */
    private Integer id;

    /**
     * 结点名称
     */
    private String name;

    /**
     * 结点形状
     */
    private String shape;

    /**
     * 结点颜色
     */
    private String color;

    /**
     * 结点大小
     */
    private Double size;

    /**
     *
     */
    private Integer alpha;

    private List<PolysemantNamedEntity> polysemantNamedEntities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public List<PolysemantNamedEntity> getPolysemantNamedEntities() {
        return polysemantNamedEntities;
    }

    public void setPolysemantNamedEntities(
            List<PolysemantNamedEntity> polysemantNamedEntities) {
        this.polysemantNamedEntities = polysemantNamedEntities;
    }

    @Override
    public String toString() {
        return "KnowledgeGraphNodeVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shape='" + shape + '\'' +
                ", color='" + color + '\'' +
                ", size=" + size +
                ", alpha=" + alpha +
                ", polysemantNamedEntities=" + polysemantNamedEntities +
                '}';
    }
}
