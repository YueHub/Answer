package cn.lcy.answer.util;

public class StringHandle {
	
	/**
	 * 判断一个字符是否是汉字
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
	      return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
	}
	
	/**
	 * 判断字符串中是否含有汉字
	 * @param str
	 * @return
	 */
	public static boolean isIncludeChinese(String str) {
		if (str == null) return false;
		for (char c : str.toCharArray()) {
			if (isChinese(c)) return true;// 有一个中文字符就返回
		}
		return false;
	}
}
