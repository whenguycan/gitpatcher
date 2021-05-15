package wiki.blaze.gitpatcher.interfaces;

import wiki.blaze.gitpatcher.PathPair;

import java.util.Map;

/**
 * 路径解析器
 * @Author wangcy
 * @Date 2021/5/14 16:47
 */
public interface PathResolver {

    boolean access(String path);

    PathPair translate(String path);

}

