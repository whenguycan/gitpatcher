package wiki.blaze.gitpatcher.filters;

import wiki.blaze.gitpatcher.interfaces.NameFilter;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author wangcy
 * @Date 2021/5/20 14:27
 */
public class DefaultNameFilter implements NameFilter {

    public Set<String> excludes() {
        return new HashSet<String>(){{
            add("Spring-dao.xml");
            add("Spring-mvc.xml");
            add("Spring-Shiro.xml");
            add("config.properties");
            add("artemis.properties");
        }};
    }
}
