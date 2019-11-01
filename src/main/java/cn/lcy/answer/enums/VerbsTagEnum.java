package cn.lcy.answer.enums;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public enum VerbsTagEnum {
    // 常用动词
    v,
    // 动词是
    vshi,
    // 名动词
    vn,
    vi;
    // 动词有
    //vyou,

    /**
     * 判断是否为本枚举中的元素
     *
     * @param element
     * @return
     */
    public static boolean isIncludeEnumElement(String element) {
        for (Enum<?> ele : VerbsTagEnum.values()) {
            if (ele.toString() == element) {
                return true;
            }
        }
        return false;
    }
}