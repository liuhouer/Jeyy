package cn.northpark.jeyy.guice;

import javax.servlet.ServletContext;

/**
 * 凡是实现ServletContextAware接口的类，都可以取得ServletContext上下文
 * Guice module which implements this interface will automatically get the 
 * ServletContext object in web application.
 * 
 * @author bruce 
 */
public interface ServletContextAware {

    /**
     * Called by GuiceContainerFactory when initialize module.
     * 
     * @param servletContext The ServletContext object.
     */
    void setServletContext(ServletContext servletContext);

}
