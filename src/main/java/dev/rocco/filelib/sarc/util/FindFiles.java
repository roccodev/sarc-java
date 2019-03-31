package dev.rocco.filelib.sarc.util;

import java.io.File;
import java.util.List;

public class FindFiles {

    public static List<File> getAllFiles(File rootDir, List<File> start) {
        for(File f : rootDir.listFiles()) {
            System.out.println(f.isDirectory() + " / " + f.getName());
            if(f.isDirectory()) getAllFiles(f, start);
            else start.add(f);
        }
        return start;
    }

}
