package com.peas.xinrui.common.controller.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peas.xinrui.common.context.Context;
import com.peas.xinrui.common.controller.BaseInterceptor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;

@ControllerAdvice
public class CommonInterceptor extends BaseInterceptor {
    @Override
    protected boolean authenticate(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Context context, SessionType[] adminTypes) {

        return true;
    }

}