package cn.northpark.jeyy.module;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 根据 URL 找到匹配的 Action 后，就可以构造一个 Execution【执行】 对象，
 * 并根据方法签名将 URL 中的 String 转换为合适的方法参数类型，准备好全部参数
 *
 * 为了最大限度地增加灵活性， 并不强制要求 URL 的处理方法返回某一种类型。 设计支持以下返回值：
 * String：当返回一个 String 时，自动将其作为 HTML 写入 HttpServletResponse；
 * void：当返回 void 时，不做任何操作；
 * Renderer：当返回 Renderer 对象时，将调用 Renderer 对象的 render 方法渲染 HTML 页面。
 * @author bruce 
 */
public class Execution {

    public final HttpServletRequest request;
    public final HttpServletResponse response;
    private final Action action;
    private final Object[] args;

    public Execution(HttpServletRequest request, HttpServletResponse response, Action action, Object[] args) {
        this.request = request;
        this.response = response;
        this.action = action;
        this.args = args;
    }

    /**
     * 反射构造执行对象：
     * 调用 execute() 方法就可以执行目标方法，
     * 并返回一个结果。请注意，当通过反射调用方法失败时，我们通过查找 InvocationTargetException 的根异常并将其抛出，
     * 这样，客户端就能捕获正确的原始异常。
     * @return
     * @throws Exception
     */
    public Object execute() throws Exception {
        try {
            return action.method.invoke(action.instance, args);
        }
        catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t!=null && t instanceof Exception)
                throw (Exception) t;
            throw e;
        }
    }
}
