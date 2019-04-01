// Licensed under GPLv2+
// Original: https://github.com/zeldamods/sarc/blob/master/sarc/sarc.py
// Ported to Java by RoccoDev
// Copyright 2018 leoetlino <leo@leolam.fr>
package dev.rocco.filelib.sarc.sarc.table;

import dev.rocco.filelib.sarc.io.FileReader;
import dev.rocco.filelib.sarc.sarc.SarcHeader;
import dev.rocco.filelib.sarc.sarc.SarcNode;

public class SfntHeader {

    public static final short HEADER_LENGTH = 0x8;

    public void parse(SfatHeader sfat, FileReader reader) {
        int offset = (SfatHeader.HEADER_LENGTH + SarcHeader.HEADER_LENGTH) + SarcNode.NODE_SIZE * sfat.getNodeCount();

        String magic = reader.readString(offset, 4);
        if(!magic.equals("SFNT")) {
            System.err.println("Unknown SFNT magic.");
        }
    }


}
