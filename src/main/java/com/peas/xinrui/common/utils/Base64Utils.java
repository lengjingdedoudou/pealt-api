package com.peas.xinrui.common.utils;

import com.sunnysuperman.commons.model.ObjectId;
import org.apache.commons.codec.binary.Base64;

public class Base64Utils {
    private static final char[] ALPHA_NUMERICS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_"
            .toCharArray();

    public static String encode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        byte[] bytes = s.getBytes(StringUtils.UTF8_CHARSET);
        byte[] encodedBytes = Base64.encodeBase64(bytes);
        return new String(encodedBytes, StringUtils.UTF8_CHARSET);
    }

    public static String encode(byte[] bytes) {
        byte[] encodedBytes = Base64.encodeBase64(bytes);
        return new String(encodedBytes, StringUtils.UTF8_CHARSET);
    }

    public static String encodeUrlSafe(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        byte[] bytes = s.getBytes(StringUtils.UTF8_CHARSET);
        byte[] encodedBytes = Base64.encodeBase64URLSafe(bytes);
        return new String(encodedBytes, StringUtils.UTF8_CHARSET);
    }

    public static String encodeUrlSafe(byte[] bytes) {
        byte[] encodedBytes = Base64.encodeBase64URLSafe(bytes);
        return new String(encodedBytes, StringUtils.UTF8_CHARSET);
    }

    public static String decode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        byte[] bytes = s.getBytes(StringUtils.UTF8_CHARSET);
        byte[] decodedBytes = Base64.decodeBase64(bytes);
        return new String(decodedBytes, StringUtils.UTF8_CHARSET);
    }

    public static byte[] decodeAsBytes(String s) {
        byte[] bytes = s.getBytes(StringUtils.UTF8_CHARSET);
        return Base64.decodeBase64(bytes);
    }

    public static String genBase64ShortUUID() {
        StringBuilder buf = new StringBuilder();
        byte[] bytes = new ObjectId().toByteArray();
        int p1 = 64 * 64 * 64;
        int p2 = 64 * 64;
        int p3 = 64;
        for (int i = 0; i < 4; i++) { // (3*8)*4 = (4*6)*4
            int offset = i * 3;
            int v = 0;
            for (int j = 0; j < 3; j++) {
                v += Math.pow(256, 3 - j - 1) * (bytes[offset + j] & 0xFF);
            }
            int v1 = v / p1;
            v -= p1 * v1;
            int v2 = v / p2;
            v -= p2 * v2;
            int v3 = v / p3;
            int v4 = v - p3 * v3;
            buf.append(ALPHA_NUMERICS[v1]);
            buf.append(ALPHA_NUMERICS[v2]);
            buf.append(ALPHA_NUMERICS[v3]);
            buf.append(ALPHA_NUMERICS[v4]);
        }
        return buf.toString();
    }
}
