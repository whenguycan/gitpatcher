package com.kaltsit.gitpatcher.patcher;

import java.io.File;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 13:56
 */
public interface PathReader {

    Set<String> read(File commandDir);

    String version();

}
