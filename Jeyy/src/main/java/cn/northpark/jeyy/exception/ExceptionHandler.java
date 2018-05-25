package cn.northpark.jeyy.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Central handler for handling all exceptions.
 * 
 * @author bruce 
 */
public interface ExceptionHandler {

    /**
     * Handle exception when exception occurs.
     * 
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @param e The target exception.
     * @throws Exception If exception occurs or re-throw when handling.
     */
//    void handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception;
    
    
    /**
     * Handle exception when exception occurs.
     * 
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @param e The target exception.
     * @throws Exception If exception occurs or re-throw when handling.
     */
    void handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception;

}
