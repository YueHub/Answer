package cn.lcy.answer.log;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInitServlet extends HttpServlet{
	
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		String root = config.getServletContext().getRealPath("/");
		String log4jLocation = config.getInitParameter("log4jLocation");
		System.setProperty("webRoot", root);
		if(!log4jLocation.equals("")) {
			PropertyConfigurator.configure(root+log4jLocation);
		}
	}
}
