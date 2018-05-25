package cn.northpark.jeyy.util;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.northpark.jeyy.config.ConfigException;
import cn.northpark.jeyy.container.IocFactory;
import cn.northpark.jeyy.template.TemplateFactory;

/**
 * JeyyUtils for create ContainerFactory, TemplateFactory, etc.
 * 
 * @author bruce 
 */
public class JeyyUtils {

    static final Log log = LogFactory.getLog(JeyyUtils.class);

    /**
     * 描述：创建容器工厂
     *
     * @param name
     * @return
     * @throws ServletException
     */
    public static IocFactory createIocFactory(String name) throws ServletException {
        IocFactory cf = initIocFactory(name);
        if (cf==null)
            cf = initIocFactory(IocFactory.class.getPackage().getName() + "." + name + IocFactory.class.getSimpleName());
        if (cf==null)
            throw new ConfigException("Cannot create container factory by name '" + name + "'.");
        return cf;
    }

    /**
     * 描述：初始化容器工厂
     *
     * @param clazz
     * @return
     */
    static IocFactory initIocFactory(String clazz) {
        try {
            Object obj = Class.forName(clazz).newInstance();
            if (obj instanceof IocFactory)
                return (IocFactory) obj;
        }
        catch (Exception e) { }
        return null;
    }

    /**
     * 描述：创建模板工厂
     *
     * @param name
     * @return
     */
    public static TemplateFactory createTemplateFactory(String name) {
        TemplateFactory tf = initTemplateFactory(name);
        if (tf==null)
            tf = initTemplateFactory(TemplateFactory.class.getPackage().getName()+"."+name.toLowerCase() + "." + name + TemplateFactory.class.getSimpleName());
        if (tf==null) {
            log.warn("Cannot init template factory '" + name + "'.");
            throw new ConfigException("Cannot init template factory '" + name + "'.");
        }
        return tf;
    }

    /**
     * 描述：初始化模板工厂
     *
     * @param clazz
     * @return
     */
    static TemplateFactory initTemplateFactory(String clazz) {
        try {
            Object obj = Class.forName(clazz).newInstance();
            if (obj instanceof TemplateFactory)
                return (TemplateFactory) obj;
        }
        catch(Exception e) { }
        return null;
    }

}
