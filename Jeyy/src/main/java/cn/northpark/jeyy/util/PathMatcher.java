package cn.northpark.jeyy.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.northpark.jeyy.config.ConfigException;

/**
 * Match URL by regular expression.
 * 当扫描到标记有 @Mapping 注解的方法时，需要首先检查 URL 与方法参数是否匹配，
 * UrlMatcher 用于将 @Mapping 中包含 $1、$2 ……的字符串变为正则表达式，
 * 进行预编译，并检查参数个数是否符合方法参数
 * @author bruce 
 */
public final class PathMatcher {

    final Log log = LogFactory.getLog(getClass());
    public final String url;
    int[] orders;
    Pattern pattern;

    /**
     * 将 @Mapping 中包含 $1、$2 ……的字符串变为正则表达式的转换规则是，
     * 依次将每个 $n 替换为 ([^\\/]*)，其余部分作精确匹配。
     * 例如，“/blog/$1/$2”变化后的正则表达式为：^\\/blog\\/([^\\/]*)\\/([^\\/]*)$
     * Java 字符串需要两个连续的“\\”表示正则表达式中的转义字符“\”。将“/”排除在变量匹配之外可以避免很多歧义。
     * @param url Url may contains $1, $2, ... $9.
     */
    public PathMatcher(String url) {
        this.url = url;
        StringBuilder sb = new StringBuilder(url.length() + 20);
        sb.append('^');
        List<Integer> paramList = new ArrayList<Integer>();
        Set<Integer> paramSet = new HashSet<Integer>();
        int start = 0;
        for (;;) {
            int n = url.indexOf('$', start);
            if (n!=(-1) && n<url.length()-1 && isParamIndex(url.charAt(n+1))) {
                // get index:
                int i = url.charAt(n+1) - '0';
                // $x found:
                paramSet.add(i);
                paramList.add(i);
                addExactMatch(sb, url.substring(start, n));
                addParameterMatch(sb);
                start = n + 2;
            }
            else {
                // $x not found!
                addExactMatch(sb, url.substring(start, url.length()));
                break;
            }
        }
        // check parameters:
        if (paramList.size()!=paramSet.size())
            throw new ConfigException("Duplicate parameters.");
        for (int i=1; i<=paramSet.size(); i++) {
            if (!paramSet.contains(i))
                throw new ConfigException("Missing parameter '$" + i + "'.");
        }
        this.orders = new int[paramList.size()];
        for (int i=0; i<paramList.size(); i++) {
            this.orders[i] = paramList.get(i) - 1;
        }
        sb.append('$');
        this.pattern = Pattern.compile(sb.toString());
    }

    public int getArgumentCount() {
        return this.orders.length;
    }

    boolean isParamIndex(char c) {
        return c>='1' && c<='9';
    }

    void addParameterMatch(StringBuilder sb) {
        sb.append("([^\\/]*)");
    }

    void addExactMatch(StringBuilder sb, String s) {
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (c>='a' && c<='z')
                sb.append(c);
            else if (c>='A' && c<='Z')
                sb.append(c);
            else if (c>='0' && c<='9')
                sb.append(c);
            else {
                int n = SAFE_CHARS.indexOf(c);
                if (n==(-1)) {
                    // need encoding:
                    log.warn("Warning: URL contains unsafe character '" + c + "'.");
                    sb.append("\\u").append(toHex(c));
                }
                else {
                    sb.append('\\').append(c);
                }
            }
        }
    }

    static final String[] EMPTY_STRINGS = new String[0];
    static final String SAFE_CHARS = "/$-_.+!*'(),";

    String toHex(char c) {
        int i = c;
        return Integer.toHexString(i).toUpperCase();
    }

    /**
     *  根据正则表达式匹配 URL，若匹配成功，返回从 URL 中提取的参数，
     * 若匹配失败，返回 null 
     * 
     * @param url The target url.
     * @return String[] array or null if url is not match.
     */
    public String[] getMatchedParameters(String url) {
        Matcher m = pattern.matcher(url);
        if (!m.matches())
            return null;
        if (orders.length==0)
            return EMPTY_STRINGS;
        String[] params = new String[orders.length];
        for (int i=0; i<orders.length; i++) {
            params[orders[i]] = m.group(i+1);
        }
        return params;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this)
            return true;
        if (obj instanceof PathMatcher) {
            return ((PathMatcher)obj).url.equals(this.url);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

}
