package cn.northpark.jey.module;


import com.google.inject.Binder;
import com.google.inject.Module;

import cn.northpark.jey.controller.TestController;

public class ActionModule implements Module{

	@Override
	public void configure(Binder binder) {
		// TODO Auto-generated method stub
		binder.bind(TestController.class).asEagerSingleton();
	}



}
