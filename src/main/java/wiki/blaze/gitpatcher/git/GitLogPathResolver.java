package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.PathPair;
import wiki.blaze.gitpatcher.interfaces.PathResolver;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * git日志路径解析器
 * @Author wangcy
 * @Date 2021/5/14 16:48
 */
public class GitLogPathResolver implements PathResolver {

    public boolean access(String path) {
        return isSourcePath(path) || isResourcePath(path) || isWebappPath(path);
    }

    public PathPair translate(String path) {
        if(isSourcePath(path)) {
            int idx = path.lastIndexOf(".");
            String pre = path.substring(0, idx);
            return new PathPair(
                    pathReplace(pre, sourcePathSet, "target/classes").concat(".class"),
                    pathReplace(pre, sourcePathSet, "/WEB-INF/classes").concat(".class")
            );
        }else if(isResourcePath(path)) {
            return new PathPair(
                    pathReplace(path, resourcePathSet, "target/classes"),
                    pathReplace(path, resourcePathSet, "/WEB-INF/classes")
            );
        }else if(isWebappPath(path)) {
            return new PathPair(
                    path,
                    pathReplace(path, webappPathSet, "")
            );
        }else {
            throw new RuntimeException("-->path not match in resolver: " + path);
        }
    }

    Set<String> sourcePathSet = Arrays.stream(new String[]{
            "src/main/java",
            "src/main/business", "src/main/app",
            "src/main/core", "/src/main/resourcelibrary"
    }).collect(Collectors.toSet());
    Set<String> resourcePathSet = Arrays.stream(new String[]{"src/main/resources"}).collect(Collectors.toSet());
    Set<String> webappPathSet = Arrays.stream(new String[]{"src/main/webapp"}).collect(Collectors.toSet());

    boolean isSourcePath(String path) {
        return pathMatches(path, sourcePathSet);
    }

    boolean isResourcePath(String path) {
        return pathMatches(path, resourcePathSet);
    }

    boolean isWebappPath(String path) {
        return pathMatches(path, webappPathSet);
    }

    boolean pathMatches(String path, Set<String> matcherPathSet) {
        for (String matcherPath : matcherPathSet) {
            if(path.startsWith(matcherPath)) {
                return true;
            }
        }
        return false;
    }

    String pathReplace(String path, Set<String> pathSet, String replaceBy) {
        for (String p : pathSet) {
            if(path.startsWith(p)) {
                return path.replace(p, replaceBy);
            }
        }
        throw new RuntimeException("set not contains path: " + path);
    }
}
