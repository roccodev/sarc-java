package dev.rocco.filelib.sarc.util;

import java.io.File;
import java.util.List;

public class FindFiles {

    public static List<File> getAllFiles(File rootDir, List<File> start) {
        for(File f : rootDir.listFiles()) {
            if(f.isDirectory()) getAllFiles(f, start);
            else start.add(f);
        }
        return start;
    }

}
