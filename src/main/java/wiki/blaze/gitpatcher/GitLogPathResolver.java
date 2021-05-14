package wiki.blaze.gitpatcher;

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

    public String translate(String path) {
        if(isSourcePath(path)) {

            return path;
        }else if(isResourcePath(path)) {

            return path;
        }else if(isWebappPath(path)) {

            return path;
        }else {
            throw new RuntimeException("-->path not match in resolver: " + path);
        }
    }

    Set<String> sourcePathSet = Arrays.stream(new String[]{
            "src/main/business", "src/main/app", "src/main/core", "/src/main/resourcelibrary"
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
}
