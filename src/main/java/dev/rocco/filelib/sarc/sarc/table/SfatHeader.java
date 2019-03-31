package dev.rocco.filelib.sarc.sarc.table;

import dev.rocco.filelib.sarc.io.FileReader;
import dev.rocco.filelib.sarc.io.FileWriter;
import dev.rocco.filelib.sarc.sarc.SarcHeader;

public class SfatHeader {

    public static final short HEADER_LENGTH = 0xc;

    private short nodeCount;
    private int hashKey;

    public void parse(FileReader reader) {
        int offset = SarcHeader.HEADER_LENGTH;

        String magic = reader.readString(offset, 4);
        if(!magic.equals("SFAT")) {
            System.err.println("Unknown SFAT magic.");
            return;
        }

        nodeCount = reader.readShort(offset + 6);
        hashKey = reader.readInt(offset + 8);
    }

    public void write(FileWriter writer) {
        int offset = SarcHeader.HEADER_LENGTH;

        writer.writeString(offset, "SFAT");

        writer.writeShort(offset + 4, HEADER_LENGTH);
        writer.writeInt(offset + 8, 0x65);
    }

    public short getNodeCount() {
        return nodeCount;
    }

    public int getHashKey() {
        return hashKey;
    }
}
