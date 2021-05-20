package wiki.blaze.gitpatcher;

import wiki.blaze.gitpatcher.interfaces.NameFilter;
import wiki.blaze.gitpatcher.interfaces.PathReader;
import wiki.blaze.gitpatcher.interfaces.PathResolver;
import wiki.blaze.gitpatcher.util.PathPair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 简易补丁生成器（读取git日志打包）
 * @Author wangcy
 * @Date 2021/5/14 13:49
 */
public class Patcher {

    PathReader pathReader;
    PathResolver pathResolver;
    NameFilter nameFilter;
    File sourceDir;
    File targetDir;

    public static Patcher newInstance() {
        return new Patcher();
    }

    public Patcher pathReader(PathReader pathReader) {
        this.pathReader = pathReader;
        return this;
    }

    public Patcher pathResolver(PathResolver pathResolver) {
        this.pathResolver = pathResolver;
        return this;
    }

    public Patcher nameFilter(NameFilter nameFilter) {
        this.nameFilter = nameFilter;
        return this;
    }

    public Patcher sourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
        return this;
    }

    public Patcher targetDir(File targetDir) {
        this.targetDir = targetDir;
        return this;
    }

    public void patches() {
        System.out.println("--> start to make patch");
        if(pathReader == null) {
            throw new RuntimeException("pathReader will not be null");
        }
        if(pathResolver == null) {
            throw new RuntimeException("pathResolver will not be null");
        }
        Set<String> willExcludes = new HashSet<>();
        Set<PathPair> pathPairSet = pathReader.read()
                .stream()
                .filter(path -> pathResolver.access(path))
                .map(path -> pathResolver.translate(path))
                .filter(pair -> {
                    String targetPath = pair.target;
                    if(nameFilter == null) {
                        return true;
                    }else {
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
                .collect(Collectors.toSet());
        System.out.println(String.format("[%s] files to copy", pathPairSet.size()));
        pathPairSet.forEach(pair -> {
            fileCopy(pair);
            System.out.println("file copy --> " + pair.target);
        });
        System.out.println(String.format("[%s] files will be excluded", willExcludes.size()));
        willExcludes.forEach(targetPath -> System.out.println("file exclude --> " + targetPath));
        System.out.println("--> make patch complete");
    }

    private void fileCopy(PathPair pair) {
        File sourceFile = new File(sourceDir, pair.source);
        File targetFile = new File(targetDir, pair.target);
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
