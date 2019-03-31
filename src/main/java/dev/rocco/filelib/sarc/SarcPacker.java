package dev.rocco.filelib.sarc;

import dev.rocco.filelib.sarc.io.FileWriter;
import dev.rocco.filelib.sarc.sarc.SarcHeader;
import dev.rocco.filelib.sarc.sarc.table.SfatHeader;
import dev.rocco.filelib.sarc.util.FileAlignment;

import java.io.File;
import java.io.IOException;

public class SarcPacker {

    public static void packIntoSarc(File directoryToPack, File outputFile) {
        FileWriter writer = new FileWriter(outputFile);

        SarcHeader header = new SarcHeader();
        header.write(writer);

        SfatHeader sfat = new SfatHeader();
        sfat.write(writer);

        try {
            FileAlignment.writeAligned(directoryToPack, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        writer.writeToFile();
    }

}
