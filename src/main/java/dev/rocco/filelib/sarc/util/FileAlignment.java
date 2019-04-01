// Licensed under GPLv2+
// Original: https://github.com/zeldamods/sarc/blob/master/sarc/sarc.py
// Ported to Java by RoccoDev
// Copyright 2018 leoetlino <leo@leolam.fr>
package dev.rocco.filelib.sarc.util;

import dev.rocco.filelib.sarc.io.FileWriter;
import dev.rocco.filelib.sarc.sarc.SarcFile;
import dev.rocco.filelib.sarc.sarc.SarcHeader;
import dev.rocco.filelib.sarc.sarc.SarcNode;
import dev.rocco.filelib.sarc.sarc.table.SfatHeader;
import dev.rocco.filelib.sarc.sarc.table.SfntHeader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileAlignment {

    public static HashMap<String, Integer> alignments = new HashMap<>();

    public static void addAlignment(String extension, int alignment) {
        alignments.put(extension, alignment);
    }

    public static void refresh() {
        addAlignment("ksky", 8);
        addAlignment("bksky", 8);

        addAlignment("gtx", 0x2000);
        addAlignment("sharcb", 0x1000);
        addAlignment("sharc", 0x1000);

        addAlignment("baglmf", 0x80);

        addAlignment("bffnt", 0x2000);
    }

    public static int alignInt(int toAlign) {
        return (toAlign & 3) == 0 ? toAlign : (toAlign | 3) + 1;
    }

    private static int getFileNameSize(String name) {
        if(name.isEmpty()) return 0;
        return alignInt(name.length() + 1);
    }

    public static SarcFile writeAligned(File rootDir, FileWriter writer) throws IOException {
        List<File> files = FindFiles.getAllFiles(rootDir, new ArrayList<>());

        int nameTableSize = files.stream().mapToInt(f -> {
            String pathName = rootDir.toURI().relativize(f.toURI()).getPath();
            return getFileNameSize(pathName);
        }).sum();

        int paddedSizes = files.stream().limit(files.size() - 1)
                .mapToInt(f -> alignInt((int) f.length())).sum();

        int lastFileSize = (int) files.get(files.size() - 1).length();

        int dataSize = paddedSizes + lastFileSize;

        int nameTableOffset = SfntHeader.HEADER_LENGTH + (SarcNode.NODE_SIZE * files.size() +
                SarcHeader.HEADER_LENGTH + SfatHeader.HEADER_LENGTH);

        int dataOffsetStart = nameTableOffset + nameTableSize;
        int fileSize = dataOffsetStart + dataSize;

        /* Now we can write the offset and the file size */
        writer.writeInt(8, fileSize);
        writer.writeInt(12, dataOffsetStart);
        writer.writeShort(SarcHeader.HEADER_LENGTH + 6, (short) files.size());

        int currentNodeOffset = SarcHeader.HEADER_LENGTH + SfatHeader.HEADER_LENGTH;
        int nodeStart = 0;
        int entryCode = 0;

        for(File file : files) {
            String pathName = rootDir.toURI().relativize(file.toURI()).getPath();

            int hash = (int) SarcNode.calculateHash(pathName, pathName.length(), 0x65);

            int nameSize = getFileNameSize(pathName);

            int encoded = entryCode;
            if(nameSize > 0) encoded |= 1 << 24;

            writer.writeInt(currentNodeOffset, hash);
            writer.writeInt(currentNodeOffset + 4, encoded); // Flags
            writer.writeInt(currentNodeOffset + 8, nodeStart); // Beginning of data

            nodeStart += file.length();
            writer.writeInt(currentNodeOffset + 12, nodeStart); // End of data

            nodeStart = alignInt(nodeStart);

            entryCode += nameSize / 4;
            currentNodeOffset += SarcNode.NODE_SIZE;
        }

        int sfntOffset = (SfatHeader.HEADER_LENGTH + SarcHeader.HEADER_LENGTH) + SarcNode.NODE_SIZE * files.size();

        writer.writeString(sfntOffset, "SFNT");
        writer.writeShort(sfntOffset + 4, SfntHeader.HEADER_LENGTH);

        int currentNameOffset = sfntOffset + SfntHeader.HEADER_LENGTH;

        /* Write file names */
        for(File file : files) {
            String pathName = rootDir.toURI().relativize(file.toURI()).getPath();
            int padding = getFileNameSize(pathName) - pathName.length();
            writer.writeString(currentNameOffset, pathName);

            currentNameOffset += pathName.getBytes().length;

            for(int i = 0; i < padding; i++) {
                writer.writeByte(currentNameOffset += i, (byte) 0);
            }
        }

        int currentDataOffset = currentNameOffset;

        /* Write file data */
        for(int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            byte[] data = Files.readAllBytes(file.toPath());

            writer.writeBytes(currentDataOffset, data);

            currentDataOffset += data.length;

            if(i + 1 != files.size()) {
                int padding = alignInt((int) file.length()) - (int) file.length();
                for(int j = 0; j < padding; j++) {
                    writer.writeByte(currentDataOffset += j, (byte) 0);
                }
            }
        }

        return null;

    }

}
