package com.kaltsit.gitpatcher.patcher;

import com.kaltsit.gitpatcher.util.PathPair;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 16:23
 */
public class MavenTomcatWebappPathTranslator extends MavenTomcatPathTranslator {

    public MavenTomcatWebappPathTranslator() {
        super("src/main/webapp");
    }

    public Set<PathPair> translate(String path, File sourceDir, File targetDir) {
        String relativePath = path.replace(patternMatches, "");
        File sourceFile = new File(sourceDir, path);
        File targetFile = new File(targetDir, relativePath);
        Set<PathPair> set = new HashSet<>();
        set.add(new PathPair(sourceFile.getPath(), targetFile.getPath()));
        return set;
    }
}
