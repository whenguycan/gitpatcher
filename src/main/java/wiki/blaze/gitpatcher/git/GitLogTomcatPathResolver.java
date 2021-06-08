package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.util.PathHolder;
import wiki.blaze.gitpatcher.interfaces.PathResolver;
import wiki.blaze.gitpatcher.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * git日志路径解析器
 * @Author wangcy
 * @Date 2021/5/14 16:48
 */
public class GitLogTomcatPathResolver implements PathResolver {

    String sourceClassesRelativePath;
    String targetClassesRelativePath;

    Set<String> sourcePathSet = new HashSet<>();
    Set<String> resourcePathSet = new HashSet<>();
    Set<String> webappPathSet = new HashSet<>();

    public GitLogTomcatPathResolver(
            String sourceClassesRelativePath,
            String targetClassesRelativePath,
            String[] sourcePathSet,
            String[] resourcePathSet,
            String[] webappPathSet
    ) {
        this.sourceClassesRelativePath = sourceClassesRelativePath;
        this.targetClassesRelativePath = targetClassesRelativePath;
        if(sourcePathSet != null) {
            this.sourcePathSet.addAll(Arrays.stream(sourcePathSet).collect(Collectors.toSet()));
        }
        if(resourcePathSet != null) {
            this.resourcePathSet.addAll(Arrays.stream(resourcePathSet).collect(Collectors.toSet()));
        }
        if(webappPathSet != null) {
            this.webappPathSet.addAll(Arrays.stream(webappPathSet).collect(Collectors.toSet()));
        }
    }

    public GitLogTomcatPathResolver() {
        this(
                "target/classes",
                "/WEB-INF/classes",
                new String[]{
                        "src/main/java",
                        "src/main/business", "src/main/app", "src/main/core", "src/main/resourcelibrary"
                },
                new String[]{"src/main/resources"},
                new String[]{"src/main/webapp"}
        );
    }

    private void pathCheck() {
        if(StringUtils.isEmpty(sourceClassesRelativePath) || StringUtils.isEmpty(targetClassesRelativePath)) {
            throw new RuntimeException("relative path not initialized");
        }
    }

    public boolean access(PathHolder holder) {
        pathCheck();
        String path = holder.path;
        return isSourcePath(path) || isResourcePath(path) || isWebappPath(path);
    }

    public PathHolder translate(PathHolder holder) {
        pathCheck();
        String path = holder.path;
        if(isSourcePath(path)) {
            int idx = path.lastIndexOf(".");
            String pre = path.substring(0, idx);
            return holder.init(
                    pathReplace(sourcePathSet, pre, sourceClassesRelativePath) + ".class",
                    pathReplace(sourcePathSet, pre, targetClassesRelativePath) + ".class"
            );
        }else if(isResourcePath(path)) {
            return holder.init(
                    pathReplace(resourcePathSet, path, sourceClassesRelativePath),
                    pathReplace(resourcePathSet, path, targetClassesRelativePath)
            );
        }else if(isWebappPath(path)) {
            return holder.init(
                    path,
                    pathReplace(webappPathSet, path, "")
            );
        }else {
            throw new RuntimeException("-->path not match in resolver: " + path);
        }
    }

    boolean isSourcePath(String path) {
        return pathMatches(sourcePathSet, path);
    }

    boolean isResourcePath(String path) {
        return pathMatches(resourcePathSet, path);
    }

    boolean isWebappPath(String path) {
        return pathMatches(webappPathSet, path);
    }

    boolean pathMatches(Set<String> matcherPathSet, String path) {
        for (String matcherPath : matcherPathSet) {
            if(path.indexOf(matcherPath) == 0) {
                return true;
            }
        }
        return false;
    }

    String pathReplace(Set<String> pathSet, String path, String replaceBy) {
        for (String p : pathSet) {
            if(path.indexOf(p) == 0) {
                return path.replace(p, replaceBy);
            }
        }
        throw new RuntimeException("not contains any item in pathSet for path: " + path);
    }
}
