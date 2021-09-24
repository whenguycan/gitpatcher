package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.util.PathHolder;
import wiki.blaze.gitpatcher.util.StringUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * git日志文件读取器
 * @Author wangcy
 * @Date 2021/5/14 16:47
 */
public class GitLogShowPathReader extends GitLogPathReader {

    public GitLogShowPathReader(String... hashes) {
        super(hashes);
    }

    public Set<PathHolder> read(File commandDir) {
        Set<PathHolder> set = new HashSet<>();
        if(hashes == null || hashes.length == 0) {
            throw new RuntimeException("hashes must not be empty");
        }
        for (String hash : hashes) {
            if(StringUtils.isNotEmpty(hash)) {
                set.addAll(read0(commandDir, hash));
            }
        }
        return set;
    }

    private Set<PathHolder> read0(File commandDir, String hash) {
        String command = String.format("git show %s --name-only", hash);
        return execCommand(commandDir, command);
    }

}
