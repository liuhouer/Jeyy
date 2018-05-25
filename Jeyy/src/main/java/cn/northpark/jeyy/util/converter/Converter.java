package cn.northpark.jeyy.util.converter;

/**
 * Convert String to any given type.
 * 
 * @author bruce 
 * 
 * @param <T> Generic type of converted result.
 */
public interface Converter<T> {

    /**
     * Convert a not-null String to specified object.
     */
    T convert(String s);

}
