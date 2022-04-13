package com.peas.xinrui.common.service;

import com.peas.xinrui.api.admin.entity.AdminContexts;
import com.peas.xinrui.api.user.entity.UserContexts;

public class BaseService {

    protected boolean isUserSession() {
        return UserContexts.getUser() != null;
    }

    protected boolean isAdminSession() {
        return AdminContexts.sessionAdmin() != null;
    }
}
