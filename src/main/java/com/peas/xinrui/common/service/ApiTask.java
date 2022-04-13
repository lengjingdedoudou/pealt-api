package com.peas.xinrui.common.service;

import com.peas.xinrui.common.context.SessionThreadLocal;

public abstract class ApiTask implements Runnable {

    @Override
    public final void run() {
        SessionThreadLocal.getInstance().set(null);
        try {
            doApiWork();
        } catch (Throwable t) {
            t.printStackTrace();
            System.err.println(t);
        }
    }

    protected abstract void doApiWork() throws Exception;

}
