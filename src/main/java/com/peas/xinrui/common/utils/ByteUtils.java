package com.peas.xinrui.common.utils;

import com.sunnysuperman.commons.util.ByteUtil;

public class ByteUtils extends ByteUtil {
    public static final byte BYTE_1_NEGATE = -1;
    public static final byte BYTE_0 = 0;
    public static final byte BYTE_1 = 1;
    public static final byte BYTE_2 = 2;
    public static final byte BYTE_3 = 3;

    public static boolean isBoolean(byte b) {
        return b == BYTE_0 || b == BYTE_1;
    }

    public static byte fromBoolean(boolean b) {
        return b ? BYTE_1 : BYTE_0;
    }

    public static boolean toBoolean(byte b) {
        return b > 0;
    }

    public static boolean toBoolean(Byte b, boolean defaultValue) {
        if (b == null) {
            return defaultValue;
        }
        return b.byteValue() > 0;
    }

}
