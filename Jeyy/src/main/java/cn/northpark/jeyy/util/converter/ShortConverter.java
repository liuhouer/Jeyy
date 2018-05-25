package cn.northpark.jeyy.util.converter;

/**
 * Convert String to Short.
 * 
 * @author bruce 
 */
public class ShortConverter implements Converter<Short> {

    public Short convert(String s) {
        return Short.parseShort(s);
    }

}
