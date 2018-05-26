package cn.northpark.jeyy.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.northpark.jeyy.Jeyy;
import cn.northpark.jeyy.config.Config;

/**
 * 对于请求转发，除了使用 DispatcherServlet 外，
 * 还可以使用 Filter 来拦截所有请求，并直接在 Filter 内实现请求转发和处理。
 * 使用 Filter 的一个好处是如果 URL 没有被任何 Controller 的映射方法匹配到，
 * 则可以简单地调用 FilterChain.doFilter() 将 HTTP 请求传递给下一个 Filter，
 * 这样，就不必自己处理静态文件，而由 Web 服务器提供的默认 Servlet 处理，效率更高。
 * 和 DispatcherServlet 类似，编写一个 DispatcherFilter 作为前置处理器，负责转发请求
 * 
 * 
 * 如果用 Filter 代替 Servlet，则需要过滤“/*”，在 web.xml 中添加声明 
 *	<filter> 
 *	    <filter-name>dispatcher</servlet-name> 
 *	    <filter-class>x.x.x.x.DispatcherFilter</servlet-class> 
 *	</filter> 
 *	<filter-mapping> 
 *	    <filter-name>dispatcher</servlet-name> 
 *	    <url-pattern>/*</url-pattern> 
 *	</filter-mapping>
 * 
 *  @author bruce 
 */
public class JeyyFilter implements Filter {

    private final Log log = LogFactory.getLog(getClass());

    /**
     * 最核心的控制器
     */
    private Jeyy top_dispatcher;

    public void init(final FilterConfig filterConfig) throws ServletException {
        log.info("Init JeyyFilter...");
        this.top_dispatcher = new Jeyy();
        this.top_dispatcher.init(
                new Config() {
                    public String getInitParameter(String name) {
                        return filterConfig.getInitParameter(name);
                    }

                    public ServletContext getServletContext() {
                        return filterConfig.getServletContext();
                    }
                }
        );
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        String method = httpReq.getMethod();
        if ("GET".equals(method) || "POST".equals(method)) {
            if (!top_dispatcher.service(httpReq, httpResp))
                chain.doFilter(req, resp);
            return;
        }
        httpResp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    public void destroy() {
        log.info("Destroy JeyyFilter...");
        this.top_dispatcher.destroy();
    }

}
