package cn.northpark.jeyy.util.converter;

/**
 * Convert String to Long.
 * 
 * @author bruce 
 */
public class LongConverter implements Converter<Long> {

    public Long convert(String s) {
        return Long.parseLong(s);
    }

}
