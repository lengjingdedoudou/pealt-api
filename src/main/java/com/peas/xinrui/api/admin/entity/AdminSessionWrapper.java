package com.peas.xinrui.api.admin.entity;

import com.peas.xinrui.api.admin.model.Admin;
import com.peas.xinrui.api.admin.model.AdminSession;
import com.peas.xinrui.api.admin.model.Role;
import com.peas.xinrui.common.context.SessionWrapper;

public class AdminSessionWrapper implements SessionWrapper {
    private AdminSession adminSession;
    private Admin admin;
    private Role role;

    public AdminSessionWrapper(AdminSession adminSession, Admin admin, Role role) {
        this.adminSession = adminSession;
        this.admin = admin;
        this.role = role;
    }

    public AdminSession getAdminSession() {
        return adminSession;
    }

    public void setAdminSession(AdminSession adminSession) {
        this.adminSession = adminSession;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}