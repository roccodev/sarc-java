package dev.rocco.filelib.sarc.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/*
 * Shamelessly adapted from my AAMP library.
 * https://github.com/RoccoDev/aamp-java
 *
 * The SARC format is documented here:
 * http://mk8.tockdom.com/wiki/SARC_(File_Format)
 */
public class FileReader {

    private File inputFile;
    private byte[] fileContents;

    public FileReader(File inputFile) {
        this.inputFile = inputFile;
    }

    public void parse() {
        try(FileChannel channel = (FileChannel)
                Files.newByteChannel(Paths.get(inputFile.toURI()), StandardOpenOption.READ)) {

            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());

            channel.read(buffer);
            buffer.flip();

            fileContents = buffer.array();

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }


    public byte[] readBytes(int offset, int size) {
        try {
            return Arrays.copyOfRange(fileContents, offset, offset + size);
        } catch(Exception ex) {
            System.out.println("Offset: " + offset + " / Size: " + size);
            ex.printStackTrace();

            return new byte[0];
        }
    }

    private ByteBuffer wrap(byte[] in) {
        return ByteBuffer.wrap(in);
    }

    public String readString(int offset, int size) {
        return new String(readBytes(offset, size));
    }

    public String readString(int offset) {
        return new String(Arrays.copyOfRange(fileContents, offset, getStringOffset(offset)));
    }

    public int getStringOffset(int offsetStart) {
        byte[] copy = Arrays.copyOfRange(fileContents, offsetStart, fileContents.length - 1);
        for(int i = 0; i < copy.length; i++) {
            if(copy[i] == 0) return offsetStart + i;
        }
        return 0;
    }

    public int readInt(int offset) {
        return wrap(readBytes(offset, 4)).getInt();
    }

    public float readFloat(int offset) {
        return wrap(readBytes(offset, 4)).getFloat();
    }

    public short readShort(int offset) {
        return wrap(readBytes(offset, 2)).getShort();
    }

    public byte readByte(int offset) {
        return wrap(readBytes(offset, 1)).get();
    }

}
