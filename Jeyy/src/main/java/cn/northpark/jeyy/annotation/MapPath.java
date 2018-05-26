package cn.northpark.jeyy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotation for mapping URL.<br/>
 * For example:<br/>
 * <pre>
 * public class Hello {
 *     MapPath("/")
 *     public String index() {
 *         // handle index page...
 *     }
 * 
 *     MapPath("/people/$1")
 *     public String show(int id) {
 *         // show people with id...
 *     }
 * 
 *     MapPath("/people/edit/$1")
 *     public void edit(int id) {
 *         // edit people with id...
 *     }
 *     
 *     MapPath("/people/showmore/$1/$2")
 *     public void showmore(int id,int age) {
 *         // showmore people info with id age...
 *     }
 * }
 * </pre>
 * 
 * @author bruce 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapPath {

    String value();

}
