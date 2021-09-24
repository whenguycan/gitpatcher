package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.interfaces.PathReader;
import wiki.blaze.gitpatcher.util.PathHolder;
import wiki.blaze.gitpatcher.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangcy
 * @date 2021/9/24 9:51
 */
public class GitLogPathReader implements PathReader {

    File commandDir;
    String[] hashes;

    /**TODO 这么设计，需要传2次sourceDir，不是很好，考虑怎么能只传一次
        可以考虑不用构造方法传参，使用init方法
     */
    public GitLogPathReader(File commandDir, String... hashes) {
        this.commandDir = commandDir;
        this.hashes = hashes;
        check();
    }

    public Set<PathHolder> read() {
        Set<PathHolder> set = new HashSet<>();
        for (String hash : hashes) {
            if(StringUtils.isNotEmpty(hash)) {
                String command = String.format("git show %s --name-only", hash);
                List<String> list = execCommand(command);
                for(String line : list) {
                    set.add(new PathHolder(line, commandDir));
                }
            }
        }
        return set;
    }

    public String version() {
        if(hashes == null || hashes.length == 0) {
            return "_no_version_";
        }else {
            List<String> list = new ArrayList<>();
            for (String hash : hashes) {
                if(StringUtils.isNotEmpty(hash)) {
                    list.add(hash);
                }
            }
            int size = list.size();
            if(size == 0) {
                return "_no_version_";
            }else if(size == 1) {
                return String.format("_%s_", list.get(0));
            }else {
                return String.format("_%s_%s_", list.get(0), list.get(size - 1));
            }
        }
    }

    protected List<String> execCommand(String command) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command, null, commandDir);
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            List<String> list = new ArrayList<>();
            String line = null;
            while((line = reader.readLine()) != null) {
                list.add(line);
            }
            reader.close();
            is.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected void check() {
        if(commandDir == null || !commandDir.exists()) {
            throw new RuntimeException("commandDir not exists");
        }
        if(hashes == null || hashes.length == 0) {
            throw new RuntimeException("hashes must not be empty");
        }
    }

}
