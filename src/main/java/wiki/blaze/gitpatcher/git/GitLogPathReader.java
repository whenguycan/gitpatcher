package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.interfaces.PathReader;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * git日志文件读取器
 * @Author wangcy
 * @Date 2021/5/14 16:47
 */
public class GitLogPathReader implements PathReader {

    String hash;
    File dir;

    public GitLogPathReader(String hash, File commandDir) {
        this.hash = hash;
        this.dir = commandDir;
    }

    public Set<String> read() {
        try {
            Set<String> set = new HashSet<>();
            Runtime runtime = Runtime.getRuntime();
            String command = String.format("git show %s --name-only", hash);
            Process process = runtime.exec(command, null, dir);
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = reader.readLine()) != null) {
                set.add(line);
            }
            return set;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
