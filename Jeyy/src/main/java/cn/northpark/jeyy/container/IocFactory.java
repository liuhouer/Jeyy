package cn.northpark.jeyy.container;

import java.util.List;

import cn.northpark.jeyy.config.Config;

/**
 * 集成 IoC： 
 * 当接收到来自浏览器的请求，并匹配到合适的 URL 时，
 * 应该转发给某个 Controller 实例的某个标记有 @Mapping 的方法，
 * 这需要持有所有 Controller 的实例。不过，让一个 MVC 框架去管理这些组件并不是一个好的设计，
 * 这些组件可以很容易地被 IoC 容器管理，MVC 框架需要做的仅仅是向 IoC 容器请求并获取这些组件的实例。
 * 为了解耦一种特定的 IoC 容器，我们通过 ContainerFactory 来获取所有 Controller 组件的实例
 * @author bruce 
 */
public interface IocFactory {

    /**
     * Init container factory.
     */
    void init(Config config);

    /**
     * Find all beans in container.
     * findAllBeans() 返回 IoC 容器管理的所有 Bean，然后，
     * 扫描每一个 Bean 的所有 public 方法，并引用那些标记有 @Mapping 的方法实例。
     */
    List<Object> findAllBeans();

    /**
     * When container destroyed.
     */
    void destroy();
}
