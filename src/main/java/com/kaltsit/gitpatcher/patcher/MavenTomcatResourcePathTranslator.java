package com.kaltsit.gitpatcher.patcher;

import com.kaltsit.gitpatcher.util.PathPair;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 16:17
 */
public class MavenTomcatResourcePathTranslator extends MavenTomcatPathTranslator {

    public MavenTomcatResourcePathTranslator() {
        super("src/main/resources");
    }

    public Set<PathPair> translate(String path, File sourceDir, File targetDir) {
        String relativePath = path.replace(patternMatches, "");
        File sourceFile = new File(sourceDir, super.sourceClasspath + relativePath);
        File targetFile = new File(targetDir, super.targetClasspath + relativePath);
        Set<PathPair> set = new HashSet<>();
        set.add(new PathPair(sourceFile.getPath(), targetFile.getPath()));
        return set;
    }
}
