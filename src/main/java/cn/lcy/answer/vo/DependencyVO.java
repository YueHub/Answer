package cn.lcy.answer.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public class DependencyVO implements java.io.Serializable {

    /**
     * default serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 依存关系的弧线集合
     */
    private List<DependencyNode> dependencyNodes = new ArrayList<DependencyNode>();

    public List<DependencyNode> getDependencyNodes() {
        return dependencyNodes;
    }

    public void setDependencyNodes(List<DependencyNode> dependencyNodes) {
        this.dependencyNodes = dependencyNodes;
    }

    @Override
    public String toString() {
        return "DependencyVO{" +
                "dependencyNodes=" + dependencyNodes +
                '}';
    }
}
