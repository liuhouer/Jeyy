package cn.northpark.jeyy;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.northpark.jeyy.annotation.InterceptorOrder;
import cn.northpark.jeyy.annotation.MapPath;
import cn.northpark.jeyy.config.Config;
import cn.northpark.jeyy.config.ConfigException;
import cn.northpark.jeyy.container.IocFactory;
import cn.northpark.jeyy.exception.DefaultExceptionHandler;
import cn.northpark.jeyy.exception.ExceptionHandler;
import cn.northpark.jeyy.interceptor.Interceptor;
import cn.northpark.jeyy.interceptor.InterceptorChainImpl;
import cn.northpark.jeyy.module.Action;
import cn.northpark.jeyy.module.JeyyContext;
import cn.northpark.jeyy.module.Execution;
import cn.northpark.jeyy.renderer.JavaScriptRenderer;
import cn.northpark.jeyy.renderer.Renderer;
import cn.northpark.jeyy.renderer.TextRenderer;
import cn.northpark.jeyy.servlet.MultipartHttpServletRequest;
import cn.northpark.jeyy.template.TemplateFactory;
import cn.northpark.jeyy.template.jsp.JspTemplateFactory;
import cn.northpark.jeyy.util.JeyyUtils;
import cn.northpark.jeyy.util.PathMatcher;
import cn.northpark.jeyy.util.converter.ConverterFactory;

/**
 * 
 *	@since v0.0.1 核心控制器定义
 *	@category DispatcherServlet和DispatcherFilter可以二选一
 *	@see DispatcherServlet用于扫描Controller，扫不到的需要调用StaticFileHandler来当做静态文件处理
 *	     DispatcherFilter 可以扫描所有方法，找不到的可以放到filterChain去处理
 *  @author bruce 
 */
public class Jeyy {

    private final Log log = LogFactory.getLog(getClass());

    /**
     * 上下文
     */
    private ServletContext servletContext;
    /**
     * 容器工厂
     */
    private IocFactory iocFactory;
    /**
     * 文件上传
     */
    private boolean multipartSupport = false;
    /**
     * 文件上传限制
     */
    private long maxFileSize = 10L * 1024L * 1024L; // default to 10M.
    /**
     * 路径匹配
     */
    private PathMatcher[] pathMatchers = null;
    /**
     * 路径匹配指向具体的action
     */
    private Map<PathMatcher, Action> pathMap = new HashMap<PathMatcher, Action>();
    /**
     * 转换工厂
     */
    private ConverterFactory converterFactory = new ConverterFactory();
    /**
     * 拦截器
     */
    private Interceptor[] interceptors = null;
    /**
     * 异常处理
     */
    private ExceptionHandler exceptionHandler = null;

    public void init(Config config) throws ServletException {
        log.info("Init your Config's Dispatcher...");
        this.servletContext = config.getServletContext();
        try {
            initEveryThing(config);
        }
        catch (ServletException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ServletException("Dispatcher init failed.", e);
        }
    }

    void initEveryThing(Config config) throws Exception {
    	//初始化文件上传, 如果不存在这个类, 则会抛异常, 这样的话, 系统就不支持multipartSupport.
    	//这种处理手法非常适合支持组件
        try {
            Class.forName("org.apache.commons.fileupload.servlet.ServletFileUpload");
            this.multipartSupport = true;
            log.info("add CommonsFileUpload 2 handle file upload.. sth.. http request.");
            String maxFileSize = config.getInitParameter("maxFileSize");
            if (maxFileSize!=null) {
                try {
                    long n = Long.parseLong(maxFileSize);
                    if (n<=0)
                        throw new NumberFormatException();
                    this.maxFileSize = n;
                }
                catch (NumberFormatException e) {
                    log.warn("Invalid parameter <maxFileSize> value '" + maxFileSize + "', using default.");
                }
            }
        }
        catch (ClassNotFoundException e) {
            log.info("CommonsFileUpload not found. Multipart http request can not be handled. You can add repository 2 build.");
        }

        //获取container的名字
        String iocName = config.getInitParameter("container");
        if (iocName==null)
            throw new ConfigException("Config Error: missing init parameter <container>...");
        //创建一个iocFactory
        this.iocFactory = JeyyUtils.createIocFactory(iocName);
        //初始化iocFactory, 必须保证, 这个类是单例, 否则的话, iocFactory 会有多份.
        this.iocFactory.init(config);
        List<Object> beans = this.iocFactory.findAllBeans();
        //初始化所有的组件
        initComponents(beans);

        //初始化模板
        initTemplateFactory(config);
    }

    /**
     * 描述：初始化模板引擎
     *
     * @param config
     */
    void initTemplateFactory(Config config) {
        String name = config.getInitParameter("template");
        if (name==null) {
            name = JspTemplateFactory.class.getName();
            log.info("No template factory specified. Default to '" + name + "'.");
        }
        TemplateFactory tf = JeyyUtils.createTemplateFactory(name);
        tf.init(config);
        log.info("Template factory '" + tf.getClass().getName() + "' init ok.");
        TemplateFactory.setTemplateFactory(tf);
    }

    /**
     * 描述：初始化所有组件
     *
     * @param beans
     */
    void initComponents(List<Object> beans) {
    	//这里的beans, 就是所有带有@PathMap方法的类(具体的Action)
        List<Interceptor> intList = new ArrayList<Interceptor>();
        for (Object bean : beans) {
        	//如果是Interceptor的话, 则加入到Interceptor队列中
            if (bean instanceof Interceptor)
                intList.add((Interceptor)bean);
            if (this.exceptionHandler==null && bean instanceof ExceptionHandler)
                this.exceptionHandler = (ExceptionHandler) bean;
            //找出action类的所有标注方法放到Map中
            putAnnotionMehtod2Map(bean);
        }
      //定义一个处理异常的handler, 这个是非常好的. 可以专门一个异常模板去渲染
        if (this.exceptionHandler==null)
            this.exceptionHandler = new DefaultExceptionHandler();
        this.interceptors = intList.toArray(new Interceptor[intList.size()]);
      //通过InterceptorOrder标记, 来排序.
      //一个链式的处理, 必须要有序
        Arrays.sort(
                this.interceptors,
                new Comparator<Interceptor>() {
                    public int compare(Interceptor i1, Interceptor i2) {
                        InterceptorOrder o1 = i1.getClass().getAnnotation(InterceptorOrder.class);
                        InterceptorOrder o2 = i2.getClass().getAnnotation(InterceptorOrder.class);
                        int n1 = o1==null ? Integer.MAX_VALUE : o1.value();
                        int n2 = o2==null ? Integer.MAX_VALUE : o2.value();
                        if (n1==n2)
                            return i1.getClass().getName().compareTo(i2.getClass().getName());
                        return n1<n2 ? (-1) : 1;
                    }
                }
        );
        this.pathMatchers = pathMap.keySet().toArray(new PathMatcher[pathMap.size()]);
        //对urlMather按url排序
        Arrays.sort(
                this.pathMatchers,
                new Comparator<PathMatcher>() {
                    public int compare(PathMatcher o1, PathMatcher o2) {
                        String u1 = o1.url;
                        String u2 = o2.url;
                        int n = u1.compareTo(u2);
                        if (n==0)
                            throw new ConfigException("Config Error: Cannot mapping one url '" + u1 + "' to more than one action method.");
                        return n;
                    }
                }
        );
    }

    //找出action类的所有标注方法放到Map中
    void putAnnotionMehtod2Map(Object bean) {
        Class<?> clazz = bean.getClass();
        Method[] ms = clazz.getMethods();
        for (Method m : ms) {
            if (isActionMethod(m)) {
                MapPath mappath = m.getAnnotation(MapPath.class);
                //获取被标记的路径
                String path = mappath.value();
                PathMatcher matcher = new PathMatcher(path);
                if (matcher.getArgumentCount()!=m.getParameterTypes().length) {
                    logWarnMethod(m, "Arguments in Path '" + path + "' does not match the arguments of method.");
                    continue;
                }
                log.info("Mapping Path '" + path + "' to method '" + m.toGenericString() + "'.");
                pathMap.put(matcher, new Action(bean, m));
            }
        }
    }

    //判断一个方法是不是被标记的
    boolean isActionMethod(Method m) {
        MapPath mappath = m.getAnnotation(MapPath.class);
        if (mappath==null)
            return false;
        if (mappath.value().length()==0) {
            logWarnMethod(m, "Url mappath cannot be empty.");
            return false;
        }
        if (Modifier.isStatic(m.getModifiers())) {
            logWarnMethod(m, "method is static.");
            return false;
        }
        Class<?>[] argTypes = m.getParameterTypes();
        for (Class<?> argType : argTypes) {
            if (!converterFactory.canConvert(argType)) {
                logWarnMethod(m, "unsupported parameter '" + argType.getName() + "'.");
                return false;
            }
        }
        
	    //判断方法的返回类型: 一共有三种类型的处理方法: void型, String型以及Render型
	    //所有其他的返回类型都不支持
        Class<?> retType = m.getReturnType();
        if (retType.equals(void.class)
                || retType.equals(String.class)
                || Renderer.class.isAssignableFrom(retType)
        )
        return true;
        logWarnMethod(m, "unsupported return type '" + retType.getName() + "'.");
        return false;
    }

    // log warning message of invalid action method:
    void logWarnMethod(Method m, String string) {
        log.warn("Invalid Action method '" + m.toGenericString() + "': " + string);
    }

    public boolean service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	//得到请求的URI, 比如/article/200910/
    	String url = req.getRequestURI();
    	//得到项目的名字, 比如http://localhost/alexlog/article/200910/ ==> alexlog
        String path = req.getContextPath();
        //获取真实请求URI
        if (path.length()>0)
            url = url.substring(path.length());
        // set default character encoding to "utf-8" if encoding is not set:
        if (req.getCharacterEncoding()==null)
            req.setCharacterEncoding("UTF-8");
        if (log.isDebugEnabled())
            log.debug("Handle for URL: " + url);
        Execution execution = null;
        //遍历所有的urlMather, 查找符合的处理, 只找第一个, 因为不可能有多个, 这不是filterchain
        for (PathMatcher matcher : this.pathMatchers) {
            String[] args = matcher.getMatchedParameters(url);
            //有匹配的处理
            if (args!=null) {
            	//从urlMap中根据mathcer来获取对应的action
                Action action = pathMap.get(matcher);
                Object[] arguments = new Object[args.length];
                for (int i=0; i<args.length; i++) {
                    Class<?> type = action.arguments[i];
                    if (type.equals(String.class))
                        arguments[i] = args[i];
                    else
                        arguments[i] = converterFactory.convert(type, args[i]);
                }
                //构造一个执行者
                execution = new Execution(req, resp, action, arguments);
                break;
            }
        }
        if (execution!=null) {
        	//处理执行
            handleExecution(execution, req, resp);
        }
        return execution!=null;
    }

    /**
     * 描述：执行action:
     *
     * @param execution
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    void handleExecution(Execution execution, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//如果支持上传, 把当前的request重新包装成一个MultipartHttpServletRequest类.
    	if (this.multipartSupport) {
            if (MultipartHttpServletRequest.isMultipartRequest(request)) {
                request = new MultipartHttpServletRequest(request, maxFileSize);
            }
        }

        //初始化 ActionContext，并在 finally 中移除所有已绑定变量
    	//把request和response封装到ThreadLocal中, 供其他类使用
        JeyyContext.setActionContext(servletContext, request, response);
        
        //调用拦截器链
        try {
            InterceptorChainImpl chains = new InterceptorChainImpl(interceptors);
            //从第一个interceptor 开始处理, 当最后一个拦截器被调用后，InterceptorChain 才真正调用 Execution 对象的 execute() 方法，
            //并保存其返回结果，整个请求处理过程结束，进入渲染阶段。
            chains.doInterceptor(execution);
            //开始渲染
            handleResult(request, response, chains.getResult());
        }
        catch (Exception e) {
            handleException(request, response, e);
        }
        finally {
            JeyyContext.removeActionContext();
        }
    }

    void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws ServletException, IOException {
        try {
            exceptionHandler.handle(request, response, ex);
        }
        catch (ServletException e) {
            throw e;
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * 描述：由于没有强制 HTTP 处理方法的返回类型，因此，handleResult() 方法针对不同的返回值将做不同的处理。
     * @param request
     * @param response
     * @param result
     * @throws Exception
     */
    void handleResult(HttpServletRequest request, HttpServletResponse response, Object result) throws Exception {
        if (result==null)
            return;
        //如果是Renderer的话, 使用Renderer来渲染
        if (result instanceof Renderer) {
            Renderer r = (Renderer) result;
            r.render(this.servletContext, request, response);
            return;
        }
        if (result instanceof String) {
            String s = (String) result;
            
          //如果开头是redirect的话, 那么做转发, 比如:
          //@Mapping("/register") 
          //String register() { 
//              ... 
//              if (success) 
//                  return "redirect:/reg/success"; 
//              return "redirect:/reg/failed"; 
          //} 
            if (s.startsWith("redirect:")) {
                response.sendRedirect(s.substring("redirect:".length()));
                return;
            }
          //如果开头是script的话, 那么就使用JavaScriptRenderer去渲染
            if (s.startsWith("script:")) {
                String script = s.substring("script:".length());
                new JavaScriptRenderer(script).render(servletContext, request, response);
                return;
            }
            //默认使用Text去渲染
            new TextRenderer(s).render(servletContext, request, response);
            return;
        }
        throw new ServletException("Cannot handle result with type '" + result.getClass().getName() + "'.");
    }

    public void destroy() {
        log.info("Destroy Dispatcher...");
        this.iocFactory.destroy();
    }

}
