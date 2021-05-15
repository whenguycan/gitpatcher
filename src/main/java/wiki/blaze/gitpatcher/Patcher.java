package wiki.blaze.gitpatcher;

import wiki.blaze.gitpatcher.interfaces.PathReader;
import wiki.blaze.gitpatcher.interfaces.PathResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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
    File sourceDir;
    File targetDir;

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

    public Patcher setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
        return this;
    }

    public Patcher setTargetDir(File targetDir) {
        this.targetDir = targetDir;
        return this;
    }

    public void patches() {
        if(pathReader == null) {
            throw new RuntimeException("pathReader is null");
        }
        if(pathResolver == null) {
            throw new RuntimeException("pathResolver is null");
        }
        Set<PathPair> pathPairSet = pathReader.read()
                .stream()
                .filter(path -> pathResolver.access(path))
                .map(path -> pathResolver.translate(path))
                .collect(Collectors.toSet());
        pathPairSet.forEach(pair -> fileCopy(pair));
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
