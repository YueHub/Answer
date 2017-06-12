package cn.lcy.answer.vo;

public class SemanticGraphNodeVO implements java.io.Serializable {
	
	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 连接线顺序 
	 */
	private int ID;

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
	private double size;

	private int alpha;
	

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}
	
	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
}