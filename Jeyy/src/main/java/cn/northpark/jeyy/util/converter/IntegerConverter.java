package cn.northpark.jeyy.util.converter;

/**
 * Convert String to Integer.
 * 
 * @author bruce 
 */
public class IntegerConverter implements Converter<Integer> {

    public Integer convert(String s) {
        return Integer.parseInt(s);
    }

}
