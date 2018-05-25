package cn.northpark.jeyy.interceptor;

import cn.northpark.jeyy.module.Execution;

/**
 * 和 Filter 类似，InterceptorChain 代表拦截器链。
 * 实现 InterceptorChain 要比实现 FilterChain 简单，
 * 因为 Filter 需要处理 Request、Forward、Include 和 Error 这 4 种请求转发的情况，
 * 而 Interceptor 仅拦截 Request。
 * 当 MVC 框架处理一个请求时，先初始化一个拦截器链，
 * 然后，依次调用链上的每个拦截器。
 * 
 * @author bruce 
 */
public interface InterceptorChain {

    /**
     * Apply next interceptor around the execution of Action.
     * 
     * @param execution Execution to execute.
     * @throws Exception Any exception if error occured.
     */
    void doInterceptor(Execution execution) throws Exception;

}
