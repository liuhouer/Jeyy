package cn.northpark.jeyy.util.converter;

/**
 * Convert String to Boolean.
 * 
 * @author bruce 
 */
public class BooleanConverter implements Converter<Boolean> {

    public Boolean convert(String s) {
        return Boolean.parseBoolean(s);
    }

}
