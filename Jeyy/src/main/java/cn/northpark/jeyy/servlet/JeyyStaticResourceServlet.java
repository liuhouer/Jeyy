package cn.northpark.jeyy.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  由于将 DispatcherServlet 映射为“/”，即默认的 Servlet，
 *  则所有的未匹配成功的 URL 都将由 DispatcherServlet 处理，包括所有静态文件，
 *  因此，当未匹配到任何 Controller 的 @Mapping 方法后，
 *  DispatcherServlet 将试图按 URL 查找对应的静态文件，用 StaticFileHandler[JeyyStaticResourceServlet] 封装
 * 
 * @author bruce 
 */
public class JeyyStaticResourceServlet {

    private static final String MIME_OCTET_STREAM = "application/octet-stream";
    private static final int MAX_BUFFER_SIZE = 4096;

    private final Log log = LogFactory.getLog(getClass());
    private final ServletContext servletContext;

    private long expires = 0;
    private String maxAge = "";

    public JeyyStaticResourceServlet(ServletConfig config) throws ServletException {
        this.servletContext = config.getServletContext();
        String expiresValue = config.getInitParameter("expires");
        if (expiresValue!=null) {
            int n = Integer.parseInt(expiresValue);
            if (n>0) {
                this.expires = n * 1000L;
                this.maxAge = "max-age=" + n;
                log.info("Static file's cache time is set to " + n + " seconds.");
            }
            else if (n<0) {
                this.expires = (-1);
                log.info("Static file is set to no cache.");
            }
        }
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();
        String path = request.getContextPath();
        url = url.substring(path.length());
      //处理静态文件时要过滤 /WEB-INF/ 目录，否则将造成安全漏洞。
      //WEB-INF内部的内容不允许访问
        if (url.toUpperCase().startsWith("/WEB-INF/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
      //处理 /xxx.html?yy=zz这种资源
        int n = url.indexOf('?');
        if (n!=(-1))
            url = url.substring(0, n);
      //处理 /xxx.html#1这种资源
        n = url.indexOf('#');
        if (n!=(-1))
            url = url.substring(0, n);
      //获取资源名
        File f = new File(servletContext.getRealPath(url));
        
      //如果找不到资源, 则返回404
        if (! f.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
      //设置ETag
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        long lastModified = f.lastModified();
        if (ifModifiedSince!=(-1) && ifModifiedSince>=lastModified) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        response.setDateHeader("Last-Modified", lastModified);
        response.setContentLength((int)f.length());
        // set cache:
        if (expires<0) {
            response.setHeader("Cache-Control", "no-cache");
        }
        else if (expires>0) {
            response.setHeader("Cache-Control", maxAge);
            response.setDateHeader("Expires", System.currentTimeMillis() + expires);
        }
        // should download?
//        String name = request.getParameter("_download");
//        if (name!=null) {
//            resp.setContentType(MIME_OCTET_STREAM);
//            resp.setHeader("Content-disposition", "attachment; filename=" + name);
//        }
        response.setContentType(getMimeType(f));
      //把文件的内容写入到response的流中
        sendFile(f, response.getOutputStream());
    }

    String getMimeType(File f) {
        String mime = servletContext.getMimeType(f.getName());
        return mime==null ? MIME_OCTET_STREAM : mime;
    }

    void sendFile(File file, OutputStream output) throws IOException {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            for (;;) {
                int n = input.read(buffer);
                if (n==(-1))
                    break;
                output.write(buffer, 0, n);
            }
            output.flush();
        }
        finally {
            if (input!=null) {
                try {
                    input.close();
                }
                catch (IOException e) {}
            }
        }
    }
}
