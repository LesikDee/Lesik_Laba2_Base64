package com.company.dema;

import com.company.dema.base64.ApplicationBase64;
import com.company.dema.base64.Base64InputStream;
import com.company.dema.base64.Base64OutputStream;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class Base64Test {
    private boolean byteArrCompare(byte[] arr1, byte[] arr2) {
        boolean areThereEqual = true;
        if (arr1.length != arr2.length) {
            areThereEqual = false;
        } else {
            for (int i = 0; i < arr1.length; i++)
                if (arr1[i] != arr2[i]) {
                    areThereEqual = false;
                    break;
                }
        }
        return areThereEqual;

    }

    private void testLauncher(byte[] inArray) throws IOException {
        OutputStream encoded = new ByteArrayOutputStream();
        OutputStream decoded = new ByteArrayOutputStream();
        ApplicationBase64.recordFromInToOut((new ByteArrayInputStream(inArray)),
                new Base64OutputStream(encoded));
        byte[] encodedByteArray = encoded.toString().getBytes();
        ApplicationBase64.recordFromInToOut(new Base64InputStream(new ByteArrayInputStream(encodedByteArray)), decoded);
        assertEquals(true, byteArrCompare(inArray, decoded.toString().getBytes()));//сравниваю изначальную строку и декодированную

    }

    @Test
    public void test0Help() {
        String inText[] = {"--help"};
        ApplicationBase64.main(inText);

    }

    @Test
    public void test1Model() {
        String inText1[] = {"--code", "-i", "test1Encode.txt"};//lo Base64"};
        ApplicationBase64.main(inText1);
        System.out.println();
        String inText2[] = {"--decode", "-i", "test1Decode.txt"};
        ApplicationBase64.main(inText2);
    }

    @Test
    public void test2Simple() throws IOException {
        String inText = "Tom did play hookey, and he had a very good time. He got back home barely in season to help Jim," +
                " the small colored boy, saw next-day's wood and split the kindlings before supper at least he was there" +
                " in time to tell his adventures to Jim while Jim did three-fourths of the work. Tom's younger brother " +
                "(or rather half-brother) Sid was already through with his part of the work (picking up chips)," +
                " for he was a quiet boy, and had no adventurous, trouble-some ways.";
        testLauncher(inText.getBytes());
    }

    @Test
    public void test3Picture() throws IOException {
        String inText = new FileInputStream("les.jpg").toString();
        testLauncher(inText.getBytes());
    }

    @Test
    public void test4Picture() {// наглядный пример

        String inText1[] = {"--code", "-i", "les.jpg", "-o", "codedLes.txt"};
        String inText2[] = {"--decode", "-i", "codedLes.txt", "-o", "decodeLes.jpg"};
        ApplicationBase64.main(inText1);
        ApplicationBase64.main(inText2);


    }

    @Test
    public void test5WrongParams() {

        String inText1[] = {"--wrongCommand", ""};
        ApplicationBase64.main(inText1);

        String inText2[] = {"--code", "Hello", "Bye.txt", "AddParam"};
        ApplicationBase64.main(inText2);

        String inText3[] = {"--decode", "-i", "Не на того напали"};
        ApplicationBase64.main(inText3);

        String inText4[] = {"lalalacode", "Hello", "Bye"};
        ApplicationBase64.main(inText4);

        String inText5[] = {"--help", "Hello"};
        ApplicationBase64.main(inText5);

        String inText6[] = {"c", "Hello"};
        ApplicationBase64.main(inText6);
    }
}