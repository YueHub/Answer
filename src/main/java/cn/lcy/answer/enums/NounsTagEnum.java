package cn.lcy.answer.enums;

public enum NounsTagEnum {
	// 常用名词
	n,
	// 人名
	nr,
	// 职务职称
    nnt,
    // 其他专名
    nz,
    // 星爷
    nh;
	
	/**
	 * 判断是否为本枚举中的元素
	 * @param element
	 * @return
	 */
	public static boolean isIncludeEnumElement(String element) {
		for(Enum<?> ele : NounsTagEnum.values()) {
			if(ele.toString() == element) {
				return true;
			}
		}
		return false;
	}
    
}
