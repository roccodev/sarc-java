package dev.rocco.filelib.sarc.io;

import java.io.ByteArrayOutputStream;

public class OffsetByteArrayOutputStream extends ByteArrayOutputStream {

    public void write(byte[] bytes, int offset) {
        if(offset + bytes.length > this.buf.length) {
            byte[] grown = new byte[this.buf.length + (offset + bytes.length - this.buf.length)];
            System.arraycopy(this.buf, 0, grown, 0, this.buf.length);

            this.buf = grown;
        }
        System.arraycopy(bytes, 0, this.buf, offset, bytes.length);
        this.count = this.buf.length;
    }

    public int getOffset() {
        return count;
    }

}
