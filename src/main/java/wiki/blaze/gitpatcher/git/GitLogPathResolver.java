package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.util.PathHolder;
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

    public boolean access(PathHolder pathHolder) {
        String path = pathHolder.path;
        return isSourcePath(path) || isResourcePath(path) || isWebappPath(path);
    }

    public PathHolder translate(PathHolder pathHolder) {
        String path = pathHolder.path;
        if(isSourcePath(path)) {
            int idx = path.lastIndexOf(".");
            String pre = path.substring(0, idx);
            return pathHolder.init(
                    pathReplace(sourcePathSet, pre, "target/classes") + ".class",
                    pathReplace(sourcePathSet, pre, "/WEB-INF/classes") + ".class"
            );
        }else if(isResourcePath(path)) {
            return pathHolder.init(
                    pathReplace(resourcePathSet, path, "target/classes"),
                    pathReplace(resourcePathSet, path, "/WEB-INF/classes")
            );
        }else if(isWebappPath(path)) {
            return pathHolder.init(
                    path,
                    pathReplace(webappPathSet, path, "")
            );
        }else {
            throw new RuntimeException("-->path not match in resolver: " + path);
        }
    }

    Set<String> sourcePathSet = Arrays.stream(new String[]{
            "src/main/java",
            "src/main/business", "src/main/app", "src/main/core", "src/main/resourcelibrary"
    }).collect(Collectors.toSet());
    Set<String> resourcePathSet = Arrays.stream(new String[]{"src/main/resources"}).collect(Collectors.toSet());
    Set<String> webappPathSet = Arrays.stream(new String[]{"src/main/webapp"}).collect(Collectors.toSet());

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
