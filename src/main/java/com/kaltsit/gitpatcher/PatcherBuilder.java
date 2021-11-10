package com.kaltsit.gitpatcher;

import com.kaltsit.gitpatcher.patcher.*;

import java.io.File;

/**
 * @author wangcy
 * @date 2021/11/10 14:22
 */
public class PatcherBuilder {

    private Patcher patcher = new Patcher();

    public PatcherBuilder setDir(File sourceDir, File patchDir) {
        patcher.sourceDir = sourceDir;
        patcher.patchDir = patchDir;
        return this;
    }

    public PatcherBuilder setPathReader(PathReader reader) {
        patcher.reader = reader;
        return this;
    }

    public PatcherBuilder addPathTranslator(PathTranslator translator) {
        patcher.translators.add(translator);
        return this;
    }

    public PatcherBuilder useDefaultTranslators() {
        patcher.translators.add(new MavenTomcatSourcePathTranslator());
        patcher.translators.add(new MavenTomcatResourcePathTranslator());
        patcher.translators.add(new MavenTomcatWebappPathTranslator());
        return this;
    }

    public PatcherBuilder addNameFilter(NameFilter filter) {
        patcher.filters.add(filter);
        return this;
    }

    public Patcher build() {
        return patcher;
    }

}
