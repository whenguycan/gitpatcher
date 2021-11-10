package com.kaltsit.gitpatcher.patcher;

import com.kaltsit.gitpatcher.util.PathPair;

import java.io.File;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 13:56
 */
public interface PathTranslator {

    boolean accept(String path);

    Set<PathPair> translate(String path, File sourceDir, File targetDir);

}
