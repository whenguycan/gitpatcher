package com.kaltsit.gitpatcher.patcher;

/**
 * @author wangcy
 * @date 2021/11/9 13:57
 */
public interface NameFilter {

    boolean access(String path);

}
