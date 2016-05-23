package cn.lcy.answer.naction;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;

import cn.lcy.answer.log.Log;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {

	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 以JSON格式写回到前端
	 * @param object
	 */
	public void writeJson(Object object) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
			ServletActionContext.getResponse().getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化业务日志
	 * @param log
	 */
	public void initLog(Log log) {
		// 客户端的地址
		log.setIpAddress(ServletActionContext.getRequest().getRemoteAddr());
		// 客户端信息 判断客户端浏览器类型
		log.setClient(ServletActionContext.getRequest().getHeader("User-Agent"));
		// 请求使用的协议
		log.setProtocol(ServletActionContext.getRequest().getProtocol());
		// 请求的方法
		log.setRequestType(ServletActionContext.getRequest().getMethod());
		// 请求URL地址
		log.setRequestURL(ServletActionContext.getRequest().getRequestURI());
	}	
}