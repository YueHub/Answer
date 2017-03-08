package cn.lcy.answer.demo;

import java.net.URLDecoder;

public class DemoURLDecoder {
	@SuppressWarnings("deprecation")
	public static void main(String args[]) {
		String url = "http://baike.baidu.com/item/%E5%91%A8%E6%98%9F%E9%A9%B0/169917";
		System.out.println("解码后："+URLDecoder.decode(url));
	}

}
