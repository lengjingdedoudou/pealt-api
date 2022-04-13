package com.peas.xinrui.common.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peas.xinrui.common.context.Context;
import com.peas.xinrui.common.context.Contexts;
import com.peas.xinrui.common.controller.auth.RequiredPermission;
import com.peas.xinrui.common.controller.auth.RequiredSchoolPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.web.method.HandlerMethod;

public abstract class BaseInterceptor implements org.springframework.web.servlet.HandlerInterceptor, WebappKeys {

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (CrossDomainHandler.handle(request, response)) {
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 获取客户端信息(api版本号)
        Context context = new Context();
        Contexts.set(context);

        RequiredSession adminType = handlerMethod.getMethodAnnotation(RequiredSession.class);

        if (adminType == null) {
            adminType = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredSession.class);
            if (adminType == null) {
                return true;
            }
        }
        SessionType[] adminTypes = adminType.value();

        if (Arrays.asList(adminTypes).contains(SessionType.NONE)) {
            return true;
        }
        return authenticate(request, response, handlerMethod, context, adminTypes);
    }

    protected abstract boolean authenticate(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Context context, SessionType[] adminTypes) throws Exception;

    protected RequiredPermission getRequestPermission(HandlerMethod handlerMethod) {
        RequiredPermission requiredPermission = handlerMethod.getMethodAnnotation(RequiredPermission.class);
        if (requiredPermission == null) {
            requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
        }
        return requiredPermission;
    }

    protected RequiredSchoolPermission getRequestSchoolPermission(HandlerMethod handlerMethod) {
        RequiredSchoolPermission requiredPermission = handlerMethod.getMethodAnnotation(RequiredSchoolPermission.class);
        if (requiredPermission == null) {
            requiredPermission = handlerMethod.getMethod().getDeclaringClass()
                    .getAnnotation(RequiredSchoolPermission.class);
        }
        return requiredPermission;
    }
}
