package dev.rocco.filelib.sarc.sarc;

import dev.rocco.filelib.sarc.io.FileReader;

public class SarcHeader {

    public static final short HEADER_LENGTH = 0x14;

    private short byteOrderMark;
    private int fileSize;
    private int dataOffsetStart;

    private int version;

    public void parse(FileReader reader) {

    }

}
