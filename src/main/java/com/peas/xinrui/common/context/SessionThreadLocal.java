package com.peas.xinrui.common.context;

public class SessionThreadLocal extends ThreadLocal<Context> {
    private static final SessionThreadLocal INSTANCE = new SessionThreadLocal();

    private SessionThreadLocal() {

    }

    public static SessionThreadLocal getInstance() {
        return INSTANCE;
    }
}