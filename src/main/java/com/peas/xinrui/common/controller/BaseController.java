package com.peas.xinrui.common.controller;

import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.peas.xinrui.common.L;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.utils.StringUtils;
import com.sunnysuperman.commons.bean.Bean;
import com.sunnysuperman.commons.bean.ParseBeanOptions;
import com.sunnysuperman.commons.util.FormatUtil;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {

    protected ModelAndView feedBack() {
        return feedBack(null);
    }

    protected ModelAndView feedBack(Object ret) {
        Object result = ret != null ? ret : "success";

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("errcode", 0);
        data.put("result", result);
        return new ModelAndView(new JsonView(data));
    }

    protected static int parseInt(Integer i) {
        return FormatUtil.parseIntValue(i, 0);
    }

    protected static int parseInt(Integer i, int defaultValue) {
        return FormatUtil.parseIntValue(i, defaultValue);
    }

    protected static boolean parseBoolean(String b) {
        return FormatUtil.parseBoolean(b, false);
    }

    protected static boolean parseBoolean(String b, boolean defaultValue) {
        return FormatUtil.parseBoolean(b, defaultValue);
    }

    protected static Date parseDate(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return FormatUtil.parseDate(s);
    }

    // 根据model类型，将modelJson转换为model类型
    public static <T> T parseModel(String modelJSON, T model) throws ServiceException {
        return parseModel(modelJSON, model, null, null);
    }

    protected static <T> T parseModel(String modelJSON, T model, String key) throws ServiceException {
        return parseModel(modelJSON, model, key, null);
    }

    protected static <T> T parseModel(String modelJSON, T model, ParseBeanOptions options) throws ServiceException {
        return parseModel(modelJSON, model, null, options);
    }

    protected static <T> T parseModel(String modelJSON, T model, String key, ParseBeanOptions options)
            throws ServiceException {
        try {
            return Bean.fromJson(modelJSON, model, options);
        } catch (Exception e) {
            L.error(e);
            // throw new ArgumentServiceException(key != null ? key : "model");
            throw new ServiceException(1);
        }
    }

    protected static <T> T parseModel(HttpServletRequest request, T bean) {
        return parseModel(request, bean, null);
    }

    protected static <T> T parseModel(HttpServletRequest request, T bean, Collection<String> excludeKeys) {
        Enumeration<?> enu = request.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        boolean shouldExcludeKeys = excludeKeys != null && !excludeKeys.isEmpty();
        while (enu.hasMoreElements()) {
            String key = enu.nextElement().toString();
            if (shouldExcludeKeys && excludeKeys.contains(key)) {
                continue;
            }
            String value = FormatUtil.parseString(request.getParameter(key));
            value = StringUtils.trimToNull(value);
            if (value == null) {
                continue;
            }
            map.put(key, value);
        }
        return Bean.fromMap(map, bean);
    }

    protected static <T> List<T> parseList(String modelJSON, Class<T> model) throws ServiceException {
        try {
            return Bean.fromJson(modelJSON, model);
        } catch (Exception e) {
            // throw new ArgumentServiceException("model");
            throw new ServiceException(1);

        }

    }

    protected static <T> List<T> parseList(String modelJSON, Class<T> model, String msg) throws ServiceException {
        try {
            return Bean.fromJson(modelJSON, model);
        } catch (Exception e) {
            // throw new ArgumentServiceException(msg);
            throw new ServiceException(1);

        }
    }

    protected static String getHeader(HttpServletRequest request, String key) {
        String value = WebUtils.getHeader(request, key);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    HttpServletRequest getRequest() {
        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ra.getRequest();
    }

    protected String getRemoteAddress() {
        return WebUtils.getRemoteAddress(getRequest());
    }

}