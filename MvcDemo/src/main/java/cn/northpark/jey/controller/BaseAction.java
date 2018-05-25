package cn.northpark.jey.controller;

import cn.northpark.jeyy.module.ActionContext;

public abstract class BaseAction{

	
	public ActionContext JeyyContext() {
		return ActionContext.getActionContext();
		
	}
	
	
}
