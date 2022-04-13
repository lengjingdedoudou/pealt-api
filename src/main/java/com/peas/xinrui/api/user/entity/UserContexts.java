package com.peas.xinrui.api.user.entity;

import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.model.UserSession;
import com.peas.xinrui.common.context.Contexts;
import com.peas.xinrui.common.context.SessionWrapper;
import com.peas.xinrui.common.exception.SessionServiceException;

public class UserContexts extends Contexts {
    public static UserSessionWrapper userSessionWrapper() {
        SessionWrapper wrapper = sessionWrapper();
        if (wrapper == null || !(wrapper instanceof UserSessionWrapper)) {
            return null;
        }

        return (UserSessionWrapper) wrapper;
    }

    public static UserSessionWrapper getUserSessionWrapper() {
        UserSessionWrapper wrapper = userSessionWrapper();
        if (wrapper == null) {
            throw new SessionServiceException();
        }

        return wrapper;
    }

    public static UserSession userSession() {
        return getUserSessionWrapper().getUserSession();
    }

    public static UserSession getUserSession() {
        UserSession userSession = userSession();
        if (userSession == null) {
            throw new SessionServiceException();
        }

        return userSession;
    }

    public static User user() {
        if (userSessionWrapper() == null) {
            return null;
        }

        return userSessionWrapper().getUser();
    }

    public static User getUser() {
        User user = user();
        if (user == null) {
            throw new SessionServiceException();
        }

        return user;
    }

    public static Long userId() {
        User user = user();
        if (user == null) {
            return null;
        }

        return user().getId();
    }

    public static Long getUserId() {
        Long userId = userId();
        if (userId == null) {
            throw new SessionServiceException();
        }

        return userId;
    }
}