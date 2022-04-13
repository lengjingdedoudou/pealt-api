package com.peas.xinrui.api.schadmin.entity;

import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.model.SchAdminSession;
import com.peas.xinrui.api.schadmin.model.SchRole;
import com.peas.xinrui.common.context.SessionWrapper;

public class SchAdminSessionWrapper implements SessionWrapper {
    private SchAdminSession adminSession;
    private SchAdmin admin;
    private SchRole role;

    public SchAdminSessionWrapper(SchAdminSession adminSession, SchAdmin admin, SchRole role) {
        this.adminSession = adminSession;
        this.admin = admin;
        this.role = role;
    }

    public SchAdminSession getAdminSession() {
        return adminSession;
    }

    public void setAdminSession(SchAdminSession adminSession) {
        this.adminSession = adminSession;
    }

    public SchAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(SchAdmin admin) {
        this.admin = admin;
    }

    public SchRole getRole() {
        return role;
    }

    public void setRole(SchRole role) {
        this.role = role;
    }

}