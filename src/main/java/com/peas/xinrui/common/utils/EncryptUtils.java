package com.peas.xinrui.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptUtils {

    public static String encryptPassword(String password, String salt) {
        if (password == null) {
            return null;
        }
        String encryptPassword = DigestUtils.sha256Hex(password + salt);
        encryptPassword = DigestUtils.sha256Hex(encryptPassword);
        return encryptPassword;
    }

    public static String md5(String input) {
        return DigestUtils.md5Hex(input);
    }

    public static String sha1(String input) {
        return DigestUtils.sha1Hex(input);
    }

}
