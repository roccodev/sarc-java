package dev.rocco.filelib.sarc.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/*
 * Shamelessly adapted from my AAMP library.
 * https://github.com/RoccoDev/aamp-java
 *
 * The SARC format is documented here:
 * http://mk8.tockdom.com/wiki/SARC_(File_Format)
 */
public class FileWriter {

    private OffsetByteArrayOutputStream fileContents;
    private File f;

    public FileWriter(File file) {
        this.f = file;
        fileContents = new OffsetByteArrayOutputStream();
    }

    public void writeBytes(int offset, byte[] bytes) {
        fileContents.write(bytes, offset);
    }

    public int getCurrentOffset() {
        return fileContents.getOffset();
    }

    public void writeBytes(int offset, ByteBuffer buffer) {
        fileContents.write(buffer.array(), offset);
    }

    public void writeString(int offset, String s) {
        writeBytes(offset, s.getBytes());
    }

    public void writeInt(int offset, int write) {
        writeBytes(offset, ByteBuffer.allocate(4).putInt(write));
    }

    public void writeFloat(int offset, float write) {
        writeBytes(offset, ByteBuffer.allocate(4).putFloat(write));
    }

    public void writeShort(int offset, short write) {
        writeBytes(offset, ByteBuffer.allocate(2).putShort(write));
    }

    public void writeByte(int offset, byte write) {
        fileContents.write(new byte[] {write}, offset);
    }


    public void writeToFile() {
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println(fileContents.size());
            Files.write(Paths.get(f.toURI()), fileContents.toByteArray(), StandardOpenOption.WRITE);
            fileContents.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}