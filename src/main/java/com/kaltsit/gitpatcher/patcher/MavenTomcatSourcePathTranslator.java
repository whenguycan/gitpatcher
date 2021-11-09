package com.kaltsit.gitpatcher.patcher;

import com.kaltsit.gitpatcher.patcher.MavenTomcatPathTranslator;
import com.kaltsit.gitpatcher.util.FileUtils;
import com.kaltsit.gitpatcher.util.PathPair;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 15:44
 */
public class MavenTomcatSourcePathTranslator extends MavenTomcatPathTranslator {

    public MavenTomcatSourcePathTranslator() {
        super("src/main/java", "src/main/business", "src/main/app", "src/main/core", "src/main/resourcelibrary");
    }

    public Set<PathPair> translate(String path, File sourceDir, File targetDir) {
        String relativePath = path.replace(patternMatches, "").replace(".java", ".class");
        File sourceFile = new File(sourceDir, super.sourceClasspath + relativePath);
        File targetFile = new File(targetDir, super.targetClasspath + relativePath);
        Set<PathPair> set = new HashSet<>();
        set.add(new PathPair(sourceFile.getPath(), targetFile.getPath()));
        File sDir = sourceDir.getParentFile();
        if(sDir.exists()) {
            File[] files = sDir.listFiles();
            if(files != null) {
                File tDir = targetFile.getParentFile();
                String pattern = FileUtils.getFileName(sourceFile) + "$";
                for(File file : files) {
                    if(file.getName().contains(pattern)) {
                        set.add(new PathPair(new File(sDir, file.getName()).getPath(), new File(tDir, file.getName()).getPath()));
                    }
                }
            }
        }
        return set;
    }
}
