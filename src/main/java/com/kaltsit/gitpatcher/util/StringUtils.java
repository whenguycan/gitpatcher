package com.kaltsit.gitpatcher.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wangcy
 * @date 2021/11/9 13:47
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String getCurrentTimestamp() {
        return DateTimeFormatter.ofPattern("_yyyy_MM_dd_HH_mm_ss").format(LocalDateTime.now());
    }

}
