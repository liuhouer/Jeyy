package cn.northpark.jeyy.interceptor;

import cn.northpark.jeyy.module.Execution;

/**
 * 拦截器和 Servlet 规范中的 Filter 非常类似，
 * 不过 Filter 的作用范围是整个 HttpServletRequest 的处理过程，
 * 而拦截器仅作用于 Controller，不涉及到 View 的渲染，
 * 在大多数情况下，使用拦截器比 Filter 速度要快，尤其是绑定数据库事务时，拦截器能缩短数据库事务开启的时间。
 * 
 * @author bruce 
 */
public interface Interceptor {

    /**
     * Do intercept and invoke chain.doInterceptor() to process next interceptor. 
     * NOTE that process will not continue if chain.doInterceptor() method is not 
     * invoked.
     * 
     * @param execution Execution instance to handle http request.
     * @param chain Interceptor chain.
     * @throws Exception If any exception is thrown, process will not continued.
     */
    void intercept(Execution execution, InterceptorChain chain) throws Exception;

}
