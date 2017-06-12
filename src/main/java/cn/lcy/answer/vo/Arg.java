package cn.lcy.answer.vo;

public class Arg implements java.io.Serializable {
	
	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private int length;
	
	private String type;
	
	private int beg;
	
	private int end;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getBeg() {
		return beg;
	}

	public void setBeg(int beg) {
		this.beg = beg;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}