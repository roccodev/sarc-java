package dev.rocco.filelib.sarc.sarc.table;

import dev.rocco.filelib.sarc.io.FileReader;

public class SfntHeader {

    public static final short HEADER_LENGTH = 0x8;

    public void parse(FileReader reader) {
        int offset = SfatHeader.HEADER_LENGTH;

        String magic = reader.readString(offset, 4);
        if(!magic.equals("SFNT")) {
            System.err.println("Unknown SFNT magic.");
        }
    }

}
