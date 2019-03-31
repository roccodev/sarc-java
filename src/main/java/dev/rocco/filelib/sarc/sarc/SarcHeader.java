package dev.rocco.filelib.sarc.sarc;

import dev.rocco.filelib.sarc.io.FileReader;
import dev.rocco.filelib.sarc.io.FileWriter;
import dev.rocco.filelib.sarc.sarc.table.SfatHeader;
import dev.rocco.filelib.sarc.sarc.table.SfntHeader;

public class SarcHeader {

    public static final short HEADER_LENGTH = 0x14;

    private short byteOrderMark;
    private int fileSize;
    private int dataOffsetStart;

    private int version;

    private SfatHeader sfatHeader;
    private SfntHeader sfntHeader;

    public boolean parse(FileReader reader) {
        String magic = reader.readString(0, 4);
        if(!magic.equals("SARC")) {
            System.err.println("Unknown SARC magic.");
            return false;
        }

        byteOrderMark = reader.readShort(6);
        System.out.println(byteOrderMark);

        fileSize = reader.readInt(8);

        dataOffsetStart = reader.readInt(12);
        version = reader.readShort(0x10);

        sfatHeader = new SfatHeader();
        sfatHeader.parse(reader);

        sfntHeader = new SfntHeader();
        sfntHeader.parse(sfatHeader, reader);

        return true;
    }

    public void write(FileWriter writer) {
        writer.writeString(0, "SARC");

        writer.writeShort(4, HEADER_LENGTH);
        writer.writeShort(6, (short) 0xFEFF);

        /* File size and data start will be written later */

        writer.writeInt(0x10, 0x0100);

    }

    public short getByteOrderMark() {
        return byteOrderMark;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getVersion() {
        return version;
    }

    public int getDataOffsetStart() {
        return dataOffsetStart;
    }

    public SfatHeader getSfatHeader() {
        return sfatHeader;
    }

    public SfntHeader getSfntHeader() {
        return sfntHeader;
    }
}
