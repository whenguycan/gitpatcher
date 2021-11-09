package com.kaltsit.gitpatcher.util;

/**
 * 源文件与目标文件对
 * @author wangcy
 * @date 2021/11/9 13:46
 */
public class PathPair {

    /**源文件相对路径*/
    String source;
    /**目标文件相对路径*/
    String target;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
