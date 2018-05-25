package cn.northpark.jeyy.template;

import org.apache.commons.logging.LogFactory;

import cn.northpark.jeyy.config.Config;

/**
 * 集成模板引擎：
 * 作为示例，返回一个“<h1>Hello, world!</h1>”作为 HTML 页面非常容易。
 * 然而，实际应用的页面通常是极其复杂的，需要一个模板引擎来渲染出 HTML。
 * 可以把 JSP 看作是一种模板，只要不在 JSP 页面中编写复杂的 Java 代码。
 * 目标是实现对 JSP 和 Velocity 这两种模板的支持。
 * 
 * 和集成 IoC 框架类似， 需要解耦 MVC 与模板系统，
 * 因此，TemplateFactory 用于初始化模板引擎，并返回 Template 模板对象。
 * 
 * @author bruce 
 */
public abstract class TemplateFactory {

    private static TemplateFactory instance;

    /**
     * Init TemplateFactory.
     */
    public abstract void init(Config config);

    /**
     * Load Template from path.
     * 
     * @param path Template file path, relative with webapp's root path.
     * @return Template instance.
     * @throws Exception If load failed, e.g., file not found.
     */
    public abstract Template loadTemplate(String path) throws Exception;
    
    
    //=====================================GET+SET============================================
    /**
     * Set the static TemplateFactory instance by webwind framework.
     */
    public static void setTemplateFactory(TemplateFactory templateFactory) {
        instance = templateFactory;
        LogFactory.getLog(TemplateFactory.class).info("TemplateFactory is set to: " + instance);
    }

    /**
     * Get the static TemplateFactory instance.
     */
    public static TemplateFactory getTemplateFactory() {
        return instance;
    }
   //=====================================GET+SET============================================
}
