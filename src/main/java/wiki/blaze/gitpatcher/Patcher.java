package wiki.blaze.gitpatcher;

import wiki.blaze.gitpatcher.interfaces.PathReader;
import wiki.blaze.gitpatcher.interfaces.PathResolver;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 简易补丁生成器（读取git日志打包）
 * @Author wangcy
 * @Date 2021/5/14 13:49
 */
public class Patcher {

    PathReader pathReader;
    PathResolver pathResolver;


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

    public void patches() {
        if(pathReader == null) {
            throw new RuntimeException("pathReader is null");
        }
        if(pathResolver == null) {
            throw new RuntimeException("pathResolver is null");
        }
        Map<String, String> map = pathReader.read()
                .stream()
                .filter(path -> pathResolver.access(path))
                .collect(Collectors.toMap(path -> path, path -> pathResolver.translate(path)));
        map.entrySet().forEach(entry -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        });
    }

}
