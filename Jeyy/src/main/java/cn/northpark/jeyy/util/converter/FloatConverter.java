package cn.northpark.jeyy.util.converter;

/**
 * Convert String to Float.
 * 
 * @author bruce 
 */
public class FloatConverter implements Converter<Float> {

    public Float convert(String s) {
        return Float.parseFloat(s);
    }

}
