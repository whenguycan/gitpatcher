package com.kaltsit.gitpatcher;

import com.kaltsit.gitpatcher.patcher.NameFilter;
import com.kaltsit.gitpatcher.patcher.PathReader;
import com.kaltsit.gitpatcher.patcher.PathTranslator;
import com.kaltsit.gitpatcher.util.PathPair;
import com.kaltsit.gitpatcher.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

/**
 * 打包器主程序
 * @author wangcy
 * @date 2021/11/9 13:44
 */
public class Patcher {

    File sourceDir;
    File patchDir;

    PathReader reader;
    List<PathTranslator> translators = new ArrayList<>();
    List<NameFilter> filters = new ArrayList<>();

    Patcher() {

    }

    public static PatcherBuilder builder() {
        return new PatcherBuilder();
    }

    public void patches() {
        patches(false);
    }

    public void patches(boolean openInExplorer) {
        check();
        Set<String> pathSet = reader.init(sourceDir).read();
        File targetDir = new File(patchDir.getPath() + reader.version());
        List<PathPair> pairToCopyList = new ArrayList<>();
        Set<String> copyFailed = new LinkedHashSet<>();
        List<String> pathUnrecognizedList = new ArrayList<>();
        List<String> pathExcludeList = new ArrayList<>();
        for(String path : pathSet) {
            if(StringUtils.isNotEmpty(path)) {
                if(inFilter(path)) {
                    pathExcludeList.add(path);
                }else {
                    if(recognized(path)) {
                        for(PathTranslator translator : translators) {
                            if(translator.accept(path)) {
                                pairToCopyList.addAll(translator.translate(path, sourceDir, targetDir));
                                break;
                            }
                        }
                    }else {
                        pathUnrecognizedList.add(path);
                    }
                }
            }
        }
        System.out.println("--> make patch start");
        //copy success
        pairToCopyList.forEach(pair -> {
            fileCopy(pair, copyFailed);
            System.out.println("file copy --> " + pair.target);
        });
        System.out.printf("[%s] files copied\n", pairToCopyList.size());
        //copy failure
        copyFailed.forEach(path -> System.out.println("file copy failed --> " + path));
        System.out.printf("[%s] files copy failed\n", copyFailed.size());
        //exclude
        pathExcludeList.forEach(targetPath -> System.out.println("file exclude --> " + targetPath));
        System.out.printf("[%s] files excluded\n", pathExcludeList.size());
        System.out.println("--> patchAt: " + targetDir.getPath());
        System.out.println("--> make patch complete");
        try {
            Desktop.getDesktop().open(targetDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fileCopy(PathPair pair, Set<String> copyFailed) {
        File sourceFile = new File(pair.source);
        if(!sourceFile.exists()) {
            copyFailed.add(pair.source);
            return;
        }
        File targetFile = new File(pair.target);
        if(!targetFile.getParentFile().exists()) {
            boolean success = targetFile.getParentFile().mkdirs();
            if(!success) {
                throw new RuntimeException("mkdirs failed: " + targetFile.getParentFile().getPath());
            }
        }
        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            copyFailed.add(pair.source);
        }
    }

    private boolean inFilter(String path) {
        for(NameFilter filter : filters) {
            if(filter.matches(path)) {
                return true;
            }
        }
        return false;
    }

    private boolean recognized(String path) {
        for(PathTranslator translator : translators) {
            if(translator.accept(path)) {
                return true;
            }
        }
        return false;
    }

    void check() {
        if(sourceDir == null) {
            throw new RuntimeException("sourceDir is empty");
        }
        if(patchDir == null) {
            throw new RuntimeException("patchDir is empty");
        }
        if(reader == null) {
            throw new RuntimeException("reader is empty");
        }
        if(translators.isEmpty()) {
            throw new RuntimeException("translators is empty");
        }
    }

}
