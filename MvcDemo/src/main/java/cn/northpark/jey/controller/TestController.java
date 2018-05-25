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
//	    return "<h1>Hello, " + name  +"</h1>";
		  Map<String, Object> map = new HashMap<>();
		  map.put("name", name);
		  return new JsonRenderer(JsonUtil.object2json(map));
	  }

//	  @MapPath("/")
//	  public Renderer index() throws Exception {
//	    List posts = DbUtils.queryForList("select id, title, content, creation from Post order by id desc", new Object[0]);
//	    return new TemplateRenderer("/index.htm", "posts", posts);
//	  }
//
//	  @MapPath("/blog/display/$1")
//	  public Renderer display(long id) throws Exception {
//	    List posts = DbUtils.queryForList("select id, title, content, creation from Post where id=?", new Object[] { Long.valueOf(id) });
//	    if (posts.isEmpty())
//	      throw new IllegalArgumentException("Post not found with id: " + id);
//	    return new TemplateRenderer("/display.htm", "post", posts.get(0));
//	  }

//	  @MapPath("/blog/create")
//	  public Renderer create()
//	  {
//	    Post post = new Post(-1L, "", "");
//	    return new TemplateRenderer("/edit.htm", "post", post);
//	  }
//
//	  @MapPath("/blog/update/$1")
//	  public Renderer update(long id)
//	    throws Exception
//	  {
//	    List posts = DbUtils.queryForList("select id, title, content, creation from Post where id=?", new Object[] { Long.valueOf(id) });
//	    if (posts.isEmpty())
//	      throw new IllegalArgumentException("Post not found with id: " + id);
//	    return new TemplateRenderer("/edit.htm", "post", posts.get(0));
//	  }
//
//	  @MapPath("/blog/edit")
//	  public String edit()
//	    throws Exception
//	  {
//	    HttpServletRequest request = ActionContext.getActionContext().getHttpServletRequest();
//	    long id = Long.parseLong(request.getParameter("id"));
//	    String title = request.getParameter("title");
//	    String content = request.getParameter("content");
//	    if (id == -1L)
//	    {
//	      id = DbUtils.nextId();
//	      if (1 != DbUtils.executeUpdate("insert into Post (id, title, content, creation) values (?, ?, ?, ?)", new Object[] { Long.valueOf(id), title, content, new Date() })) {
//	        throw new SQLException("Create post failed.");
//	      }
//
//	    }
//	    else if (1 != DbUtils.executeUpdate("update Post set title=?, content=? where id=?", new Object[] { title, content, Long.valueOf(id) })) {
//	      throw new IllegalArgumentException("Post not found with id: " + id);
//	    }
//	    return "redirect:/blog/display/" + id;
//	  }
//
//	  @MapPath("/blog/delete/$1")
//	  public String delete(long id) throws Exception {
//	    DbUtils.executeUpdate("delete from Post where id=?", new Object[] { Long.valueOf(id) });
//	    return "redirect:/";
//	  }

}
