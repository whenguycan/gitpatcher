package wiki.blaze.gitpatcher.git;

import wiki.blaze.gitpatcher.interfaces.PathReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangcy
 * @date 2021/11/9 11:38
 */
public abstract class GitLogPathReader implements PathReader {

    protected List<String> execCommand(File commandDir, String command) {
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

}
