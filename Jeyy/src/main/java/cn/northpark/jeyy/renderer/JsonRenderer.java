package cn.northpark.jeyy.renderer;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author bruce
 * 直接输出文本的简化函数
 */
public class JsonRenderer extends Renderer  {

	private static final String ENCODING_DEFAULT = "UTF-8";

	//content-type 定义 //
	private static final String JSON_TYPE = "application/json";
	
	private String characterEncoding;
	private String text;
	
	
	public JsonRenderer() {
		super();
		this.characterEncoding = ENCODING_DEFAULT;
	}
	
	public JsonRenderer(String text) {
		super();
		this.characterEncoding = ENCODING_DEFAULT;
		this.text = text;
	}
	
	public JsonRenderer(String characterEncoding, String text) {
		super();
		this.characterEncoding = characterEncoding;
		this.text = text;
	}

	@Override
	public void render(ServletContext context, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		
		 StringBuilder sb = new StringBuilder(64);
	        sb.append(JSON_TYPE)
	          .append(";charset=")
	          .append(characterEncoding==null ? "UTF-8" : characterEncoding);
	        response.setContentType(sb.toString());
	        PrintWriter pw = response.getWriter();
	        pw.write(text);
	        pw.flush();
	}
	
	
	
	
}
