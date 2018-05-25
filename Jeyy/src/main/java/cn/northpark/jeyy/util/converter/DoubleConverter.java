package cn.northpark.jeyy.util.converter;

/**
 * Convert String to Double.
 * 
 * @author bruce 
 */
public class DoubleConverter implements Converter<Double> {

    public Double convert(String s) {
        return Double.parseDouble(s);
    }

}
