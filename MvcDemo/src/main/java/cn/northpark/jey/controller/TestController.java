package cn.northpark.jey.controller;

import java.util.HashMap;
import java.util.Map;

import cn.northpark.jey.util.JsonUtil;
import cn.northpark.jeyy.annotation.MapPath;
import cn.northpark.jeyy.renderer.JsonRenderer;
import cn.northpark.jeyy.renderer.Renderer;
import cn.northpark.jeyy.renderer.TemplateRenderer;

public class TestController extends BaseAction{
	
	
	
	@MapPath("/text")
	public String test() {
		return "111";
	}
	
	@MapPath("/hello")
	  public String hello()
	  {
	    return "<h1>Hello, world</h1>";
	  }

	  @MapPath("/hello/$1/$2")
	  public String hello(String name,String age) {
	    return "<h1>Hello, " + name +","+age+ "</h1>";
	  }
	  
	  @MapPath("/getPar")
	  public Renderer getPar() {
		  String name = JeyyContext().getRequest().getParameter("name");
//	    return "<h1>Hello, " + name  +"</h1>";
		  Map<String, Object> map = new HashMap<>();
		  map.put("name", name);
		  return new TemplateRenderer("/index.html", map);
	  }
	  
	  @MapPath("/getJson")
	  public Renderer getJson() {
		  String name = JeyyContext().getRequest().getParameter("name");
		  Map<String, Object> map = new HashMap<>();
		  map.put("name", name);
		  return new JsonRenderer(JsonUtil.object2json(map));
	  }

//	  @MapPath("/")
//	  public Renderer returnDemo() throws Exception {
//	    return new JsonRenderer(jsonstr);
//	    return new TemplateRenderer("/index.html", "list", list);
//          return new TemplateRenderer("/show.html", "entity", list.get(0));
//	    return new OtherExtendsRenderer(otherstr,Encoding);	
//	    return "redirect:/goods/show/" + id;
//	    return "redirect:/";
//	  }
//

