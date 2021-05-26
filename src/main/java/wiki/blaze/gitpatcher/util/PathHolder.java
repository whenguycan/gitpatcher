package wiki.blaze.gitpatcher.util;

import java.io.File;

/**
 * @Author wangcy
 * @Date 2021/5/15 10:21
 */
public class PathHolder {

    public String path;
    public File sourceDir;
    public String source;
    public String target;

    public PathHolder(String path, File sourceDir) {
        this.path = path;
        this.sourceDir = sourceDir;
    }

    public PathHolder init(String source, String target) {
        this.source = source;
        this.target = target;
        return this;
    }

}
