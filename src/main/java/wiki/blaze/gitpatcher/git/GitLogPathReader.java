package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.interfaces.PathReader;
import wiki.blaze.gitpatcher.util.PathHolder;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * git日志文件读取器
 * @Author wangcy
 * @Date 2021/5/14 16:47
 */
public class GitLogPathReader implements PathReader {

    String[] hashes;

    public GitLogPathReader(String... hashes) {
        this.hashes = hashes;
    }

    public Set<PathHolder> read(File commandDir) {
        Set<PathHolder> set = new HashSet<>();
        if(hashes == null || hashes.length == 0) {
            throw new RuntimeException("hashes must not be empty");
        }
        for (String hash : hashes) {
            set.addAll(read0(commandDir, hash));
        }
        return set;
    }

    private Set<PathHolder> read0(File commandDir, String hash) {
        try {
            Set<PathHolder> set = new HashSet<>();
            Runtime runtime = Runtime.getRuntime();
            String command = String.format("git show %s --name-only", hash);
            Process process = runtime.exec(command, null, commandDir);
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = reader.readLine()) != null) {
                set.add(new PathHolder(line, commandDir));
            }
            reader.close();
            is.close();
            return set;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
