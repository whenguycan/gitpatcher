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
public abstract class GitLogPathReader implements PathReader {

    String[] hashes;

    public GitLogPathReader(String... hashes) {
        this.hashes = hashes;
    }

    protected Set<PathHolder> execCommand(File commandDir, String command) {
        try {
            Set<PathHolder> set = new HashSet<>();
            Runtime runtime = Runtime.getRuntime();
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

}
