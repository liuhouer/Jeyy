# Jeyy
个人作品：参考Springmvc，参考大神的实现思路,实现的轻量级的Mvc 框架，后续考虑插入轻量orm。



## 特点

1. Jeyy支持RESTful的url结构，自动按顺序匹配合适的参数；
1. 少量配置，打包运行；
1. 可以按需灵活扩展；

## 运行环境

1. servlet 3.0环境，Tomcat/Jetty；
1. 基于[guice]的Ioc，可以扩展IOC工厂；
1. [gradle]构建；

## 配置web.xml

``` xml
<servlet>
		<servlet-name>dispatcher</servlet-name>
		<!-- 配置核心控制器  -->
		<servlet-class>cn.northpark.jeyy.servlet.JeyyServlet</servlet-class>
		<init-param>
			<param-name>template</param-name>
			<!-- 配置选择的模板引擎，可以Jsp 或者扩展freemarker等等 -->
			<param-value>Velocity</param-value>
		</init-param>
		<init-param>
			<param-name>container</param-name>
			<!-- 配置容器工厂，可以扩展为Spring等等 -->
			<param-value>Guice</param-value>
		</init-param>
		<init-param>
			<param-name>modules</param-name>
			<!-- 配置Guice的初始化Module，按需自定义... -->
			<param-value>cn.northpark.jey.module.ActionModule</param-value>
		</init-param>
		 
		<load-on-startup>0</load-on-startup>
	</servlet>
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

请参考例子 mvcDomo的实现


## 进阶

 1. 自定义拦截器（实现cn.northpark.jeyy.interceptor.Interceptor）
 2. 连接db操作








[guice]: http://code.google.com/p/google-guice/
[gradle]:https://gradle.org/



