package com.kaltsit.gitpatcher;

import com.kaltsit.gitpatcher.patcher.NameFilter;
import com.kaltsit.gitpatcher.patcher.PathReader;
import com.kaltsit.gitpatcher.patcher.PathTranslator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 打包器
 * @author wangcy
 * @date 2021/11/9 13:44
 */
public class Patcher {

    File sourceDir;
    File targetDir;

    PathReader reader;
    List<PathTranslator> translators = new ArrayList<>();
    List<NameFilter> filters = new ArrayList<>();

    private Patcher() {

    }

    public static Patcher getInstance(File sourceDir, File targetDir) {
        Patcher patcher = new Patcher();
        patcher.sourceDir = sourceDir;
        patcher.targetDir = targetDir;
        return patcher;
    }

    public void patches() {
        patches(false);
    }

    public void patches(boolean openInExplorer) {
        check();

    }

    private void check() {
        if(sourceDir == null) {
            throw new RuntimeException("sourceDir is empty");
        }
        if(targetDir == null) {
            throw new RuntimeException("targetDir is empty");
        }
        if(reader == null) {
            throw new RuntimeException("reader is empty");
        }
        if(translators.isEmpty()) {
            throw new RuntimeException("translators is empty");
        }
    }

}
