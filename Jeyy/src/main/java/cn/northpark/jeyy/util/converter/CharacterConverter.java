package cn.northpark.jeyy.util.converter;

/**
 * Convert String to Character.
 * 
 * @author bruce 
 */
public class CharacterConverter implements Converter<Character> {

    public Character convert(String s) {
        if (s.length()==0)
            throw new IllegalArgumentException("Cannot convert empty string to char.");
        return s.charAt(0);
    }

}
