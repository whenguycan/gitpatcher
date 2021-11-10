package com.kaltsit.gitpatcher;

import com.kaltsit.gitpatcher.patcher.NameFilter;
import com.kaltsit.gitpatcher.patcher.PathReader;
import com.kaltsit.gitpatcher.patcher.PathTranslator;
import com.kaltsit.gitpatcher.patcher.MavenTomcatResourcePathTranslator;
import com.kaltsit.gitpatcher.patcher.MavenTomcatSourcePathTranslator;
import com.kaltsit.gitpatcher.patcher.MavenTomcatWebappPathTranslator;
import com.kaltsit.gitpatcher.util.PathPair;
import com.kaltsit.gitpatcher.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
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

    private Patcher() {

    }

    public static Patcher getInstance(File sourceDir, File patchDir) {
        Patcher patcher = new Patcher();
        patcher.sourceDir = sourceDir;
        patcher.patchDir = patchDir;
        return patcher;
    }

    public Patcher setPathReader(PathReader reader) {
        this.reader = reader;
        return this;
    }

    public Patcher useDefaultTranslators() {
        this.translators.add(new MavenTomcatSourcePathTranslator());
        this.translators.add(new MavenTomcatResourcePathTranslator());
        this.translators.add(new MavenTomcatWebappPathTranslator());
        return this;
    }

    public Patcher addPathTranslator(PathTranslator translator) {
        this.translators.add(translator);
        return this;
    }

    public Patcher addNameFilter(NameFilter filter) {
        this.filters.add(filter);
        return this;
    }

    public void patches() {
        patches(false);
    }

    public void patches(boolean openInExplorer) {
        check();
        Set<String> pathSet = reader.read(sourceDir);
        String version = reader.version();
        File targetDir = new File(this.patchDir.getPath() + version);
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
        System.out.println("--> patchDir: " + targetDir.getPath());
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
        try (OutputStream os = new FileOutputStream(targetFile)) {
            Files.copy(sourceFile.toPath(), os);
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

    private void check() {
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
