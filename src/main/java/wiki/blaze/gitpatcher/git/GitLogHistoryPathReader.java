package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.util.PathHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/9/24 11:20
 */
public class GitLogHistoryPathReader extends GitLogPathReader {

    int history;

    public GitLogHistoryPathReader(int history) {
        this.history = history;
        if(history < 1 || history > 99) {
            throw new RuntimeException("history must between 1 and 99");
        }
    }

    public Set<PathHolder> read(File commandDir) {
        String command = String.format("git log --oneline -%s", history);
        List<String> list = execCommand(commandDir, command);
        List<String> hashList = new ArrayList<>();
        for(String line : list) {
            if(line != null && line.length() > 6) {
                String hash = line.substring(0, 7);
                hashList.add(hash);
            }
        }
        super.hashes = hashList.toArray(new String[0]);
        return super.read(commandDir);
    }
}
