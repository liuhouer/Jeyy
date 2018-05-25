package cn.northpark.jeyy.module;

import java.lang.reflect.Method;

/**
 * 负责请求转发的 Dispatcher 通过关联 UrlMatcher 与 Action，就可以匹配到合适的 URL，并转发给相应的 Action
 * 
 * @author bruce 
 */
public class Action {

    /**
     * Object instance.
     */
    public final Object instance;

    /**
     * Method instance.
     */
    public final Method method;

    /**
     * Method's arguments' types.
     */
    public final Class<?>[] arguments;

    public Action(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.arguments = method.getParameterTypes();
    }

}
