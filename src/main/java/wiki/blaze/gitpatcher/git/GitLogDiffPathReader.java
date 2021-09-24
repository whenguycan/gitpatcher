package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.util.PathHolder;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/9/24 9:50
 */
public class GitLogDiffPathReader extends GitLogPathReader {

    public GitLogDiffPathReader(String... hashes) {
        super(hashes);
    }

    public Set<PathHolder> read(File commandDir) {
        Set<PathHolder> set = new HashSet<>();
        if(hashes == null || hashes.length != 2) {
            throw new RuntimeException("hashes length must be 2");
        }
        String command = String.format("git diff %s %s --name-only", hashes[0], hashes[1]);
        return execCommand(commandDir, command);
    }

}
