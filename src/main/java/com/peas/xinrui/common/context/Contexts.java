package com.peas.xinrui.common.context;

public class Contexts {
    public static void set(Context context) {
        SessionThreadLocal.getInstance().set(context);
    }

    public static Context get() {
        return SessionThreadLocal.getInstance().get();
    }

    public static SessionWrapper sessionWrapper() {
        Context context = get();
        if (context == null) {
            return null;
        }

        return context.getSessionWrapper();
    }
}