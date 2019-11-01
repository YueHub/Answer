package cn.lcy.answer.util;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public class StringHandle {

    /**
     * 判断一个字符是否是汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        // 根据字节码判断
        return c >= 0x4E00 && c <= 0x9FA5;
    }

    /**
     * 判断字符串中是否含有汉字
     *
     * @param str
     * @return
     */
    public static boolean isIncludeChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            /* 有一个中文字符就返回 */
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
}
