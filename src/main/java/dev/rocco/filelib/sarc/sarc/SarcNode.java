// Licensed under GPLv2+
// Original: https://github.com/zeldamods/sarc/blob/master/sarc/sarc.py
// Ported to Java by RoccoDev
// Copyright 2018 leoetlino <leo@leolam.fr>
package dev.rocco.filelib.sarc.sarc;

import dev.rocco.filelib.sarc.io.FileReader;

public class SarcNode {

    public static final short NODE_SIZE = 0x10;

    private int offset, nameTableOffset;

    private int nameHash, flags, nameOffset;

    private int dataStart, dataEnd;

    private String name;

    public SarcNode(int offset, int nameTableOffset) {
        this.offset = offset;
        this.nameTableOffset = nameTableOffset;
    }

    public void parse(FileReader reader) {
        nameHash = reader.readInt(offset);

        int attributes = reader.readInt(offset + 4);
        flags = attributes >> 24;
        nameOffset = attributes & 0xffffff;

        dataStart = reader.readInt(offset + 8);
        dataEnd = reader.readInt(offset + 12);

        int absoluteNameOffset = nameTableOffset + 4 * nameOffset;
        name = reader.readString(absoluteNameOffset);
    }

    public static int calculateHash(String name, int length, long key) {
        long result = 0;
        for(int i = 0; i < length; i++)
        {
            result = name.charAt(i) + result * key;
        }
        return (int) result;
    }

    public int getNameHash() {
        return nameHash;
    }

    public int getFlags() {
        return flags;
    }

    public int getNameOffset() {
        return nameOffset;
    }

    public int getDataStart() {
        return dataStart;
    }

    public int getDataEnd() {
        return dataEnd;
    }

    public String getName() {
        return name;
    }
}
