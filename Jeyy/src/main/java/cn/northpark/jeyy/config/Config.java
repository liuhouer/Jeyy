package cn.northpark.jeyy.config;

import javax.servlet.ServletContext;

/**
 * 
 * 用来读取项目的servlet配置和初始化的参数配置
 * @author bruce 
 */
public interface Config {

    /**
     * Get ServletContext object.
     */
    public ServletContext getServletContext();

    /**
     * Get init parameter value by name.
     */
    public String getInitParameter(String name);

}
