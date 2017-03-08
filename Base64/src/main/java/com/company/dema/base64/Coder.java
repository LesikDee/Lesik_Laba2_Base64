package com.company.dema.base64;


public class Coder {
    private final byte[] buffer;
    private byte binaryBuffer[];
    private int extraByte6 = 0;
    public String codeInfo = "";
    private final static char[] BASE64_ARR = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'}; //таблица перевода в base64

    public Coder(byte[] inBuffer) {
        buffer = inBuffer;
        makeBinaryBuffer();
        makeCode();
    }

    private int determineBufLength(int length8) {
        int length6 = length8;
        if (length8 % 6 == 2) {
            length6 += 16;
            extraByte6 = 2;
        } else if (length8 % 6 == 4) {
            length6 += 8;
            extraByte6 = 1;
        }
        return length6;
    }

    private void makeBinaryBuffer() {
        binaryBuffer = new byte[determineBufLength(buffer.length * 8)];
        int binBuffIterator = 0;
        for (byte aBuffer : buffer) {
            int letter = (aBuffer + 256) % 256;
            for (int j = 0; j < 8; j++) {
                if (letter % 2 == 0)
                    binaryBuffer[binBuffIterator + 7 - j] = 0;
                else
                    binaryBuffer[binBuffIterator + 7 - j] = 1;
                letter /= 2;
            }
            binBuffIterator += 8;
        }
    }

    private void makeCode() {
        for (int i = 0; i < binaryBuffer.length / 6 - extraByte6; i++) {
            byte letter = 0;
            for (int j = 0; j < 5; j++) {
                letter += Math.pow(binaryBuffer[i * 6 + j] * 2, 5 - j);
            }
            letter += binaryBuffer[i * 6 + 5];//  из-за того что 0^0=1
            codeInfo += BASE64_ARR[(int) letter];
        }
        for (int j = 0; j < extraByte6; j++)
            codeInfo += "=";
    }
}


