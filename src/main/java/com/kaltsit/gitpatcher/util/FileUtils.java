package com.kaltsit.gitpatcher.util;

import java.io.File;

/**
 * @author wangcy
 * @date 2021/11/9 16:05
 */
public class FileUtils {

    public static String getFileName(String path) {
        return StringUtils.isEmpty(path) ? null : getFileName(new File(path));
    }

    public static String getFileName(File file) {
        if(file == null) {
            return null;
        }
        String filename = file.getName();
        int idx = filename.indexOf(".");
        return idx > 0 ? filename.substring(0, idx) : filename;
    }

}
