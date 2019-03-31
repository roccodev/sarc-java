package dev.rocco.filelib.sarc;

import dev.rocco.filelib.sarc.sarc.SarcFile;

import java.io.File;
import java.io.IOException;

public class SarcUnpacker {

    public static void unpack(File input, File targetDirectory) throws IOException {
        SarcFile file = new SarcFile(input);
        file.extract(targetDirectory);
    }

}
