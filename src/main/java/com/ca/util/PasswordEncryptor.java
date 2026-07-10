package com.ca.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {

    private Object salt;
    private String algorithm;
    private static HexStringUtil hexStringUtil = HexStringUtil.getInstance();

    public PasswordEncryptor(Object salt, String algorithm) {
        this.salt = salt;
        this.algorithm = algorithm;
    }

    public String encode(String rawPwd) {
        String result = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            result =
                hexStringUtil.bytesToHexString(messageDigest.digest(mergePasswordAndSalt(rawPwd).getBytes("utf-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isPasswordValid(String encPwd, String rawPwd) {
        String password = this.encode(rawPwd);
        return encPwd.equals(password);
    }

    private String mergePasswordAndSalt(String password) {
        if (password == null) {
            password = "";
        }
        if ((this.salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }
}
