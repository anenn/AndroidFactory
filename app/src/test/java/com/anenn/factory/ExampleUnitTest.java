package com.anenn.factory;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        String msg = "adf";
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(msg.getBytes());
        byte[] digestBytes = digest.digest();

        StringBuffer sb = new StringBuffer();
        for (int i=0, size=digestBytes.length; i<size; i++) {
            String hex = Integer.toHexString(0xff & digestBytes[i]);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            sb.append(hex);
        }
        System.out.println(sb.toString());
        assertEquals(4, 2 + 2);
    }

}