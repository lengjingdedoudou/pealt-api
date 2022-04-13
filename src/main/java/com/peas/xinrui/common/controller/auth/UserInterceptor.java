package com.peas.xinrui.common.controller.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peas.xinrui.api.user.entity.UserSessionWrapper;
import com.peas.xinrui.api.user.service.UserService;
import com.peas.xinrui.common.context.Context;
import com.peas.xinrui.common.context.Contexts;
import com.peas.xinrui.common.controller.BaseInterceptor;
import com.peas.xinrui.common.controller.WebUtils;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.exception.SessionServiceException;
import com.peas.xinrui.common.model.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.HandlerMethod;

@ControllerAdvice
public class UserInterceptor extends BaseInterceptor implements ErrorCode {

    @Autowired
    private UserService userService;

    @Override
    protected boolean authenticate(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Context context, SessionType[] adminTypes) throws Exception {

        for (SessionType type : adminTypes) {
            if (type != SessionType.USER) {
                throw new ServiceException(ERR_ILLEGAL_ARGUMENT);
            }
        }

        boolean touchable = false;
        Touchable touchableAnn = handlerMethod.getMethodAnnotation(Touchable.class);
        if (touchableAnn != null) {
            touchable = touchableAnn.value();
        }

        String token = WebUtils.getHeader(request, KEY_USER_TOKEN);
        if (!touchable && token == null) {
            throw new SessionServiceException();
        }

        // 先通过缓存验证session是否过期
        UserSessionWrapper wrapper = userService.findByToken(token);
        if (!touchable && wrapper == null) {
            throw new SessionServiceException();
        }
        context.setSessionWrapper(wrapper);
        Contexts.set(context);
        return true;
    }
}
