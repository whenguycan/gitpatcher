package wiki.blaze.gitpatcher.git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcy
 * @date 2021/9/24 11:20
 */
public class GitLogHistoryPathReader extends GitLogPathReader {

    int history;

    public GitLogHistoryPathReader(File commandDir, int history) {
        super(commandDir);
        this.history = history;
        if(history < 1 || history > 99) {
            throw new RuntimeException("history must between 1 and 99");
        }
        String command = String.format("git log --oneline -%s", history);
        List<String> list = execCommand(command);
        List<String> hashList = new ArrayList<>();
        for(String line : list) {
            if(line != null && line.length() > 6) {
                String hash = line.substring(0, 7);
                hashList.add(hash);
            }
        }
        super.hashes = hashList.toArray(new String[0]);
        if(hashes.length == 0) {
            throw new RuntimeException("hashes must not be empty");
        }
    }

    protected void check() {
        if(commandDir == null || !commandDir.exists()) {
            throw new RuntimeException("commandDir not exists");
        }
    }
}
