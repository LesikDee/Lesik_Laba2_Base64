package com.company.dema.base64;

import java.io.IOException;
import java.io.InputStream;


public class Base64InputStream extends InputStream {
    private final InputStream in;
    private int byteCounter = 0;
    private byte firstPartOfByte;
    private byte secondPartOfByte;
    private byte nextByte;
    private int data;
    private final static char[] BASE64_ARR = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};//таблица перевода в base64

    public Base64InputStream(InputStream in) throws IOException {
        this.in = in;
        data = in.read();
        if (data != -1) {
            firstPartOfByte = transform(data);
            if (firstPartOfByte != -1)
                firstPartOfByte <<= 2;
            else throw new IOException("Data is not  Base64 format");
        } else throw new IOException("The file is empty");
    }

    private int extraRead() throws IOException {
        data = in.read();
        if (data != -1) {
            firstPartOfByte = transform(data);
            if (firstPartOfByte != -1)
                firstPartOfByte <<= 2;
            else
                throw new IOException("Data is not  Base64 format");
            return 0;
        } else
            return -1;
    }

    private byte transform(int byte6) {
        for (byte letter = 0; letter < 64; letter++)
            if (byte6 == BASE64_ARR[letter])
                return letter;
        return -1;
    }

    @Override
    public int read() throws IOException {
        int endOfFile = 0;
        byte byteHelper;
        if (byteCounter == 3) {
            byteCounter = 0;
            endOfFile = extraRead();
        }
        byteCounter++;
        if ((endOfFile == 0) && ((data = in.read()) != -1) && (data != '=')) {
            secondPartOfByte = transform(data);
            if (secondPartOfByte == -1)
                throw new IOException("Data is not  Base64 format");
            secondPartOfByte = (byte) (secondPartOfByte << 2);
            if (secondPartOfByte >= 0)
                byteHelper = (byte) (secondPartOfByte >> (8 - 2 * byteCounter));
            else
                byteHelper = (byte) ((secondPartOfByte >> (8 - 2 * byteCounter)) ^ (-128 >> 7 - 2 * byteCounter));
            nextByte = (byte) (firstPartOfByte | byteHelper);
            firstPartOfByte = (byte) (secondPartOfByte << (2 * (byteCounter)));
            return nextByte >= 0 ? nextByte : nextByte + 256;
        } else {
            if ((byteCounter != 1) && (data != '='))
                throw new IOException("Data is not  Base64 format");
            return -1;
        }
    }

}