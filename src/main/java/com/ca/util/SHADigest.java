package com.ca.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHADigest {
    public static String getDigest(String message) {
        try {
            byte[] plaintext = message.getBytes("utf-8");
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(plaintext);
            byte[] ciphertext = messageDigest.digest();
            return HexStringUtil.getInstance().bytesToHexString(ciphertext);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
