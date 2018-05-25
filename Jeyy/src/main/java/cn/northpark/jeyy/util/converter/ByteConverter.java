package cn.northpark.jeyy.util.converter;

/**
 * Convert String to Byte.
 * 
 * @author bruce 
 */
public class ByteConverter implements Converter<Byte> {

    public Byte convert(String s) {
        return Byte.parseByte(s);
    }

}
