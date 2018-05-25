package cn.northpark.jeyy.template.jsp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.northpark.jeyy.config.Config;
import cn.northpark.jeyy.template.Template;
import cn.northpark.jeyy.template.TemplateFactory;

/**
 * 定义jsp模板工厂 继承模板工厂抽象类
 * 用来加载初始化和加载jsp模板
 * 
 * @author bruce 
 */
public class JspTemplateFactory extends TemplateFactory {

	private Log log = LogFactory.getLog(getClass());



	public void init(Config config) {
		log.info("JspTemplateFactory init ok.");
	}




	public Template loadTemplate(String path) throws Exception {
		if (log.isDebugEnabled())
			log.debug("Load JSP template '" + path + "'.");
		return new JspTemplate(path);
	}


}
