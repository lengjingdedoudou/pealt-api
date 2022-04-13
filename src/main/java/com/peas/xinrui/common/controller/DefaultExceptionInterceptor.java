package com.peas.xinrui.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.peas.xinrui.common.L;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.model.ErrorCode;
import com.peas.xinrui.common.resources.LocaleBundles;

@ControllerAdvice
public class DefaultExceptionInterceptor {

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleError(HttpServletRequest request, HandlerMethod handlerMethod, Throwable ex) {
        L.error(ex);
        if (!(ex instanceof ServiceException)) {
            ApiLog.log(request, null);
        }

        ServiceException se;
        if (ex instanceof ServiceException) {
            se = (ServiceException) ex;
        } else {
            L.error(ex);
            se = new ServiceException(ErrorCode.ERR_UNKNOWN_ERROR);
        }
        int errorCode = se.getErrorCode();
        String errorMsg = LocaleBundles.getWithArrayParams("en", "err." + errorCode, se.getErrorParams());
        Map<String, Object> error = new HashMap<>();
        error.put("errcode", errorCode);
        error.put("errmsg", errorMsg);
        if (se.getErrorData() != null) {
            error.put("errdata", se.getErrorData());
        }
        return new ModelAndView(new JsonView(error));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleError404(HttpServletRequest request, Throwable ex) {
        return new ModelAndView(new NotFoundView());
    }

}