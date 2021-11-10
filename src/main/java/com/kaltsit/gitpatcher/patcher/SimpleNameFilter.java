package com.kaltsit.gitpatcher.patcher;

import com.kaltsit.gitpatcher.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 15:18
 */
public class SimpleNameFilter implements NameFilter {

    Set<String> set = new HashSet<>();

    public SimpleNameFilter(String... names) {
        if(names != null) {
            set.addAll(Arrays.asList(names));
        }
    }

    public boolean matches(String path) {
        if(StringUtils.isEmpty(path)) {
            throw new RuntimeException("path is null");
        }
        for(String pattern : set) {
            if(path.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
}
