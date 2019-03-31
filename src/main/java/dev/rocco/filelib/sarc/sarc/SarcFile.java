package dev.rocco.filelib.sarc.sarc;

import dev.rocco.filelib.sarc.io.FileReader;
import dev.rocco.filelib.sarc.io.FileWriter;
import dev.rocco.filelib.sarc.sarc.table.SfatHeader;
import dev.rocco.filelib.sarc.sarc.table.SfntHeader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SarcFile {

    private SarcHeader header;
    private SarcNode[] nodes;


    private FileReader reader;
    private FileWriter writer;

    public SarcFile(File input) {
        reader = new FileReader(input);
        reader.parse();

        header = new SarcHeader();
        header.parse(reader);

        List<SarcNode> nodes = new ArrayList<>();
        int currentNodeOffset = SarcHeader.HEADER_LENGTH + SfatHeader.HEADER_LENGTH;
        int nameTableOffset = SfntHeader.HEADER_LENGTH +
                (SfatHeader.HEADER_LENGTH + SarcHeader.HEADER_LENGTH) + SarcNode.NODE_SIZE *
                        header.getSfatHeader().getNodeCount();

        for(int i = 0; i < header.getSfatHeader().getNodeCount(); i++) {
            SarcNode node = new SarcNode(currentNodeOffset, nameTableOffset);
            node.parse(reader);
            nodes.add(node);

            currentNodeOffset += SarcNode.NODE_SIZE;
        }

        this.nodes = nodes.toArray(new SarcNode[0]);
    }

    public void extract(File targetDir) throws IOException {
        if(!targetDir.isDirectory())
            throw new RuntimeException("Target must be a directory.");

        for(SarcNode node : nodes) {
            String fileName = node.getName();
            if(fileName.isEmpty()) continue;
            File target = new File(targetDir.getAbsolutePath() + "/" + fileName);
            target.getParentFile().mkdirs();
            if(!target.exists()) target.createNewFile();

            byte[] data = reader.readBytes(header.getDataOffsetStart() + node.getDataStart(),
                    node.getDataEnd() - node.getDataStart());

            Files.write(target.toPath(), data, StandardOpenOption.WRITE);

        }
    }


    public SarcHeader getHeader() {
        return header;
    }

    public FileReader getReader() {
        return reader;
    }

    public SarcNode[] getNodes() {
        return nodes;
    }
}
