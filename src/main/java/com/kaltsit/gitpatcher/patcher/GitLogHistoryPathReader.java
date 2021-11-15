package com.kaltsit.gitpatcher.patcher;

import java.util.List;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/11/9 15:33
 */
public class GitLogHistoryPathReader extends GitLogHashPathReader {

    int history;

    public GitLogHistoryPathReader(int history) {
        this.history = history;
        if(history < 1 || history > 99) {
            throw new RuntimeException("history must between 1 and 99");
        }
    }

    public Set<String> read() {
        String command = String.format("git log --oneline -%s", history);
        List<String> list = execCommand(command);
        for(String line : list) {
            if(line != null && line.length() > 6) {
                String hash = line.substring(0, 7);
                super.hashes.add(hash);
            }
        }
        return super.read();
    }

}
