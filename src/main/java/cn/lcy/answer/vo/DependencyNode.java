package cn.lcy.answer.vo;

public class DependencyNode implements java.io.Serializable {
	
	/**
	 *  default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 开始结点的ID
	 */
	private int id;
	
	/**
	 * 开始结点的文本
	 */
	private String cont;
	
	/**
	 * 开始结点的词性
	 */
	private String pos;
	
	/**
	 * 未知?  暂时设置为0
	 */
	private String ne;
	
	/**
	 * 终止结点
	 */
	private int parent;
	
	/**
	 * 关系
	 */
	private String relate;
	
	/**
	 * TODO 支持语义依存分析
	 */
	private int semparent;
	
	/**
	 * TODO 支持语义依存分析
	 */
	private String semrelate;
	
	private Arg arg;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getNe() {
		return ne;
	}

	public void setNe(String ne) {
		this.ne = ne;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getRelate() {
		return relate;
	}

	public void setRelate(String relate) {
		this.relate = relate;
	}

	public int getSemparent() {
		return semparent;
	}

	public void setSemparent(int semparent) {
		this.semparent = semparent;
	}

	public String getSemrelate() {
		return semrelate;
	}

	public void setSemrelate(String semrelate) {
		this.semrelate = semrelate;
	}
	
	public Arg getArg() {
		return arg;
	}

	public void setArg(Arg arg) {
		this.arg = arg;
	}
}