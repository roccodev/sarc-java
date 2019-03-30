package dev.rocco.filelib.sarc.sarc;

import dev.rocco.filelib.sarc.io.FileReader;

import java.io.File;

public class SarcFile {

    private SarcHeader header;

    private FileReader reader;

    public SarcFile(File input) {
        reader = new FileReader(input);
        reader.parse();

        header = new SarcHeader();
    }

    public SarcHeader getHeader() {
        return header;
    }

    public FileReader getReader() {
        return reader;
    }
}
