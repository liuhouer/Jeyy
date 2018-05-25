package cn.northpark.jeyy.template.velocity;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;

import cn.northpark.jeyy.template.Template;

/**
 * 比 JSP 更加简单且灵活的模板引擎是 Velocity，
 * 它使用更简洁的语法来渲染页面，对页面设计人员更加友好，
 * 并且完全阻止了开发人员试图在页面中编写 Java 代码的可能性。
 * 
 * 配置 Velocity 作为模板引擎 web.xml:
 *	<servlet> 
 *	    <servlet-name>dispatcher</servlet-name> 
 *	    <servlet-class>.....DispatcherServlet</servlet-class> 
 *      <init-param> 
 *          <param-name>template</param-name> 
 *          <param-value>Velocity</param-value> 
 *      </init-param> 
 *  </servlet>
 * 
 * @author bruce 
 */
public class VelocityTemplate implements Template {

    private org.apache.velocity.Template template;
    private String contentType;
    private String encoding;

    public VelocityTemplate(org.apache.velocity.Template template, String contentType, String encoding) {
        this.template = template;
        this.contentType = contentType;
        this.encoding = encoding;
    }

    public void render(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws Exception {
        StringBuilder sb = new StringBuilder(64);
        sb.append(contentType==null ? "text/html" : contentType)
                .append(";charset=")
                .append(encoding==null ? "UTF-8" : encoding);
        response.setContentType(sb.toString());
        response.setCharacterEncoding(encoding==null ? "UTF-8" : encoding);
        // init context:
        Context context = new VelocityContext(model);
        afterContextPrepared(context);
        // render:
        VelocityWriter vw = new VelocityWriter(response.getWriter());
        try {
            template.merge(context, vw);
            vw.flush();
        }
        finally {
            vw.recycle(null);
        }
    }

    /**
     * Let subclass do some initial work after Velocity context prepared.
     * 
     * @param context Velocity context object.
     */
    protected void afterContextPrepared(Context context) {
    }
}
