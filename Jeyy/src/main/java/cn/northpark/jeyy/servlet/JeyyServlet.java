package cn.northpark.jeyy.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
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

 * 和 Struts 等常见 MVC 框架一样，也需要实现一个前置控制器，通常命名为 DispatcherServlet，
 * 用于接收所有的请求，并作出合适的转发。在 Servlet 规范中，有以下几种常见的 URL 匹配模式：
 * /abc：精确匹配，通常用于映射自定义的 Servlet；
 * *.do：后缀模式匹配，常见的 MVC 框架都采用这种模式；
 * /app/*：前缀模式匹配，这要求 URL 必须以固定前缀开头；
 * /：匹配默认的 Servlet，当一个 URL 没有匹配到任何 Servlet 时，就匹配默认的 Servlet。
 * 一个 Web 应用程序如果没有映射默认的 Servlet，Web 服务器会自动为 Web 应用程序添加一个默认的 Servlet。
 * REST 风格的 URL 一般不含后缀，只能将 DispatcherServlet 映射到“/”，使之变为一个默认的 Servlet，
 * 这样，就可以对任意的 URL 进行处理。
 * 由于无法像 Struts 等传统的 MVC 框架根据后缀直接将一个 URL 映射到一个 Controller，必须依次匹配每个有能力处理 HTTP 请求的 @Mapping 方法。
 *  
 *  由于将 DispatcherServlet 映射为“/”，即默认的 Servlet，
 *  则所有的未匹配成功的 URL 都将由 DispatcherServlet 处理，包括所有静态文件，
 *  因此，当未匹配到任何 Controller 的 @Mapping 方法后，
 *  DispatcherServlet 将试图按 URL 查找对应的静态文件，用 StaticFileHandler 封装
 * 
 * @author bruce 
 */
public class JeyyServlet extends GenericServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Log log = LogFactory.getLog(getClass());
    
    //核心控制器
    private Jeyy top_dispatcher;
    //处理未匹配到的url 当做静态文件处理
    private JeyyStaticResourceServlet jeyyStaticResourceHandler;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        log.info("Init JeyyServlet...");
        this.top_dispatcher = new Jeyy();
        this.top_dispatcher.init(
                new Config() {
                    public String getInitParameter(String name) {
                        return config.getInitParameter(name);
                    }

                    public ServletContext getServletContext() {
                        return config.getServletContext();
                    }
                }
        );
        this.jeyyStaticResourceHandler = new JeyyStaticResourceServlet(config);
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        String method = httpReq.getMethod();
        if ("GET".equals(method) || "POST".equals(method)) {
            if (!top_dispatcher.service(httpReq, httpResp))
                jeyyStaticResourceHandler.handle(httpReq, httpResp);
            return;
        }
        httpResp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    public void destroy() {
        log.info("Destroy JeyyServlet...");
        this.top_dispatcher.destroy();
    }

}
