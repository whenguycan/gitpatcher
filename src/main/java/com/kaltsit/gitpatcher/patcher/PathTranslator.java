package com.kaltsit.gitpatcher.patcher;

import com.kaltsit.gitpatcher.util.PathPair;

import java.io.File;

/**
 * @author wangcy
 * @date 2021/11/9 13:56
 */
public interface PathTranslator {

    boolean access(String path);

    PathPair translate(PathPair pair, File sourceDir, File targetDir);

}
