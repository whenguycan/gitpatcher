package wiki.blaze.gitpatcher;

import wiki.blaze.gitpatcher.filters.DefaultNameFilter;
import wiki.blaze.gitpatcher.git.GitLogPathReader;
import wiki.blaze.gitpatcher.git.GitLogPathResolver;

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

        Patcher.newInstance()
                .setSourceDir(sourceDir)
                .setPatchDir(patchDir)
                .setPathReader(new GitLogPathReader(args))
                .setPathResolver(new GitLogPathResolver())
                .nameFilter(new DefaultNameFilter())
                .patches();
    }

}
