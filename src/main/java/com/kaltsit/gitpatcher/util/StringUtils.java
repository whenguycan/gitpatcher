package com.kaltsit.gitpatcher.util;

/**
 * @author wangcy
 * @date 2021/11/9 13:47
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
