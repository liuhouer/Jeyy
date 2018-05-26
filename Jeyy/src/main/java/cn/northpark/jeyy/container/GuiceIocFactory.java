package cn.northpark.jeyy.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.northpark.jeyy.config.Config;
import cn.northpark.jeyy.config.ConfigException;
import cn.northpark.jeyy.guice.ServletContextAware;
import cn.northpark.jeyy.module.Scavenger;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * 对于 Guice 容器，通过 Injector 实例可以返回所有绑定对象的实例
 * 通过扩展 ContainerFactory，就可以支持更多的 IoC 容器，如 PicoContainer。
 * 出于效率的考虑，缓存所有来自 IoC 的 Controller 实例，无论其在 IoC 中配置为 Singleton 还是 Prototype 类型。
 * 当然，也可以修改代码，每次都从 IoC 容器中重新请求实例。
 * @author bruce 
 */
public class GuiceIocFactory implements IocFactory {

    private Log log = LogFactory.getLog(getClass());

    private Injector injector;

    /** 
	 * 取得注入对象中绑定的所有的实例
     * @see cn.northpark.jeyy.container.IocFactory#findAllBeans()
     */
    public List<Object> findAllBeans() {
        Map<Key<?>, Binding<?>> map = injector.getBindings();
        Set<Key<?>> keys = map.keySet();
        List<Object> list = new ArrayList<Object>(keys.size());
        for (Key<?> key : keys) {
            Object bean = injector.getInstance(key);
            list.add(bean);
        }
        return list;
    }

    /**
     * 初始化 配置文件配置的module 绑定的所有对象
     * @see cn.northpark.jeyy.container.IocFactory#init(cn.northpark.jeyy.config.Config)
     */
    public void init(final Config config) {
        String value = config.getInitParameter("modules");
        if (value==null)
            throw new ConfigException("Config Error:Missing  parameter for  guice  '<modules>'...");
        //获取Module类名的列表
        String[] ss = value.split(",");
        List<Module> moduleList = new ArrayList<Module>(ss.length);
        for (String s : ss) {
        	//初始化Module,类名 -->实例
            Module m = initModule(s, config.getServletContext());
            if (m!=null)
                moduleList.add(m);
        }
        if (moduleList.isEmpty())
            throw new ConfigException("Config Error:module not found...");
        //创建injector注入对象
        this.injector = Guice.createInjector(Stage.PRODUCTION, moduleList);
        //将injactor放到servlet上下文中保存.
        config.getServletContext().setAttribute(Injector.class.getName(), this.injector);
    }

    /**
     * 描述：初始化Module,类名 -->实例
     *
     * @param s
     * @param servletContext
     * @return
     */
    Module initModule(String s, ServletContext servletContext) {
        s = trim(s);
        if (s.length()>0) {
            if (log.isDebugEnabled())
                log.debug("Initializing module '" + s + "'...");
            try {
            	//初始化
                Object o = Class.forName(s).newInstance();
                if (o instanceof Module) {
                	log.info("Initializing module '" + s + "'...");
                	//如果实现Module的类同时实现了ServletContextAware,那么就把servletContext注入到Module中
                    if (o instanceof ServletContextAware) {
                        ((ServletContextAware) o).setServletContext(servletContext);
                    }
                    return (Module) o;
                }
                throw new ConfigException("Config Error:Class '" + s + "' does not implement '" + Module.class.getName() + "'.");
            }
            catch(InstantiationException e) {
                throw new ConfigException("Config Error:Cannot instanciate class '" + s + "'.", e);
            }
            catch(IllegalAccessException e) {
                throw new ConfigException("Config Error:Cannot instanciate class '" + s + "'.", e);
            }
            catch(ClassNotFoundException e) {
                throw new ConfigException("Config Error:Cannot instanciate class '" + s + "'.", e);
            }
        }
        return null;
    }

    String trim(String s) {
        while (s.length()>0) {
            char c = s.charAt(0);
            if (" \t\r\n".indexOf(c)!=(-1))
                s = s.substring(1);
            else
                break;
        }
        while (s.length()>0) {
            char c = s.charAt(s.length()-1);
            if (" \t\r\n".indexOf(c)!=(-1))
                s = s.substring(0, s.length()-1);
            else
                break;
        }
        return s;
    }

    public void destroy() {
        List<Object> beans = findAllBeans();
        for (Object bean : beans) {
        	//如果类实现了Scavenger接口的话,那么就销毁,否则什么也不做,这个算是观察者模式吧??
        	//这里面一个思考就是:如果想强制让某个类做销毁或者初始化等动作,可以定义接口,让类去实现.
            if (bean instanceof Scavenger) {
                ((Scavenger)bean).destroy();
            }
        }
    }

}
