package com.kaltsit.gitpatcher.patcher;

import java.io.File;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 13:56
 */
public interface PathReader {

    PathReader init(File commandDir);

    Set<String> read();

    String version();

}
