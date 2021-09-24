package wiki.blaze.gitpatcher.util;

/**
 * @Author wangcy
 * @Date 2021/6/4 9:17
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
