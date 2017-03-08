package com.company.dema.base64;

import java.io.IOException;
import java.io.OutputStream;

public class Base64OutputStream extends OutputStream {
    private int byteCounter = 0;
    private final byte[] primaryBuffer;
    private final OutputStream out;

    public Base64OutputStream(OutputStream out) {
        this.out = out;
        primaryBuffer = new byte[3];
    }

    @Override
    public void write(int a) throws IOException {
        primaryBuffer[byteCounter++] = (byte) a;
        if (byteCounter == 3) {
            out.write(new Coder(primaryBuffer).codeInfo.getBytes());
            byteCounter = 0;
        }
    }

    @Override
    public void close() throws IOException {
        if (byteCounter != 0) {
            byte[] partial = new byte[byteCounter];
            for (int i = 0; i < byteCounter; i++)
                partial[i] = primaryBuffer[i];
            out.write(new Coder(partial).codeInfo.getBytes());
        }
        out.close();
    }
}
