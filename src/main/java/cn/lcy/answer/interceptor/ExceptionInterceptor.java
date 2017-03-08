package cn.lcy.answer.interceptor;

import org.apache.log4j.Logger;

import cn.lcy.answer.exception.OntologyQueryException;
import cn.lcy.answer.exception.SemanticGraphException;
import cn.lcy.answer.log.SystemExceptionLog;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;

public class ExceptionInterceptor extends AbstractInterceptor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*private static Logger businessExceptionLogger = Logger.getLogger("BusinessExceptionLog");*/
	
	private static Logger systemExceptionLogger = Logger.getLogger("SystemExceptionLog");
	
	private static Logger suspiciousOperationLogger = Logger.getLogger("SuspiciousOperationLog");

	@Override
    public String intercept(ActionInvocation ai) throws Exception {
        String result = null;
        try {
            result = ai.invoke();
        } catch (NoSuchMethodException e) {
        	suspiciousOperationLogger.warn(ai.getInvocationContext(), e);
        	// 将错误信息放入堆栈中
        	ai.getStack().push(new ExceptionHolder(e));
        	return "default_redirect";
        } catch (SemanticGraphException e) {
            ai.getStack().push(new ExceptionHolder(e));
            result = "semantic_graph_exception";
        } catch (OntologyQueryException e) {
        	ai.getStack().push(new ExceptionHolder(e));
        	result = "ontoloty_query_exception";
        } catch (Exception e) {
            //logger.error(ai.toString(), e);
        	SystemExceptionLog systemExceptionLog = new SystemExceptionLog();
        	systemExceptionLogger.error(ai.toString() + systemExceptionLog.toString(), e);
            ai.getStack().push(new ExceptionHolder(e));
            result = "system_error";
        }
        return result;
    }

}