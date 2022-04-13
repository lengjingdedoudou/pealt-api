package com.peas.xinrui.api.admin.entity;

import com.peas.xinrui.api.admin.model.Admin;
import com.peas.xinrui.api.admin.model.AdminSession;
import com.peas.xinrui.common.context.Contexts;
import com.peas.xinrui.common.context.SessionWrapper;
import com.peas.xinrui.common.exception.SessionServiceException;

public class AdminContexts extends Contexts {
    public static AdminSessionWrapper adminSessionWrapper() {
        SessionWrapper sessionWrapper = sessionWrapper();
        if (sessionWrapper == null || !(sessionWrapper instanceof AdminSessionWrapper)) {
            return null;
        }

        return (AdminSessionWrapper) sessionWrapper;
    }

    public static AdminSessionWrapper getAdminSessionWrapper() {
        AdminSessionWrapper sessionWrapper = adminSessionWrapper();
        if (sessionWrapper == null) {
            throw new SessionServiceException();
        }

        return sessionWrapper;
    }

    public static AdminSession adminSession() {
        return getAdminSessionWrapper().getAdminSession();
    }

    public static AdminSession getAdminSession() {
        AdminSession session = adminSession();
        if (session == null) {
            throw new SessionServiceException();
        }

        return session;
    }

    public static Admin sessionAdmin() {
        return getAdminSessionWrapper().getAdmin();
    }

    public static Admin getAdmin() {
        Admin admin = sessionAdmin();
        if (admin == null) {
            throw new SessionServiceException();
        }

        return admin;
    }

    public static Long sessionAdminId() {
        return adminSession().getAdminId();
    }

    public static Long getAdminId() {
        Long adminId = sessionAdminId();
        if (adminId == null) {
            throw new SessionServiceException();
        }

        return adminId;
    }
}