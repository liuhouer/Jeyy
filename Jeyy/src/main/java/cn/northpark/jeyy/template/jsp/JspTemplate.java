package cn.northpark.jeyy.template.jsp;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.northpark.jeyy.template.Template;

/**
 * JspTemplate 用于渲染页面，只需要传入 JSP 的路径，
 * 将 Model 绑定到 HttpServletRequest，
 * 就可以调用 Servlet 规范的 forward 方法将请求转发给指定的 JSP 页面并渲染。
 * 
 * @author bruce 
 */
public class JspTemplate implements Template {

    private String path;

    public JspTemplate(String path) {
        this.path = path;
    }

    /**
     * Execute the JSP with given model.
     */
    public void render(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws Exception {
        Set<String> keys = model.keySet();
        for (String key : keys) {
            request.setAttribute(key, model.get(key));
        }
        request.getRequestDispatcher(path).forward(request, response);
    }

}
