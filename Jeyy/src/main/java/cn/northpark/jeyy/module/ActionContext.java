package cn.northpark.jeyy.module;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 访问 Request 和 Response 对象:
 * 如何在 @Mapping 方法中访问 Servlet 对象？
 * 如 HttpServletRequest，HttpServletResponse，HttpSession 和 ServletContext。
 * ThreadLocal 是一个最简单有效的解决方案。
 * 编写一个 ActionContext，通过 ThreadLocal 来封装对 Request 等对象的访问。
 * 
 * 
 * 在 Dispatcher 的 handleExecution() 方法中，初始化 ActionContext，并在 finally 中移除所有已绑定变量
 * 这样，在 @Mapping 方法内部，可以随时获得需要的 Request、Response、 Session 和 ServletContext 对象。
 * @author bruce 
 */
public final class ActionContext {

    private static final ThreadLocal<ActionContext> actionContextThreadLocal = new ThreadLocal<ActionContext>();

    private ServletContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * Return the ServletContext of current web application.
     */
    public ServletContext getServletContext() {
        return context;
    }

    /**
     * Return current request object.
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Return current response object.
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Return current session object.
     */
    public HttpSession getHttpSession() {
        return request.getSession();
    }

    /**
     * Get current ActionContext object.
     */
    public static ActionContext getActionContext() {
        return actionContextThreadLocal.get();
    }

    public static void setActionContext(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
        ActionContext ctx = new ActionContext();
        ctx.context = context;
        ctx.request = request;
        ctx.response = response;
        actionContextThreadLocal.set(ctx);
    }

    public static void removeActionContext() {
        actionContextThreadLocal.remove();
    }
}
