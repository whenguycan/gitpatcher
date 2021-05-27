package wiki.blaze.gitpatcher.interfaces;

import wiki.blaze.gitpatcher.util.PathHolder;

import java.io.File;
import java.util.Set;

/**
 * 文件路径读取器
 * @Author wangcy
 * @Date 2021/5/14 16:14
 */
public interface PathReader {

    Set<PathHolder> read(File commandDir);

}
