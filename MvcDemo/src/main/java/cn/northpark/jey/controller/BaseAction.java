package cn.northpark.jey.controller;

import cn.northpark.jeyy.module.JeyyContext;

public abstract class BaseAction{

	
	public JeyyContext JeyyContext() {
		return JeyyContext.getActionContext();
		
	}
	
	
}
