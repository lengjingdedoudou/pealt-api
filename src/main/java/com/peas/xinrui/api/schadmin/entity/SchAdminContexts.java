package com.peas.xinrui.api.schadmin.entity;

import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.model.SchAdminSession;
import com.peas.xinrui.common.context.Contexts;
import com.peas.xinrui.common.context.SessionWrapper;
import com.peas.xinrui.common.exception.SessionServiceException;

public class SchAdminContexts extends Contexts {
    public static SchAdminSessionWrapper adminSessionWrapper() {
        SessionWrapper sessionWrapper = sessionWrapper();
        if (sessionWrapper == null || !(sessionWrapper instanceof SchAdminSessionWrapper)) {
            return null;
        }

        return (SchAdminSessionWrapper) sessionWrapper;
    }

    public static SchAdminSessionWrapper getAdminSessionWrapper() {
        SchAdminSessionWrapper sessionWrapper = adminSessionWrapper();
        if (sessionWrapper == null) {
            throw new SessionServiceException();
        }

        return sessionWrapper;
    }

    public static SchAdminSession adminSession() {
        return getAdminSessionWrapper().getAdminSession();
    }

    public static SchAdminSession getAdminSession() {
        SchAdminSession session = adminSession();
        if (session == null) {
            throw new SessionServiceException();
        }

        return session;
    }

    public static SchAdmin sessionAdmin() {
        return adminSessionWrapper().getAdmin();
    }

    public static SchAdmin getAdmin() {
        SchAdmin admin = sessionAdmin();
        if (admin == null) {
            throw new SessionServiceException();
        }

        return admin;
    }

    public static Integer sessionAdminId() {
        return adminSession().getSchAdminId();
    }

    public static Integer getAdminId() {
        Integer adminId = sessionAdminId();
        if (adminId == null) {
            throw new SessionServiceException();
        }

        return adminId;
    }

    public static Integer sessionSchoolId() {
        return sessionAdmin().getSchoolId();
    }

    public static Integer getSchoolId() {
        Integer schoolId = sessionSchoolId();
        if (schoolId == null) {
            throw new SessionServiceException();
        }

        return schoolId;
    }
}