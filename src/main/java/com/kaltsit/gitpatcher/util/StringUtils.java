package com.kaltsit.gitpatcher.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static String getCurrentTimestamp() {
        return DateTimeFormatter.ofPattern("_yyyy_MM_dd_HH_mm_ss").format(LocalDateTime.now());
    }

}
