package cn.lcy.answer.log;

/**
 * @author YueHub <lcy.dev@foxmail.com>
 * @github https://github.com/YueHub
 */
public class UserOperationLog extends Log{

	/**
	 * 操作代码 相当于谓语
	 */
	private int operationCode;
	
	/**
	 * 操作对象 相当于宾语
	 */
	private int operationObject;
	
	public int getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(int operationCode) {
		this.operationCode = operationCode;
	}

	public int getOperationObject() {
		return operationObject;
	}

	public void setOperationObject(int operationObject) {
		this.operationObject = operationObject;
	}
}