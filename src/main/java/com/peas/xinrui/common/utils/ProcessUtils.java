package com.peas.xinrui.common.utils;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.net.NetworkInterface;

import com.peas.xinrui.common.L;

public class ProcessUtils {

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (Throwable t) {
            L.error(t);
        }
    }

    public static void exitWithMessage(String msg, Throwable t) {
        if (t != null) {
            L.error(msg, t);
        } else if (msg != null) {
            L.error(msg);
        }
        System.exit(0);
    }

    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        if (ex instanceof Error) {
            throw (Error) ex;
        }
        throw new UndeclaredThrowableException(ex);
    }

    public static String getCallStackAsString() {
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < stackElements.length; i++) {
            StackTraceElement trace = stackElements[i];
            buf.append("at ").append(trace.getClassName()).append('(').append(trace.getFileName()).append(":")
                    .append(trace.getLineNumber()).append(')');
            buf.append('\n');
        }
        return buf.toString();
    }

    public static String getMac() {
        InetAddress ia;
        byte[] mac = null;
        try {
            ia = InetAddress.getLocalHost();
            mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString().toUpperCase();
    }
}
