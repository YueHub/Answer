package cn.lcy.answer.vo;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public class SemanticGraphNodeVO implements java.io.Serializable {

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

    private Integer alpha;


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

    @Override
    public String toString() {
        return "SemanticGraphNodeVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shape='" + shape + '\'' +
                ", color='" + color + '\'' +
                ", size=" + size +
                ", alpha=" + alpha +
                '}';
    }
}
