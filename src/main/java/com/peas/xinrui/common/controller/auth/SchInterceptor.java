package com.peas.xinrui.common.controller.auth;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.api.schadmin.entity.SchAdminSessionWrapper;
import com.peas.xinrui.api.schadmin.model.SchRole;
import com.peas.xinrui.api.schadmin.service.SchAdminService;
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
public class SchInterceptor extends BaseInterceptor implements ErrorCode {

    @Autowired
    private SchAdminService schAdminService;

    @Override
    protected boolean authenticate(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Context context, SessionType[] adminTypes) throws Exception {

        for (SessionType type : adminTypes) {
            if (type != SessionType.SCHADMIN) {
                throw new ServiceException(ERR_ILLEGAL_ARGUMENT);
            }
        }

        String token = WebUtils.getHeader(request, KEY_SCH_ADMIN_TOKEN);
        if (token == null) {
            throw new SessionServiceException();
        }

        // 先通过缓存验证session是否过期
        SchAdminSessionWrapper wrapper = schAdminService.findByToken(token);
        if (wrapper == null) {
            throw new SessionServiceException();
        }
        boolean authorized = false;

        RequiredSchoolPermission requiresAuth = getRequestSchoolPermission(handlerMethod);

        authorized = requiresAuth == null;

        if (requiresAuth != null) {
            // 权限
            SchAdminPermission[] permissions = requiresAuth.value();
            if (permissions.length == 0) {
                authorized = true;
            }
            if (Arrays.asList(permissions).contains(SchAdminPermission.NONE)) {
                authorized = true;
            }

            // 多个权限满足其一即可
            SchRole role = wrapper.getRole();
            List<String> ps = role.getPermissions();
            for (SchAdminPermission permission : permissions) {
                if (ps.contains(permission.name())) {
                    authorized = true;
                    break;
                }
            }
        }

        if (!authorized) {
            throw new ServiceException(ERR_PERMISSION_DENIED);
        }
        wrapper.getAdmin().setPassword(null);
        context.setSessionWrapper(wrapper);
        Contexts.set(context);
        return true;
    }
}