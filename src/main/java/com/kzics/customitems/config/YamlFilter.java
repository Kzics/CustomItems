package com.kzics.customitems.config;

import java.io.File;
import java.io.FileFilter;

public class YamlFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".yml");    }
}
