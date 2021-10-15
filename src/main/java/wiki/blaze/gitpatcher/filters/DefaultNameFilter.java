package wiki.blaze.gitpatcher.filters;

import wiki.blaze.gitpatcher.interfaces.NameFilter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author wangcy
 * @Date 2021/5/20 14:27
 */
public class DefaultNameFilter implements NameFilter {

    public Set<String> excludes() {
        return Arrays.stream(new String[]{
                "Spring-dao.xml",
                "Spring-mvc.xml",
                "Spring-Shiro.xml",
                "config.properties",
                "artemis.properties"
        }).collect(Collectors.toSet());
    }
}
