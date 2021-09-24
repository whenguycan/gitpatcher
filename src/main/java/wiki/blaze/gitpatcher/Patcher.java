package wiki.blaze.gitpatcher;

import wiki.blaze.gitpatcher.interfaces.NameFilter;
import wiki.blaze.gitpatcher.interfaces.PathReader;
import wiki.blaze.gitpatcher.interfaces.PathResolver;
import wiki.blaze.gitpatcher.util.PathHolder;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

/**
 * 简易补丁生成器（读取git日志打包）主程序
 *      目前只应用于编译好的class文件
 *      下版本读取java源码编译并生成补丁
 * @Author wangcy
 * @Date 2021/5/14 13:49
 * @version 0.1
 */
public class Patcher {

    PathReader pathReader;
    PathResolver pathResolver;
    NameFilter nameFilter;
    File sourceDir;
    File patchDir;

    private Patcher() {

    }

    private File getPatchDir() {
        String path = patchDir.getPath() + pathReader.version();
        return new File(path);
    }

    private File getSourceDir() {
        return sourceDir;
    }

    public static Patcher newInstance() {
        return new Patcher();
    }

    public Patcher setPathReader(PathReader pathReader) {
        this.pathReader = pathReader;
        return this;
    }

    public Patcher setPathResolver(PathResolver pathResolver) {
        this.pathResolver = pathResolver;
        return this;
    }

    public Patcher nameFilter(NameFilter nameFilter) {
        this.nameFilter = nameFilter;
        return this;
    }

    public Patcher setPatchDir(File patchDir) {
        this.patchDir = patchDir;
        return this;
    }

    public Patcher setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
        return this;
    }

    private void check() {
        if(pathReader == null) {
            throw new RuntimeException("pathReader must not be null");
        }
        if(pathResolver == null) {
            throw new RuntimeException("pathResolver must not be null");
        }
        if(patchDir == null) {
            throw new RuntimeException("patchDir must not be null");
        }
        if(sourceDir == null) {
            throw new RuntimeException("sourceDir must not be null");
        }
    }

    public void patches() {
        patches(false);
    }

    public void patches(boolean openInExplorer) {
        System.out.println("--> make patch start");
        check();
        Set<String> willExcludes = new HashSet<>();
        Set<String> copyFailed = new HashSet<>();
        Map<String, PathHolder> map = new HashMap<>();
        pathReader.read(getSourceDir())
                .stream()
                .filter(path -> pathResolver.access(path))
                .map(path -> pathResolver.translate(path))
                .filter(pair -> {
                    if (nameFilter != null) {
                        String targetPath = pair.target;
                        Set<String> excludes = nameFilter.excludes();
                        for (String exclude : excludes) {
                            if (targetPath.contains(exclude)) {
                                willExcludes.add(targetPath);
                                return false;
                            }
                        }
                    }
                    return true;
                })
                .forEach(holder -> map.put(holder.path, holder));
        // link filepath to absolute path
        linkPath(map);
        // find inner .class file and fill into map
        fillWithInnerClass(map);
        // copy file
        for(Map.Entry<String, PathHolder> entry : map.entrySet()) {
            fileCopy(entry.getValue(), copyFailed);
            System.out.println("file copy --> " + entry.getValue().target);
        }
        System.out.printf("[%s] files copied\n", map.size());
        copyFailed.forEach(path -> System.out.println("file copy failed --> " + path));
        System.out.printf("[%s] files copy failed\n", copyFailed.size());
        willExcludes.forEach(targetPath -> System.out.println("file exclude --> " + targetPath));
        System.out.printf("[%s] files excluded\n", willExcludes.size());
        System.out.println("--> patchDir: " + getPatchDir().getPath());
        System.out.println("--> make patch complete");
        if(openInExplorer) {
            try {
                Desktop.getDesktop().open(getPatchDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void linkPath(Map<String, PathHolder> map) {
        map.forEach((path, holder) -> {
            holder.source = new File(holder.sourceDir, holder.source).getPath();
            holder.target = new File(getPatchDir(), holder.target).getPath();
        });
    }

    final String CLASS_PATTERN = ".class";

    private void fillWithInnerClass(Map<String, PathHolder> map) {
        Map<String, PathHolder> resultMap = new HashMap<>();
        map.forEach((path, holder) -> {
            String filepathWithoutExt = holder.source.replace(CLASS_PATTERN, "");
            File targetFile = new File(holder.target);
            if(targetFile.exists()) {
                File targetDir = new File(holder.target).getParentFile();
                Arrays.stream(new File(holder.source).getParentFile().listFiles())
                    .forEach(file -> {
                        String filepath = file.getPath();
                        if(filepath.contains(CLASS_PATTERN) && filepath.contains(filepathWithoutExt + "$")) {
                            resultMap.put(filepath, new PathHolder().init(filepath, new File(targetDir, file.getName()).getPath()));
                        }
                    });
            }
        });
        map.putAll(resultMap);
    }

    private void fileCopy(PathHolder holder, Set<String> copyFailed) {
        File sourceFile = new File(holder.source);
        if(!sourceFile.exists()) {
            copyFailed.add(holder.source);
            return;
        }
        File targetFile = new File(holder.target);
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
            copyFailed.add(holder.source);
        }
    }

}
