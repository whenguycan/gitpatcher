package wiki.blaze.gitpatcher;

import wiki.blaze.gitpatcher.filters.DefaultNameFilter;
import wiki.blaze.gitpatcher.git.GitLogHistoryPathReader;
import wiki.blaze.gitpatcher.git.GitLogPathReader;
import wiki.blaze.gitpatcher.git.GitLogTomcatPathResolver;
import wiki.blaze.gitpatcher.interfaces.PathReader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * jar包方法入口
 * @Author wangcy
 * @Date 2021/5/27 9:25
 */
public class Startup {

    public static void main(String[] args) {
        File sourceDir = new File(System.getProperty("user.dir"));
        File patchDir = new File(
                sourceDir,
                sourceDir.getName() + new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss").format(new Date()));
        if(args == null) {
            throw new RuntimeException("参数错误");
        }
        String mode = args[0];
        PathReader reader = null;
        String[] hashes = new String[args.length - 1];
        for(int i=0,len=hashes.length; i<len; i++) {
            hashes[i] = args[i + 1];
        }
        switch(mode) {
            case "simple":
                reader = new GitLogPathReader(sourceDir, hashes);
                break;
            case "history":
                reader = new GitLogHistoryPathReader(sourceDir, Integer.parseInt(args[1]));
                break;
            default:
                throw new RuntimeException("参数[0]错误，不存在的模式");
        }
        Patcher.newInstance()
                .setSourceDir(sourceDir)
                .setPatchDir(patchDir)
                .setPathReader(reader)
                .setPathResolver(new GitLogTomcatPathResolver())
                .nameFilter(new DefaultNameFilter())
                .patches();
    }

}
