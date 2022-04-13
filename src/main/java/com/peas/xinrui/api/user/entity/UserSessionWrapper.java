package com.peas.xinrui.api.user.entity;

import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.model.UserSession;
import com.peas.xinrui.common.context.SessionWrapper;

public class UserSessionWrapper implements SessionWrapper {
    private UserSession userSession;
    private User user;

    public UserSessionWrapper() {

    }

    public UserSessionWrapper(UserSession userSession, User user) {
        this.userSession = userSession;
        this.user = user;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}