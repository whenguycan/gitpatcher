package com.kaltsit.gitpatcher.patcher;

import com.kaltsit.gitpatcher.util.StringUtils;

import java.util.*;

/**
 * @author wangcy
 * @date 2021/11/9 15:26
 */
public class GitLogHashPathReader extends GitLogPathReader {

    List<String> hashes = new ArrayList<>();

    GitLogHashPathReader() {

    }

    public GitLogHashPathReader(String... hashes) {
        if(hashes == null || hashes.length == 0) {
            throw new RuntimeException("hashes is empty");
        }
        for(String hash : hashes) {
            if(StringUtils.isNotEmpty(hash)) {
                this.hashes.add(hash);
            }
        }
    }

    public Set<String> read() {
        Set<String> set = new LinkedHashSet<>();
        for (String hash : hashes) {
            if(StringUtils.isNotEmpty(hash)) {
                String command = String.format("git show %s --name-only", hash);
                List<String> list = execCommand(command);
                if(list != null) {
                    set.addAll(list);
                }
            }
        }
        return set;
    }

    public String version() {
        if(hashes == null || hashes.isEmpty()) {
            throw new RuntimeException("hashes is empty");
        }else {
            int size = hashes.size();
            if(size == 1) {
                return String.format("_%s_", hashes.get(0));
            }else {
                return String.format("_%s_%s_", hashes.get(0), hashes.get(size - 1));
            }
        }
    }
}
