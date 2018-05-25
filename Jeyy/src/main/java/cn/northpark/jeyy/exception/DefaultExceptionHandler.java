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
    public void  handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
      
        
        if (isAjax(request)) {
    			    String text = ResultGenerator.genErrorResult(ResultCode.SERVER_ERROR.getCode(),ResultCode.SERVER_ERROR.getMessage()+"-->"+e.getMessage()).toString();
    			    StringBuilder sb = new StringBuilder(64);
    		        sb.append("application/json")
    		          .append(";charset=")
    		          .append("UTF-8");
    		        response.setContentType(sb.toString());
    		        PrintWriter pw = response.getWriter();
    		        pw.write(text);
    		        pw.flush();
        } else {
        	
        	    StringBuilder sb = new StringBuilder(64);
		        sb.append("text/html")
		          .append(";charset=")
		          .append("UTF-8");
		        response.setContentType(sb.toString());
    		    PrintWriter pw = response.getWriter();
    	        pw.write("<html><head><title>Exception</title></head><body><h1>星际穿越</h1><div><p><pre style=\"padding:16px; overflow: auto;font-size: 85%;line-height: 1.45;background-color: #f6f8fa;border-radius: 3px;\">");
    	        e.printStackTrace(pw);
    	        pw.write("</pre></p></div></body></html>");
    	        pw.flush();
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
