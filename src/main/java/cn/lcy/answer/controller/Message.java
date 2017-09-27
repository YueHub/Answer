package cn.lcy.answer.controller;

public class Message {

	/**
	 * RESTful 暂时仅作为管道 
	 */
    private final String result;

    public Message(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

}
