package cn.northpark.jeyy.exception;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Default exception handler which just print the exception trace on web page.
 * 
 * @author bruce 
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    /**
     * Handle exception that print stack trace on HTML page.
     */
    public Object  handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
      
        
        if (isAjax(request)) {
    			return ResultGenerator.genErrorResult(ResultCode.SERVER_ERROR.getCode(),ResultCode.SERVER_ERROR.getMessage()+"-->"+e.getMessage());
    	} else {
    		    PrintWriter pw = response.getWriter();
    	        pw.write("<html><head><title>Exception</title></head><body><pre>");
    	        e.printStackTrace(pw);
    	        pw.write("</pre></body></html>");
    	        pw.flush();
    	        return null;
    	}
    }
    
    
    
    /**
	 * 
	 * @Description: 判断是否是ajax请求
	 * Copyright: Copyright (c) 2018
	 * 
	 * @author bruce
	 * @date 2018年5月2日
	 */
	public static boolean isAjax(HttpServletRequest httpRequest){
		return  (httpRequest.getHeader("X-Requested-With") != null  
					&& "XMLHttpRequest"
						.equals( httpRequest.getHeader("X-Requested-With").toString()) );
	}

}
