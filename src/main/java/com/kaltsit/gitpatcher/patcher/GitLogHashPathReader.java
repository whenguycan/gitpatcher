package com.kaltsit.gitpatcher.patcher;

import com.kaltsit.gitpatcher.util.StringUtils;

import java.io.File;
import java.util.*;

/**
 * @author wangcy
 * @date 2021/11/9 15:26
 */
public class GitLogHashPathReader extends GitLogPathReader {

    List<String> hashes = new ArrayList<>();

    public GitLogHashPathReader(String... hashes) {
        if(hashes == null) {
            throw new RuntimeException("hashes is null");
        }
        for(String hash : hashes) {
            if(StringUtils.isNotEmpty(hash)) {
                this.hashes.add(hash);
            }
        }
    }

    public Set<String> read(File commandDir) {
        Set<String> set = new LinkedHashSet<>();
        for (String hash : hashes) {
            if(StringUtils.isNotEmpty(hash)) {
                String command = String.format("git show %s --name-only", hash);
                List<String> list = execCommand(commandDir, command);
                if(list != null) {
                    set.addAll(list);
                }
            }
        }
        return set;
    }

    public String version() {
        if(hashes == null || hashes.isEmpty()) {
            return "_no_version_";
        }else {
            int size = hashes.size();
            if(size == 0) {
                return "_no_version_";
            }else if(size == 1) {
                return String.format("_%s_", hashes.get(0));
            }else {
                return String.format("_%s_%s_", hashes.get(0), hashes.get(size - 1));
            }
        }
    }
}
