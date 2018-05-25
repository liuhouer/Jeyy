package cn.northpark.jeyy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to sort interceptors.
 * 
 * @author bruce 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptorOrder {

    /**
     * Lower value has more priority.
     */
    int value();

}
