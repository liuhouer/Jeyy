package cn.northpark.jeyy.renderer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Renderer ：抽象类,负责渲染动作,至于渲染职责,是template的责任.
 * 扩展 Renderer 还可以处理更多的格式，例如，向浏览器返回 JavaScript 代码等。
 * 
 * @author bruce 
 */
public abstract class Renderer {

    protected String contentType;

    /**
     * Get response content type.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set response content type, for example, "text/xml". The default content 
     * type is "text/html". DO NOT add "charset=xxx".
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Render the output of http response.
     * 
     * @param context ServletContext object.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @throws Exception If any Exception occur.
     */
    public abstract void render(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
