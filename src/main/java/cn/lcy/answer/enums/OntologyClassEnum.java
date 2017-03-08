package cn.lcy.answer.enums;

/**
 * 
 * @author NarutoKu
 *
 */
public enum OntologyClassEnum {
	// 人
	CHARACTER(-1, "拟人"),
	// 电影
	MOVIE(-2, "电影"),
	// 音乐
	MUSIC(-3, "音乐"),
	// 动画
	ANIMATION(-4, "动画"),
	// 漫画
	CARICATURE(-5, "漫画"),
	// 地区
	AREA(-6, "地区"),
	// 院校
	ACADEMY(-7, "院校"),
	// 公司
	COMPANY(-8, "公司"),
	// 其他
	OTHERS(-9, "其它");
	
	private int index;
	private String name;
	
	private OntologyClassEnum(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
