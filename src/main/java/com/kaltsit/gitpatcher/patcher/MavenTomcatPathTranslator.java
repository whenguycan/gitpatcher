package com.kaltsit.gitpatcher.patcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 15:37
 */
public abstract class MavenTomcatPathTranslator implements PathTranslator {

    protected final String sourceClasspath = "/target/classes";
    protected final String targetClasspath = "/WEB-INF/classes";

    private Set<String> patternSet = new HashSet<>();
    protected String patternMatches;

    protected MavenTomcatPathTranslator(String... patterns) {
        if(patterns == null) {
            throw new RuntimeException("patterns is empty");
        }
        patternSet.addAll(Arrays.asList(patterns));
    }

    public boolean access(String path) {
        for (String pattern : patternSet) {
            if(path != null && path.contains(pattern)) {
                patternMatches = pattern;
                return true;
            }
        }
        return false;
    }
}
