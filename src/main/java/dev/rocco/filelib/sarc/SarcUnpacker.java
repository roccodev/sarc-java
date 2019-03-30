package dev.rocco.filelib.sarc;

import dev.rocco.filelib.sarc.sarc.SarcFile;

import java.io.File;

public class SarcUnpacker {

    public static void unpack(File input) {
        SarcFile file = new SarcFile(input);
    }

}
