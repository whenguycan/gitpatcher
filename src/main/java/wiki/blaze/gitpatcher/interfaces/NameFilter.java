package wiki.blaze.gitpatcher.interfaces;

import java.util.Set;

/**
 * 文件名过滤器
 * @Author wangcy
 * @Date 2021/5/20 14:25
 */
public interface NameFilter {

    /**
     * 文件名黑名单
     * @return
     */
    Set<String> excludes();

}
