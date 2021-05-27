package wiki.blaze.gitpatcher;

import wiki.blaze.gitpatcher.interfaces.NameFilter;
import wiki.blaze.gitpatcher.interfaces.PathReader;
import wiki.blaze.gitpatcher.interfaces.PathResolver;
import wiki.blaze.gitpatcher.util.PathHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 简易补丁生成器（读取git日志打包）主程序
 * @Author wangcy
 * @Date 2021/5/14 13:49
 */
public class Patcher {

    PathReader pathReader;
    PathResolver pathResolver;
    NameFilter nameFilter;
    File sourceDir;
    File patchDir;

    private Patcher() {

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
        System.out.println("--> make patch start");
        check();
        Set<String> willExcludes = new HashSet<>();
        Set<PathHolder> pathHolderSet = new HashSet<>();
        pathHolderSet.addAll(
                pathReader.read(sourceDir)
                        .stream()
                        .filter(path -> pathResolver.access(path))
                        .map(path -> pathResolver.translate(path))
                        .filter(pair -> {
                            if(nameFilter == null) {
                                return true;
                            }else {
                                String targetPath = pair.target;
                                Set<String> excludes = nameFilter.excludes();
                                for (String exclude : excludes) {
                                    if(targetPath.contains(exclude)) {
                                        willExcludes.add(targetPath);
                                        return false;
                                    }
                                }
                                return true;
                            }
                        })
                        .collect(Collectors.toSet()));
        pathHolderSet.forEach(holder -> {
            fileCopy(holder);
            System.out.println("file copy --> " + holder.target);
        });
        System.out.println(String.format("[%s] files copied", pathHolderSet.size()));
        willExcludes.forEach(targetPath -> System.out.println("file exclude --> " + targetPath));
        System.out.println(String.format("[%s] files excluded", willExcludes.size()));
        System.out.println("--> make patch complete");
    }

    private void fileCopy(PathHolder pathHolder) {
        File sourceFile = new File(pathHolder.sourceDir, pathHolder.source);
        File targetFile = new File(patchDir, pathHolder.target);
        if(!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try (OutputStream os = new FileOutputStream(targetFile)) {
            Files.copy(sourceFile.toPath(), os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
