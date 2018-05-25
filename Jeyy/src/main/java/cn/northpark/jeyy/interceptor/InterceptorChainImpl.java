package cn.northpark.jeyy.interceptor;

import cn.northpark.jeyy.module.Execution;

/**
 * 实现 InterceptorChain 要比实现 FilterChain 简单，
 * 因为 Filter 需要处理 Request、Forward、Include 和 Error 这 4 种请求转发的情况，
 * 而 Interceptor 仅拦截 Request。
 * 当 MVC 框架处理一个请求时，先初始化一个拦截器链，
 * 然后，依次调用链上的每个拦截器。
 * 
 * @author bruce 
 */
public class InterceptorChainImpl implements InterceptorChain {

    private final Interceptor[] interceptors;
    private int index = 0;
    private Object result = null;

    public InterceptorChainImpl(Interceptor[] interceptors) {
        this.interceptors = interceptors;
    }

    public Object getResult() {
        return result;
    }

    public void doInterceptor(Execution execution) throws Exception {
        if(index==interceptors.length)
            result = execution.execute();
        else {
            // must update index first, otherwise will cause stack overflow:
            index++;
            interceptors[index-1].intercept(execution, this);
        }
    }
}
